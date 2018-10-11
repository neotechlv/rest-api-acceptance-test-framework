package lv.neotech.tests.cucumber;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see ProfiledCucumberRunner
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TestEnvironmentProfile {

    String DEFAULT_CONFIG_FILE_NAME = "config.properties";

    /**
     * Profile name
     * @return profile name
     */
    String name();

    /**
     * Config file name
     * @return configuration file to load data from
     */
    String configFile() default DEFAULT_CONFIG_FILE_NAME;
}
