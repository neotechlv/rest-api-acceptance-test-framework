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

    /**
     * Profile name
     * @return
     */
    String name();

    /**
     * System variable to set to this profile name
     * @return
     */
    String systemVarToSet() default "env";

}
