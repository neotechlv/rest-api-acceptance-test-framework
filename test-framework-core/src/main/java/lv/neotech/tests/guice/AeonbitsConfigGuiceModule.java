package lv.neotech.tests.guice;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;

public final class AeonbitsConfigGuiceModule<ENVCONFIG extends Config> extends AbstractConfigGuiceModule<ENVCONFIG> {

    public AeonbitsConfigGuiceModule(Class<ENVCONFIG> configClass) {
        super(configClass);
    }

    @Override
    ENVCONFIG createConfigInstance() {
        return ConfigFactory.create(configClass);
    }

}
