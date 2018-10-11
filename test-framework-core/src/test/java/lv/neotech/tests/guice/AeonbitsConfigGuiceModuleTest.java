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

public class AeonbitsConfigGuiceModuleTest {

    private Injector injector;

    @Rule
    public final RestoreSystemProperties restoreSystemProperties = new RestoreSystemProperties();

    @Before
    public void setup () {
        GlobalTestSettings.setEnvironment("ci");

        AeonbitsConfigGuiceModule<AeonbitsEnvironmentConfig> module = new AeonbitsConfigGuiceModule<>(AeonbitsEnvironmentConfig.class);
        injector = Guice.createInjector(module);
        injector.injectMembers(this);
    }

    @Inject
    private AeonbitsEnvironmentConfig environmentConfig;

    @Test
    public void shouldInitWithConfigClass() {
        assertThat(environmentConfig.bookServiceBaseUrl()).isEqualTo("http://api.school-app.net:12345/");
    }

}