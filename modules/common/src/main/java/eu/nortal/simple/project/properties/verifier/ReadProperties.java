package eu.nortal.simple.project.properties.verifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import eu.nortal.simple.project.properties.verifier.exception.RunException;
import eu.nortal.util.properties.LinkedProperties;

/**
 * @author Margus Hanni <margus.hanni@nortal.com>
 */
public class ReadProperties {

    private static final String PROP_PROJECT_SETUP_PROPERTIES_SAMPLE_LOCATION = "project.setup.properties.sample.location";
    private static final String PROP_PROJECT_SETUP_PROPERTIES_STORE_LOCATION = "project.setup.properties.store.location";

    private Properties properties = new LinkedProperties();

    public ReadProperties() throws FileNotFoundException, IOException {

        String conf = "project-setup.properties";

        String confLocation = System.getProperty("configuration");
        if (StringUtils.isNotEmpty(confLocation)) {
            conf = confLocation;
        }

        File confFile = new File(conf);

        System.out.println("#########################################################################");
        System.out.println("Configuration: " + confFile.getAbsolutePath());
        if (confFile.exists()) {
            properties.load(new FileReader(confFile));
        } else {
            System.out.println("Configuration file does not exist.");

            String location = System.getProperty(PROP_PROJECT_SETUP_PROPERTIES_SAMPLE_LOCATION);
            System.out.println("Parameter '" + PROP_PROJECT_SETUP_PROPERTIES_SAMPLE_LOCATION + "' value is '" + location + "'");
            if (StringUtils.isNotEmpty(location)) {
                if (!new File(location).exists()) {
                    throw new RunException("Location '" + location + "' does not exist");
                }
                properties.setProperty(PROP_PROJECT_SETUP_PROPERTIES_SAMPLE_LOCATION, location);
            }

            location = System.getProperty(PROP_PROJECT_SETUP_PROPERTIES_STORE_LOCATION);
            System.out.println("Parameter '" + PROP_PROJECT_SETUP_PROPERTIES_STORE_LOCATION + "' value is '" + location + "'");
            if (StringUtils.isNotEmpty(location)) {
                if (!new File(location).exists()) {
                    throw new RunException("Location '" + location + "' does not exist");
                }
                properties.setProperty(PROP_PROJECT_SETUP_PROPERTIES_STORE_LOCATION, location);
            }
        }
        
        if (properties.isEmpty()) {
            throw new RunException("Configuration parameters are not set!");
        }

        PropertyResolver.resolveProperties(properties);
    }

    public String getPropertiesSampleLocation() {
        return FilenameUtils.normalize(properties.getProperty(PROP_PROJECT_SETUP_PROPERTIES_SAMPLE_LOCATION).replace("/", File.separator));
    }

    public String getPropertiesStoreLocation() {
        return FilenameUtils.normalize(properties.getProperty(PROP_PROJECT_SETUP_PROPERTIES_STORE_LOCATION));
    }
}
