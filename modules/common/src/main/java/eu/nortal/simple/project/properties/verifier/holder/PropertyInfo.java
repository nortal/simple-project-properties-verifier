package eu.nortal.simple.project.properties.verifier.holder;

import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Margus Hanni <margus.hanni@nortal.com>
 */
public class PropertyInfo {

    public static final String DESCRIPTION_SUFFIX = ".description";

    private final String key;
    private String defaultValue;
    private String description;

    public PropertyInfo(String key, Properties properties) {
        super();
        this.key = key;
        this.defaultValue = StringUtils.trimToEmpty(properties.getProperty(key));

        this.description = properties.getProperty(key + DESCRIPTION_SUFFIX);
        if (StringUtils.isEmpty(this.description)) {
            this.description = this.key;
        }
    }

    public String getKey() {
        return key;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public String getDescription() {
        return description;
    }

}
