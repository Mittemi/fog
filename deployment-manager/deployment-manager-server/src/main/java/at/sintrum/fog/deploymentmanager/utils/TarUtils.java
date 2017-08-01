package at.sintrum.fog.deploymentmanager.utils;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael Mittermayr on 31.07.2017.
 */
public class TarUtils {

    private static final Logger LOG = LoggerFactory.getLogger(TarUtils.class);

    public static ByteArrayOutputStream mergeArchives(InputStream sourceArchive, InputStream targetArchive) throws IOException {

        File fileA = storeTemp(sourceArchive);
        File fileB = storeTemp(targetArchive);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Map<String, TarArchiveEntry> entriesA = listFiles(fileA);
        LOG.debug(entriesA.size() + " files in A");
        Map<String, TarArchiveEntry> entriesB = listFiles(fileB);
        LOG.debug(entriesB.size() + " files in B");

        mergeTar(outputStream, fileA, entriesA, fileB, entriesB);

        removeTemp(fileA);
        removeTemp(fileB);

        return outputStream;
    }

    private static void mergeTar(OutputStream outputStream, File fileA, Map<String, TarArchiveEntry> entriesA, File fileB, Map<String, TarArchiveEntry> entriesB) throws IOException {

        try (TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(outputStream)) {
            tarArchiveOutputStream.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_POSIX);
            tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

            try (TarArchiveInputStream tarA = getArchive(fileA)) {
                try (TarArchiveInputStream tarB = getArchive(fileB)) {

                    Map<String, TarArchiveInputStream> joined = getMergedFileList(entriesA, entriesB, tarA, tarB);
                    LOG.debug(joined.size() + " files in result");

                    addFileToOutput(tarArchiveOutputStream, tarA, joined);
                    addFileToOutput(tarArchiveOutputStream, tarB, joined);
                }
            }
        }
    }

    private static Map<String, TarArchiveInputStream> getMergedFileList(Map<String, TarArchiveEntry> entriesA, Map<String, TarArchiveEntry> entriesB, TarArchiveInputStream tarA, TarArchiveInputStream tarB) {
        Map<String, TarArchiveInputStream> joined = new HashMap<>();

        for (String keyA : entriesA.keySet()) {
            if (!entriesB.containsKey(keyA)) {
                LOG.debug("missing in B, take from A: " + keyA);
                joined.put(keyA, tarA);
            } else {
                TarArchiveEntry entryA = entriesA.get(keyA);
                TarArchiveEntry entryB = entriesB.get(keyA);

                if (entryA.isDirectory() != entryB.isDirectory()) {
                    LOG.error("Problem during tar merge. Directory vs File. Fallback to directory, file skipped: " + keyA);
                    if (entryA.isDirectory()) {
                        LOG.debug("Directory in A");
                        joined.put(keyA, tarA);
                    } else {
                        LOG.debug("Directory in B");
                        joined.put(keyA, tarB);
                    }
                    continue;
                }

                if (entryA.getLastModifiedDate().after(entryB.getLastModifiedDate())) {
                    LOG.debug("Take A since A is newer than B: " + keyA);
                    joined.put(keyA, tarA);
                } else {
                    LOG.debug("Take B since A is older than B: " + keyA);
                    joined.put(keyA, tarB);
                }
            }
        }
        for (String keyB : entriesB.keySet()) {
            if (joined.containsKey(keyB)) {
                continue;
            }
            joined.put(keyB, tarB);
        }
        return joined;
    }

    private static void addFileToOutput(TarArchiveOutputStream tarArchiveOutputStream, TarArchiveInputStream tarIn, Map<String, TarArchiveInputStream> joined) throws IOException {

        TarArchiveEntry tarEntry = null;
        // tarIn is a TarArchiveInputStream
        while ((tarEntry = tarIn.getNextTarEntry()) != null) {
            if (joined.get(tarEntry.getName()) != tarIn) {
                continue;
            }

            if (tarEntry.isDirectory()) {
                tarArchiveOutputStream.putArchiveEntry(tarEntry);
                tarArchiveOutputStream.closeArchiveEntry();
            } else {
                byte[] btoRead = new byte[1024];
                try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
                    int len = 0;
                    while ((len = tarIn.read(btoRead)) != -1) {
                        stream.write(btoRead, 0, len);
                    }

                    tarArchiveOutputStream.putArchiveEntry(tarEntry);
                    tarArchiveOutputStream.write(stream.toByteArray());
                    tarArchiveOutputStream.closeArchiveEntry();
                }
            }
        }
    }


    private static void removeTemp(File fileA) {
        try {
            if (!fileA.delete()) {
                fileA.deleteOnExit();
            }
        } catch (Exception ex) {
            LOG.warn("Failed to delete temp file", ex);
        }
    }

    private static File storeTemp(InputStream sourceArchive) throws IOException {
        File tempFile = File.createTempFile("fog_", ".tar");
        StreamUtils.copy(sourceArchive, new FileOutputStream(tempFile));
        return tempFile;
    }

    private static Map<String, TarArchiveEntry> listFiles(File file) throws IOException {

        Map<String, TarArchiveEntry> result = new HashMap<>();

        try (TarArchiveInputStream tar = getArchive(file)) {
            TarArchiveEntry entry = null;
            while ((entry = tar.getNextTarEntry()) != null) {
                result.put(entry.getName(), entry);
            }
        }
        return result;
    }

    private static TarArchiveInputStream getArchive(File file) throws FileNotFoundException {
        return new TarArchiveInputStream(new BufferedInputStream(new FileInputStream(file)));
    }

    private static URL createTar(File inputDirectoryPath, String dest) throws IOException {

        File outputFile = new File(dest);

        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
             TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(bufferedOutputStream)) {

            tarArchiveOutputStream.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_POSIX);
            tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

            IOFileFilter fileFilter = new IOFileFilter() {
                @Override
                public boolean accept(File file) {
                    return true;
                }

                @Override
                public boolean accept(File dir, String name) {
                    return true;
                }
            };
            List<File> files = new ArrayList<>(FileUtils.listFiles(inputDirectoryPath, fileFilter, fileFilter));


            for (int i = 0; i < files.size(); i++) {
                File currentFile = files.get(i);

                String relativeFilePath = inputDirectoryPath.toURI().relativize(
                        new File(currentFile.getAbsolutePath()).toURI()).getPath();

                TarArchiveEntry tarEntry = new TarArchiveEntry(currentFile, relativeFilePath);
                tarEntry.setSize(currentFile.length());

                tarArchiveOutputStream.putArchiveEntry(tarEntry);
                tarArchiveOutputStream.write(IOUtils.toByteArray(new FileInputStream(currentFile)));
                tarArchiveOutputStream.closeArchiveEntry();
            }
            tarArchiveOutputStream.close();
            return outputFile.toURI().toURL();
        }
    }

    private static void readArchive(File file, String dest) throws IOException {

        File f = new File(dest);
        if (!f.exists()) {
            f.mkdirs();
        }

        try (TarArchiveInputStream tarIn = getArchive(file)) {
            TarArchiveEntry tarEntry = tarIn.getNextTarEntry();
            // tarIn is a TarArchiveInputStream
            while (tarEntry != null) {// create a file with the same name as the tarEntry
                File destPath = new File(dest, tarEntry.getName());
                System.out.println(destPath.getCanonicalPath());
                if (tarEntry.isDirectory()) {
                    destPath.mkdirs();
                } else {
                    destPath.createNewFile();
                    //byte [] btoRead = new byte[(int)tarEntry.getSize()];
                    byte[] btoRead = new byte[1024];
                    //FileInputStream fin
                    //  = new FileInputStream(destPath.getCanonicalPath());
                    BufferedOutputStream bout =
                            new BufferedOutputStream(new FileOutputStream(destPath));
                    int len = 0;

                    while ((len = tarIn.read(btoRead)) != -1) {
                        bout.write(btoRead, 0, len);
                    }

                    bout.close();
                    btoRead = null;

                }
                tarEntry = tarIn.getNextTarEntry();
            }
        }
    }
}
