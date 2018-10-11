package lv.neotech.tests.guice;

import lv.neotech.tests.configuration.TestConfiguration;
import lv.neotech.tests.configuration.TestConfigurationProvider;

public final class CommonsConfigGuiceModule<ENVCONFIG extends TestConfiguration> extends AbstractConfigGuiceModule<ENVCONFIG> {

    public CommonsConfigGuiceModule(Class<ENVCONFIG> configClass) {
        super(configClass);
    }

    @Override
    ENVCONFIG createConfigInstance() {
        // If configuration is altered in test runtime to overrride/set the dynamically allocated ports,
        // they will be lost if we reset the config here
        return TestConfigurationProvider.fromDefaults(configClass, false);
    }

}
