package lv.neotech.tests.utils;

import org.junit.Test;

import lv.neotech.tests.utils.DockerSupport;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DockerSupportTest {

    @Test
    public void currentHostIpInDocker() throws Exception {
        assertThat(DockerSupport.currentHostIpInDocker()).isNotEmpty();
    }

}