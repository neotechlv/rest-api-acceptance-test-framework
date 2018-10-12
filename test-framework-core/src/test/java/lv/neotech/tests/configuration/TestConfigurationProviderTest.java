package lv.neotech.tests.configuration;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;

import lv.neotech.tests.guice.CommonsEnvironmentConfig;

import static org.assertj.core.api.Assertions.assertThat;

public class TestConfigurationProviderTest {

    private static final String CONFIG_FILE = "base.properties";

    @Rule
    public final RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

    @Before
    public void before() {
        TestConfigurationProvider.resetCache();
    }

    @Test
    public void shouldProvideFromDefaults() throws Exception {
        GlobalTestSettings.setEnvironment("ci");
        GlobalTestSettings.setConfigFile(CONFIG_FILE);

        CommonsEnvironmentConfig config = TestConfigurationProvider.fromDefaults(CommonsEnvironmentConfig.class);
        assertThat(config.bookServiceBaseUrl()).isEqualTo("http://api.school-app.net:12345/");
    }

    @Test
    public void shouldNotReloadIfAlreadyLoaded() throws Exception {
        assertThat(getEnvironmentConfig("ci", false).bookServiceBaseUrl())
                .isEqualTo("http://api.school-app.net:12345/");

        assertThat(getEnvironmentConfig("local", false).bookServiceBaseUrl())
                .isEqualTo("http://api.school-app.net:12345/");
    }

    @Test
    public void shouldNotReloadIfAlreadyLoaded2() throws Exception {
        assertThat(getEnvironmentConfig("ci", true).bookServiceBaseUrl())
                .isEqualTo("http://api.school-app.net:12345/");

        assertThat(getEnvironmentConfig("local", false).bookServiceBaseUrl())
                .isEqualTo("http://api.school-app.net:12345/");
    }

    @Test
    public void shouldReloadIfRequired() throws Exception {
        assertThat(getEnvironmentConfig("ci", false).bookServiceBaseUrl())
                .isEqualTo("http://api.school-app.net:12345/");

        assertThat(getEnvironmentConfig("local", true).bookServiceBaseUrl())
                .isEqualTo("http://localhost:14001/");
    }

    private CommonsEnvironmentConfig getEnvironmentConfig(String profile, boolean reload) {
        return new TestConfigurationProvider()
                .withProfile(profile)
                .withPropertyFile(CONFIG_FILE)
                .provide(CommonsEnvironmentConfig.class, reload);
    }

}