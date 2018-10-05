package lv.neotech.tests.cucumber;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfiledCucumberRunnerTest {

    private static final String ENV = "env";

    @Test
    public void shouldSetProfileToSystemVariable() {
        Result result = JUnitCore.runClasses(ProfiledTest.class, UnprofiledTest.class);
        assertThat(result.wasSuccessful()).isTrue();
    }

    @RunWith(ProfiledCucumberRunner.class)
    @TestEnvironmentProfile(name = "dev", systemVarToSet = ENV)
    public static class ProfiledTest extends TestBase {

        @BeforeClass
        public static void shouldSetProfileToSystemVariable() {
            assertThat(getEnvValue()).isEqualTo("dev");
        }

    }

    @RunWith(ProfiledCucumberRunner.class)
    public static class UnprofiledTest extends TestBase {

        @BeforeClass
        public static void shouldNotSetProfileToSystemVariable() {
            assertThat(getEnvValue()).isNull();
        }

    }

    public abstract static class TestBase {

        @AfterClass
        public static void cleanUp() {
            System.clearProperty(ENV);
        }

        static String getEnvValue() {
            return System.getProperty(ENV);
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