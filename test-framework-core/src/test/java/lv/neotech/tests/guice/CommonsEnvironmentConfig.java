package lv.neotech.tests.guice;

import lv.neotech.tests.configuration.TestConfiguration;

public class CommonsEnvironmentConfig extends TestConfiguration {

    private static final String BOOK_SERVICE_BASE_URL = "book-service.baseUrl";

    public String bookServiceBaseUrl() {
        return getString(BOOK_SERVICE_BASE_URL);
    }

}
