package at.sintrum.fog.deploymentmanager.api;

import at.sintrum.fog.core.PlatformCoreConfig;
import at.sintrum.fog.deploymentmanager.utils.TarUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Michael Mittermayr on 01.08.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PlatformCoreConfig.class})
public class TarTests {

    private InputStream getFile(String name) throws FileNotFoundException {
        Path path = Paths.get("src/test/resources/" + name);
        return new FileInputStream(path.toFile());
    }

    @Test
    public void testTarMerge() throws IOException {

        InputStream archiveA = getFile("a.tar");
        InputStream archiveB = getFile("c.tar");

        try (FileOutputStream out = new FileOutputStream("C://temp//result.tar")) {
            try (ByteArrayOutputStream outputStream = TarUtils.mergeArchives(archiveA, archiveB)) {
                outputStream.writeTo(out);
            }
        }
    }
}
