package lv.neotech.tests.cucumber;

import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import java.io.IOException;

import cucumber.api.junit.Cucumber;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.io.ResourceLoader;
import lv.neotech.tests.configuration.GlobalTestSettings;

import static com.google.common.base.Preconditions.checkState;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * <p>Extended Cucumber test runner that sets the information about current test profile
 * as a system property.</p>
 *
 * <p>Sample usage:</p>
 * <pre>
 * {@code
 *
 * @literal @RunWith(ProfiledCucumberRunner.class)
 * @literal @TestEnvironmentProfile(name = "ci")
 * @literal @CucumberOptions(
 *      features = "classpath:features",
 *      glue = { "com.schoollibrary.tests.steps", "com.schoollibrary.tests.hooks" })
 *  public class AcceptanceLibraryServiceTest {
 *  }
 * }
 * </pre>
 * <br/>
 * <pre>
 * {@code
 *
 * @literal @RunWith(ProfiledCucumberRunner.class)
 * @literal @TestEnvironmentProfile(name = "local")
 * @literal @CucumberOptions(
 *      features = "classpath:features",
 *      glue = { "com.schoollibrary.tests.steps", "com.schoollibrary.tests.hooks" })
 *  public class LocalLibraryServiceTest {
 *  }
 * }
 * </pre>
 *
 * <p>The `test-env` property set for test runtime can be used to load an appropriate config file</p>
 */
public class ProfiledCucumberRunner extends Cucumber {

    public ProfiledCucumberRunner(Class clazz) throws InitializationError, IOException {
        super(clazz);
    }

    @Override
    protected Runtime createRuntime(ResourceLoader resourceLoader, ClassLoader classLoader, RuntimeOptions runtimeOptions)
            throws InitializationError, IOException {
        TestClass testClass = getTestClass();
        TestEnvironmentProfile environmentProfile = testClass.getAnnotation(TestEnvironmentProfile.class);

        checkState(environmentProfile != null && isNotBlank(environmentProfile.name()),
                "Environment profile definition in @%s is missing", TestEnvironmentProfile.class.getSimpleName());

        GlobalTestSettings.setEnvironment(environmentProfile.name());
        GlobalTestSettings.setConfigFile(environmentProfile.configFile());

        return super.createRuntime(resourceLoader, classLoader, runtimeOptions);
    }

}
