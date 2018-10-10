package lv.neotech.tests.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.startsWith;

public class DockerSupport {

    private DockerSupport() {}

    /**
     * Host IP detection for Linux & Mac
     */
    public static String currentHostIpInDocker() {
        return optionalCurrentHostIpInDocker().orElseThrow(() -> new RuntimeException("Can't find Docker gateway"));
    }

    private static Optional<String> optionalCurrentHostIpInDocker() {
        String osName = System.getProperty("os.name", StringUtils.EMPTY);
        if (osName.contains("Win")) {
            throw new UnsupportedOperationException("Host IP detection for Windows is not supported yet");
        }
        if (osName.contains("Mac")) {
            return Optional.of("docker.for.mac.localhost");
        }
        Enumeration<NetworkInterface> networkInterfaces;
        try {
            networkInterfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        Optional<NetworkInterface> optionalDockerInterface = Collections.list(networkInterfaces).stream()
                .filter(networkInterface -> startsWith(networkInterface.getName(), "docker"))
                .findAny();
        if (!optionalDockerInterface.isPresent()) {
            return Optional.empty();
        }
        NetworkInterface dockerInterface = optionalDockerInterface.get();
        List<InterfaceAddress> interfaceAddresses = dockerInterface.getInterfaceAddresses();
        Optional<InterfaceAddress> optionalAddress = shouldPickIPv6Address()
                ? pickAddressByType(interfaceAddresses, Inet6Address.class)
                : pickAddressByType(interfaceAddresses, Inet4Address.class);
        return optionalAddress
                .map(thisAddress -> thisAddress.getAddress().getHostAddress());
    }

    private static boolean shouldPickIPv6Address() {
        return "true".equals(System.getProperty("java.net.preferIPv6Addresses"));
    }

    private static Optional<InterfaceAddress> pickAddressByType(List<InterfaceAddress> interfaceAddresses, Class<? extends InetAddress> addressClass) {
        return interfaceAddresses.stream()
                .filter(interfaceAddress -> interfaceAddress.getAddress().getClass().equals(addressClass))
                .findAny();
    }
}
