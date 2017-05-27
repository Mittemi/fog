package at.sintrum.fog.core.service;

import at.sintrum.fog.core.config.FogApplicationConfigProperties;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by Michael Mittermayr on 24.05.2017.
 */
@Service
public class EnvironmentInfoServiceImpl implements EnvironmentInfoService {

    private final org.slf4j.Logger LOG = LoggerFactory.getLogger(EnvironmentInfoServiceImpl.class);
    private FogApplicationConfigProperties fogApplicationConfigProperties;

    public EnvironmentInfoServiceImpl(FogApplicationConfigProperties fogApplicationConfigProperties) {
        this.fogApplicationConfigProperties = fogApplicationConfigProperties;
    }

    @Override
    public String getOwnContainerId() {
        return getHostname();
    }

    @Override
    public boolean isInsideContainer() {
        if (isWin()) {
            return false;
        }

        if (isLinux()) {
            File f = new File("/.dockerenv");
            return f.exists();
        }

        LOG.warn("Unable to determine if application runs inside a container!");
        return false;
    }

    @Override
    public String getDeploymentManagerUrl() {
        return fogApplicationConfigProperties.getDeploymentManagerUrl();
    }

    private String getHostname() {
        if (isWin()) {
            return System.getenv("COMPUTERNAME");
            //return execReadToString("hostname");
        } else if (isLinux()) {
            return System.getenv("HOSTNAME");
            //return execReadToString("hostname");
            //return execReadToString("cat /etc/hostname");
        }
        LOG.warn("Unable to determine the hostname!");
        return null;
    }

    private boolean isLinux() {
        String OS = System.getProperty("os.name").toLowerCase();
        return OS.contains("nix") || OS.contains("nux");
    }

    private boolean isWin() {
        String OS = System.getProperty("os.name").toLowerCase();
        return OS.contains("win");
    }

    private String execReadToString(String execCommand) {
        try {
            Process proc = Runtime.getRuntime().exec(execCommand);
            try (InputStream stream = proc.getInputStream()) {
                try (Scanner s = new Scanner(stream).useDelimiter("\\A")) {
                    return s.hasNext() ? s.next() : "";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
