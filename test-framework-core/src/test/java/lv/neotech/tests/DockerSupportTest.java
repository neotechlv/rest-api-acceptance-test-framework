package lv.neotech.tests;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DockerSupportTest {

    @Test
    public void currentHostIpInDocker() throws Exception {
        assertThat(DockerSupport.currentHostIpInDocker()).isNotEmpty();
    }

}