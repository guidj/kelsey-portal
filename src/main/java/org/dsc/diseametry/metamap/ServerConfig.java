package org.dsc.diseametry.metamap;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerConfig {

    private final static String METAMAP_CONFIG_FILE = "metamap.properties";
    private final static Logger LOGGER = Logger.getLogger(ServerConfig.class.getName());

    Properties properties;

    public ServerConfig() {
        InputStream inputStream;

        properties = new Properties();

        inputStream = getClass().getClassLoader().getResourceAsStream(METAMAP_CONFIG_FILE);

        try {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("Property file '" + METAMAP_CONFIG_FILE + "' not found in the classpath");
            }
        } catch (IOException e) {
            LOGGER.error(e);
            throw new RuntimeException(e);
        }
    }

    public String getProperty(CONFIG key){
        return this.properties.getProperty(key.getName());
    }
}
