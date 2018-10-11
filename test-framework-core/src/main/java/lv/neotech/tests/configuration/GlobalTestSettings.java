package lv.neotech.tests.configuration;

public final class GlobalTestSettings {

    public static String ENV_VAR = "test-env";

    public static String CONFIG_FILE = "test-config-file";

    private GlobalTestSettings() {
    }

    public static String getEnvironment() {
        return System.getProperty(ENV_VAR);
    }

    public static String getConfigFile() {
        return System.getProperty(CONFIG_FILE);
    }

    public static void setEnvironment(String environment) {
        System.setProperty(ENV_VAR, environment);
    }

    public static void setConfigFile(String configFile) {
        System.setProperty(CONFIG_FILE, configFile);
    }
}
