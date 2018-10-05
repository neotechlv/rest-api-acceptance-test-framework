package lv.neotech.tests.utils;

import org.rnorth.ducttape.TimeoutException;
import org.rnorth.ducttape.unreliables.Unreliables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.fail;

/**
 * Utility that can be used to wait for application to start
 */
public class AppStartupWait {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppStartupWait.class);

    private String appName;

    private String appUrl;

    private String httpMethod;

    private int timeoutInMillis = 30_000;

    private int expectedHttpStatus;

    public AppStartupWait withAppName(String appName) {
        this.appName = appName;
        return this;
    }

    public AppStartupWait withAppUrlToPoll(String appUrl) {
        this.appUrl = appUrl;
        return this;
    }

    public AppStartupWait withPollMethod(RequestMethod httpMethod) {
        this.httpMethod = httpMethod.name();
        return this;
    }

    public AppStartupWait withPollTimeoutInMillis(int timeoutInMillis) {
        this.timeoutInMillis = timeoutInMillis;
        return this;
    }

    public AppStartupWait withExpectedHttpStatus(int httpStatus) {
        this.expectedHttpStatus = httpStatus;
        return this;
    }

    public void waitUntilStarted() {
        verifyState();

        URL appUrl;
        try {
            appUrl = new URL(this.appUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        LOGGER.info(format("Going to wait for %s API accessibility at %s", appName, appUrl));
        try {
            Unreliables.retryUntilSuccess(timeoutInMillis, TimeUnit.MILLISECONDS, () -> {
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) appUrl.openConnection();
                    connection.setRequestMethod(httpMethod);
                    connection.connect();

                    int code = connection.getResponseCode();
                    if (code != expectedHttpStatus) {
                        throw new RuntimeException("Application not ready");
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }

                return null;
            });
            LOGGER.info(format("%s API is accessible at %s", appName, appUrl));
        } catch (TimeoutException e) {
            String message = format("%s API is not accessible at %s", appName, appUrl);
            LOGGER.error(message, e);
            fail(message);
        }
    }

    private void verifyState() {
        checkState(isNotBlank(this.appName), "Must define app name");
        checkState(isNotBlank(this.appUrl), "Must define app url to check");
        checkState(isNotBlank(this.httpMethod), "Must define HTTP method to use for polling");
        checkState(this.timeoutInMillis > 0, "Timeout should be 1+ of milliseconds");
        checkState(this.expectedHttpStatus > 0, "Must define the expected HTTP status");
    }

    public enum RequestMethod {
        GET, POST
    }
}
