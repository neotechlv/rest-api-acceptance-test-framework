package lv.neotech.tests.guice;

import lv.neotech.tests.configuration.TestConfiguration;
import lv.neotech.tests.configuration.TestConfigurationProvider;

public final class CommonsConfigGuiceModule<ENVCONFIG extends TestConfiguration> extends AbstractConfigGuiceModule<ENVCONFIG> {

    public CommonsConfigGuiceModule(Class<ENVCONFIG> configClass) {
        super(configClass);
    }

    @Override
    ENVCONFIG createConfigInstance() {
        return TestConfigurationProvider.fromDefaults(configClass, true);
    }

}
