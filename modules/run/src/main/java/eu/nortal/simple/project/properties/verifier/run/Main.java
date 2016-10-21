package eu.nortal.simple.project.properties.verifier.run;

import org.apache.commons.lang3.ArrayUtils;

import eu.nortal.simple.project.properties.verifier.FillProperties;
import eu.nortal.simple.project.properties.verifier.cmd.CmdFillProperties;

/**
 * @author Margus Hanni <margus.hanni@nortal.com>
 */
public class Main {
    public static void main(String[] args) throws Exception {

        FillProperties fillProperties = new CmdFillProperties();

        if (ArrayUtils.contains(args, "verify")) {
            fillProperties.verify();
        } else {
            fillProperties.run();
        }

        // new GuiFillProperties();

    }

    public Main() {
        super();
    }
}