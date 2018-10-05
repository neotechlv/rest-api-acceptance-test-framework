package lv.neotech.tests.guice;

import com.google.inject.AbstractModule;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EnvConfigGuiceModule<ENVCONFIG extends Config> extends AbstractModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnvConfigGuiceModule.class);

    private final Class<ENVCONFIG> configClass;

    @SuppressWarnings("unchecked")
    public EnvConfigGuiceModule(Class<ENVCONFIG> configClass) {
        this.configClass = configClass;
    }

    @Override
    public void configure() {
        bind(configClass).toProvider(() -> {
            String environment = System.getProperty("env", "local");

            LOGGER.trace("Configuration environment is set to : {}", environment);
            ConfigFactory.setProperty("env", environment);

            return ConfigFactory.create(configClass);
        }).asEagerSingleton();
    }

}
