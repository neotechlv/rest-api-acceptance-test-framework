package lv.neotech.tests.configuration;

import com.google.common.io.Resources;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import lv.neotech.tests.cucumber.TestEnvironmentProfile;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newConcurrentMap;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


public class TestConfigurationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestConfigurationProvider.class);

    /**
     * Prefix for classpath config files
     */
    private static final String CONFIG_FILE_CLASSPATH_PREFIX = "config";

    public static Map<Class, TestConfiguration> CONFIG_CACHE = newConcurrentMap();

    private final List<String> configFileNames = newArrayList();

    private String profile;

    public static <C extends TestConfiguration> C fromDefaults(Class<C> clazz) {
        return fromDefaults(clazz, false);
    }

    public static <C extends TestConfiguration> C fromDefaults(Class<C> clazz, boolean reloadIfPresent) {
        return new TestConfigurationProvider()
                .withProfile(GlobalTestSettings.getEnvironment())
                .withPropertyFile(GlobalTestSettings.getConfigFile())
                .provide(clazz, reloadIfPresent);
    }

    public static void resetCache() {
        CONFIG_CACHE.clear();
    }

    public TestConfigurationProvider withPropertyFile(String propertyFileName) {
        this.configFileNames.add(propertyFileName);
        return this;
    }

    public TestConfigurationProvider withProfile(String profile) {
        this.profile = profile;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <C extends TestConfiguration> C provide(Class<C> clazz, boolean reloadIfPresent) {
        checkState(isNotBlank(profile), "Test profile is undefined");

        if (CONFIG_CACHE.containsKey(clazz) && !reloadIfPresent) {
            LOGGER.info("Test configuration for {} already loaded, reusing", clazz.getName());
            return (C) CONFIG_CACHE.get(clazz);
        } else {
            if (configFileNames.isEmpty()) {
                configFileNames.add(TestEnvironmentProfile.DEFAULT_CONFIG_FILE_NAME);
            }
            LOGGER.info("Loading test configuration");

            C configuration = newConfiguration(clazz, profile, configFileNames);
            CONFIG_CACHE.put(clazz, configuration);

            return configuration;
        }
    }

    private <C extends TestConfiguration> C newConfiguration(Class<C> clazz, String profile, List<String> propertyFiles) {
        C config;
        try {
            config = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.error("Failed to instantiate configuration class {}", clazz, e);
            throw new RuntimeException(e);
        }

        // Runtime options take precedence
        config.addConfiguration(new SystemConfiguration());

        propertyFiles.forEach(propertyFileName -> {
            Path fullPathToFile;
            File propertyFile = new File(propertyFileName);
            if (propertyFile.isAbsolute()) {
                fullPathToFile = propertyFile.toPath();
            } else {
                fullPathToFile = Paths.get(CONFIG_FILE_CLASSPATH_PREFIX, profile, propertyFileName);
            }

            // Should exist as an absolute path or as a classpath resource
            if (Files.exists(fullPathToFile) || Resources.getResource(fullPathToFile.toString()) != null) {
                try {
                    config.addConfiguration(new PropertiesConfiguration(fullPathToFile.toString()));
                } catch (ConfigurationException e) {
                    LOGGER.error("Failed to load configuration from {}", fullPathToFile, e);
                }
            } else {
                LOGGER.error("Configuration file {} not found", fullPathToFile);
            }
        });

        return config;
    }

}
