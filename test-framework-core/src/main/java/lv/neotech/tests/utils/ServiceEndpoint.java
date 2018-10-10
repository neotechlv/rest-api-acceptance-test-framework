package lv.neotech.tests.utils;

import static java.lang.String.format;

public class ServiceEndpoint {

    private final String host;

    private final int port;

    public ServiceEndpoint(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getBaseUrl() {
        return format("http://%s:%s/", getHost(), getPort());
    }

}
