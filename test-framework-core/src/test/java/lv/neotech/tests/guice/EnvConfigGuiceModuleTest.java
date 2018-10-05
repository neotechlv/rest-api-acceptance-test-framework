package lv.neotech.tests.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

public class EnvConfigGuiceModuleTest {

    private Injector injector = Guice.createInjector(new EnvConfigGuiceModule<>(EnvironmentConfig.class));

    @Before
    public void setup () {
        injector.injectMembers(this);
    }

    @Inject
    private EnvironmentConfig environmentConfig;

    @Test
    public void shouldInitWithConfigClass() {
        assertThat(environmentConfig.bookServiceBaseUrl()).isEqualTo("http://localhost:14001/");
    }

}