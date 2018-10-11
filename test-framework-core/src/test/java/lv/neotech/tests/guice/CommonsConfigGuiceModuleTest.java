package lv.neotech.tests.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.RestoreSystemProperties;

import javax.inject.Inject;

import lv.neotech.tests.configuration.GlobalTestSettings;

import static org.assertj.core.api.Assertions.assertThat;

public class CommonsConfigGuiceModuleTest {

    private Injector injector;

    @Rule
    public final RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

    @Before
    public void setup () {
        GlobalTestSettings.setEnvironment("ci");
        GlobalTestSettings.setConfigFile("base.properties");

        CommonsConfigGuiceModule<CommonsEnvironmentConfig> module = new CommonsConfigGuiceModule<>(CommonsEnvironmentConfig.class);
        injector = Guice.createInjector(module);
        injector.injectMembers(this);
    }

    @Inject
    private CommonsEnvironmentConfig configuration;

    @Test
    public void shouldInitWithConfigClass() {
        assertThat(configuration.bookServiceBaseUrl()).isEqualTo("http://api.school-app.net:12345/");
    }

}