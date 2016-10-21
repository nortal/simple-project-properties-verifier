package eu.nortal.simple.project.properties.verifier.cmd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.StringUtils;

import eu.nortal.simple.project.properties.verifier.FillProperties;
import eu.nortal.simple.project.properties.verifier.ReadProperties;
import eu.nortal.simple.project.properties.verifier.exception.RunException;
import eu.nortal.simple.project.properties.verifier.holder.PropertyInfo;
import eu.nortal.util.properties.LinkedProperties;

/**
 * @author Margus Hanni <margus.hanni@nortal.com>
 */
public class CmdFillProperties implements FillProperties {

    private final ReadProperties properies;

    public CmdFillProperties() throws FileNotFoundException, IOException {
        properies = new ReadProperties();
    }

    public void check() throws FileNotFoundException, IOException {

        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));) {
            for (File file : this.collectProperySamples()) {
                this.readPropertiesAndAsk(console, file);
            }

            System.out.println("#########################################################################");
            System.out.println("#########################################################################");
        }
    }

    @Override
    public void run() {

        try {
            try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));) {
                for (File file : this.collectProperySamples()) {
                    this.readPropertiesAndAsk(console, file);
                }

                System.out.println("#########################################################################");
                System.out.println("#########################################################################");
            }
        } catch (Exception ex) {
            throw new RunException(ex);
        }
    }

    @Override
    public void verify() {
        try {
            Set<String> errors = new LinkedHashSet<>();

            System.out.println("#########################################################################");
            System.out.println("Verifying property files");
            System.out.println("#########################################################################");

            for (File file : this.collectProperySamples()) {
                this.readPropertiesAndVerify(errors, file);
            }

            System.out.println("#########################################################################");

            if (!errors.isEmpty()) {
                System.out.println("ERRORS:");
                for (String error : errors) {
                    System.out.println(error);
                }

                System.out.println("#########################################################################");
                System.exit(1);
            }
            System.out.println("OK");
            System.out.println("#########################################################################");
        } catch (Exception ex) {
            throw new RunException(ex);
        }

    }

    private void readPropertiesAndVerify(Set<String> errors, File propertyFile) throws FileNotFoundException, IOException {
        LinkedProperties defaultProperties = new LinkedProperties();
        defaultProperties.load(new FileReader(propertyFile));

        String propertyFilename = FilenameUtils
                .normalize(StringUtils.removeStart(propertyFile.getAbsolutePath(),
                                                   StringUtils.substringBeforeLast(propertyFile.getAbsolutePath(), properies.getPropertiesSampleLocation())));

        propertyFilename = StringUtils.removeStart(propertyFilename, properies.getPropertiesSampleLocation());

        Properties originalProperties = new LinkedProperties();

        File originalPropertyFile = new File(FilenameUtils.normalize(properies.getPropertiesStoreLocation() + File.separator + propertyFilename));

        if (originalPropertyFile.exists()) {
            originalProperties.load(new FileReader(originalPropertyFile));
        } else {
            errors.add(String.format("Property file \"%s\" does not exist", propertyFilename));
            return;
        }

        for (Object key : defaultProperties.orderedKeys()) {

            String keyS = key.toString();
            if (keyS.endsWith(PropertyInfo.DESCRIPTION_SUFFIX)) {
                continue;
            }

            PropertyInfo propertyInfo = new PropertyInfo(keyS.toString(), defaultProperties);
            if (!originalProperties.containsKey(propertyInfo.getKey())) {
                errors.add(String.format("Property file \"%s\" does not contain key \"%s\"", propertyFilename, propertyInfo.getKey()));
            }
        }

        for (Object key : originalProperties.keySet()) {
            String keyS = key.toString();
            if (!defaultProperties.containsKey(keyS)) {
                errors.add(String.format("Property file \"%s\" contains redundant key \"%s\"", propertyFilename, keyS));
            }
        }
    }

    private void readPropertiesAndAsk(BufferedReader console, File propertyFile) throws FileNotFoundException, IOException {
        LinkedProperties defaultProperties = new LinkedProperties();
        defaultProperties.load(new FileReader(propertyFile));

        String propertyFilename = FilenameUtils
                .normalize(StringUtils.removeStart(propertyFile.getAbsolutePath(),
                                                   StringUtils.substringBeforeLast(propertyFile.getAbsolutePath(), properies.getPropertiesSampleLocation())));

        propertyFilename = StringUtils.removeStart(propertyFilename, properies.getPropertiesSampleLocation());

        System.out.println();
        System.out.println("#########################################################################");
        System.out.println("Property file: " + propertyFilename);
        System.out.println("#########################################################################");
        System.out.println();

        LinkedProperties newProperties = new LinkedProperties();
        LinkedProperties oldProperties = new LinkedProperties();

        File createOrReplacePropertyFile = new File(FilenameUtils.normalize(properies.getPropertiesStoreLocation() + File.separator + propertyFilename));

        if (createOrReplacePropertyFile.exists()) {
            oldProperties.load(new FileReader(createOrReplacePropertyFile));
        }

        for (Object key : defaultProperties.orderedKeys()) {

            String keyS = key.toString();
            if (keyS.endsWith(PropertyInfo.DESCRIPTION_SUFFIX)) {
                continue;
            }

            PropertyInfo propertyInfo = new PropertyInfo(keyS.toString(), defaultProperties);

            String askEntry = propertyInfo.getDescription();

            // Vaikeväärtus
            String defaultValue = oldProperties.getProperty(keyS);

            if (StringUtils.isEmpty(defaultValue)) {
                defaultValue = StringUtils.trimToEmpty(propertyInfo.getDefaultValue());
            }

            if (!defaultValue.isEmpty()) {
                askEntry += " [" + defaultValue + "]";
            }

            askEntry += ": ";
            System.out.println(askEntry);
            String askInput = console.readLine();
            if (StringUtils.isEmpty(askInput)) {
                askInput = defaultValue;
            }

            newProperties.put(keyS, askInput);
        }

        this.saveProperties(createOrReplacePropertyFile, newProperties);
    }

    private void saveProperties(File createOrReplacePropertyFile, Properties properties) throws FileNotFoundException, IOException {
        if (createOrReplacePropertyFile.getParent() != null) {
            File directory = new File(createOrReplacePropertyFile.getParent());
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
        try (OutputStream output = new FileOutputStream(createOrReplacePropertyFile)) {
            properties.store(output, null);
        }
    }

    private Collection<File> collectProperySamples() throws UnsupportedEncodingException {
        String sampleLocation = properies.getPropertiesSampleLocation();

        File sampleLocationFile = new File(sampleLocation);
        System.out.println("Samples location: " + sampleLocationFile.getAbsolutePath());

        return FileUtils.listFiles(sampleLocationFile, FileFilterUtils.suffixFileFilter(".properties"), TrueFileFilter.INSTANCE);
    }
}
