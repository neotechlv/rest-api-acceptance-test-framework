package lv.neotech.tests.mocks;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.Resources;

import com.github.tomakehurst.wiremock.extension.Parameters;

import org.jtwig.JtwigTemplate;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static com.toomuchcoding.jsonassert.JsonAssertion.assertThat;


public class JTwigTemplateTransformerTest {

    private JTwigTemplateTransformer unit = new JTwigTemplateTransformer();

    @Test
    public void renderTemplate() throws Exception {
        Map<String, Object> valueMap = ImmutableMap.<String, Object>builder()
                .put("oneValueTpl", "oneValue")
                .put("threeValueTpl", "threeValue")
                .build();
        String renderedTemplate = renderTemplate("sample_jtwig_template.json", valueMap);

        assertThat(renderedTemplate).field("one").isEqualTo("oneValue");
        assertThat(renderedTemplate).field("two").isEqualTo("twoValue");
        assertThat(renderedTemplate).field("three").isEqualTo("threeValue");
        assertThat(renderedTemplate).field("unresolved").isEqualTo("");
    }

    private String renderTemplate(String filePath, Map<String, Object> valueMap) throws IOException {
        URL templateUrl = Resources.getResource(filePath);
        String templateBody = Resources.toString(templateUrl, StandardCharsets.UTF_8);

        JtwigTemplate template = JtwigTemplate.inlineTemplate(templateBody);
        Parameters parameters = Parameters.from(valueMap);
        return unit.renderTemplate(template, parameters);
    }

}