package lv.neotech.tests.mocks;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.util.Map;
import java.util.Optional;

public class JTwigTemplateTransformer extends AbstractTemplateTransformer {

    private static final String NAME = "jtwig-transformer";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean applyGlobally() {
        return true;
    }

    @Override
    public final ResponseDefinition transform(Request request,
                                        ResponseDefinition responseDefinition,
                                        FileSource files,
                                        Parameters parameters) {
        if (!responseDefinition.wasConfigured()) {
            return responseDefinition;
        }

        Optional<String> optionalTemplateBody = getTemplateBody(responseDefinition, files);
        if (!optionalTemplateBody.isPresent()) {
            return responseDefinition;
        }

        JtwigTemplate template = initTemplate(optionalTemplateBody.get());
        String rendered = renderTemplate(template, parameters);

        ResponseDefinitionBuilder newResponseDefBuilder = ResponseDefinitionBuilder.like(responseDefinition);
        newResponseDefBuilder.withBody(rendered);
        return newResponseDefBuilder.build();
    }

    JtwigTemplate initTemplate(String templateBody) {
        return JtwigTemplate.inlineTemplate(templateBody);
    }

    String renderTemplate(JtwigTemplate template, Parameters parameters) {
        JtwigModel model = JtwigModel.newModel();

        if (parameters != null) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                model.with(entry.getKey(), entry.getValue());
            }
        }
        return template.render(model);
    }

}
