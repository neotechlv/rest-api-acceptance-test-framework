package lv.neotech.tests.cucumber;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import lv.neotech.tests.configuration.GlobalTestSettings;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfiledCucumberRunnerTest {

    @Rule
    public final RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

    @Test
    public void shouldSetProfileToSystemVariable() {
        Result result = JUnitCore.runClasses(ProfiledTest.class);
        assertThat(result.wasSuccessful()).isTrue();
    }

    @RunWith(ProfiledCucumberRunner.class)
    @TestEnvironmentProfile(name = "dev")
    public static class ProfiledTest extends TestBase {

        @BeforeClass
        public static void shouldSetProfileToSystemVariable() {
            assertThat(getEnvValue()).isEqualTo("dev");
        }

    }

    public abstract static class TestBase {

        @AfterClass
        public static void cleanUp() {
            System.clearProperty(GlobalTestSettings.ENV_VAR);
        }

        static String getEnvValue() {
            return GlobalTestSettings.getEnvironment();
        }

    }

    // Will be picked up by runner, since it's in the same package
    public static class MockTestSteps {

        @Given("^All is set$")
        public void allIsSet() {
        }

        @When("^All is peachy$")
        public void allIsPeachy() throws Throwable {
        }

    }
}