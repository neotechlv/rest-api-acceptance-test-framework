package lv.neotech.tests.mocks;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;

import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import freemarker.template.Template;

import org.junit.Test;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThat;
import static org.mockito.Mockito.mock;

public class FreemarkerTemplateTransformerTest {

    private FreemarkerTemplateTransformer unit = new FreemarkerTemplateTransformer();

    @Test
    public void shouldRenderTemplateWithCallback() throws Exception {
        URL templateUrl = Resources.getResource("sample_freemarker_template.json");
        String templateBody = Resources.toString(templateUrl, StandardCharsets.UTF_8);

        Template template = unit.initTemplate(mock(ResponseDefinition.class), templateBody);
        Map<String, Object> valueMap = ImmutableMap.<String, Object>builder()
                .put("oneValueTpl", "oneValue")
                .put("twoValueTpl", "twoValue")
                .put("concatenator", new ConcatenatingCallback())
                .build();
        Parameters parameters = Parameters.from(valueMap);
        String renderedTemplate = unit.renderTemplate(template, parameters);

        assertThat(renderedTemplate).field("one").isEqualTo("oneValue");
        assertThat(renderedTemplate).field("two").isEqualTo("twoValue");
        assertThat(renderedTemplate).field("concatenated").isEqualTo("oneValue_twoValue");
    }

    public static class ConcatenatingCallback {

        @SuppressWarnings("unused")
        public String concatenate(String... parts) {
            return Joiner.on("_").join(parts);
        }
    }
}