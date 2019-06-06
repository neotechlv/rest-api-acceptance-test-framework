package lv.neotech.tests.mocks;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.VerificationException;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.responsetemplating.ResponseTemplateTransformer;
import com.github.tomakehurst.wiremock.verification.LoggedRequest;
import com.github.tomakehurst.wiremock.verification.NearMiss;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.google.common.base.Preconditions.checkArgument;
import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractServiceMock {

    protected WireMockServer mock;

    protected void start(int port, FileSource definitionFileSource) {
        checkArgument(port > 0, "Invalid port");
        mock = new WireMockServer(
                options()
                        .port(port)
                        .fileSource(definitionFileSource)
                        .extensions(new FreemarkerTemplateTransformer(), new ResponseTemplateTransformer(false))
        );
        mock.start();
    }

    public final void destroy() {
        mock.stop();
    }

    public final void simulateFailure() {
        mock.stubFor(
                post(urlMatching("/.*"))
                        .willReturn(aResponse()
                                .withStatus(500)
                        ));

        mock.stubFor(
                get(urlMatching("/.*"))
                        .willReturn(aResponse()
                                .withStatus(500)
                        ));
    }

    public final void resetAllStubs() {
        mock.resetAll();
    }

    public final void verifyNoRequestMismatches() {
        List<NearMiss> nearMisses = mock.findNearMissesForUnmatchedRequests().getNearMisses();
        if (!nearMisses.isEmpty()) {
            throw VerificationException.forUnmatchedNearMisses(nearMisses);
        }
        List<LoggedRequest> allUnmatchedRequests = mock.findUnmatchedRequests().getRequests();
        // Unfortunately, no nice Wiremock throwable for "far misses"
        assertThat(allUnmatchedRequests).as("Received yet unbound requests to %s mock", this.getClass()).isEmpty();
    }

}
