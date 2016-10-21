package eu.nortal.simple.project.properties.verifier;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author Margus Hanni <margus.hanni@nortal.com>
 */
public class PropertyResolver {

    public static void resolveProperties(Properties props) {
        Map<String, String> allProps = resolveMap(props);

        Set<String> sysPropRefs = allProps.keySet();

        Enumeration<?> names = props.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String value = props.getProperty(name);
            for (String ref : sysPropRefs) {
                if (value.contains(ref)) {
                    value = value.replace(ref, allProps.get(ref));
                }
            }
            props.setProperty(name, value);
        }
    }

    private static Map<String, String> resolveMap(Properties props) {
        Map<String, String> propsMap = new HashMap<String, String>(props.size());

        Enumeration<?> names = props.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            propsMap.put("${" + name + "}", props.getProperty(name));
        }
        return propsMap;
    }

}
