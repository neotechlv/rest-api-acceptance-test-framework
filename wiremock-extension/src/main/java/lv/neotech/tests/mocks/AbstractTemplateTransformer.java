package lv.neotech.tests.mocks;

import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.common.TextFile;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;

import java.util.Optional;

public abstract class AbstractTemplateTransformer extends ResponseDefinitionTransformer {

    protected Optional<String> getTemplateBody(ResponseDefinition responseDefinition, FileSource files) {
        String templateBody = null;
        if (responseDefinition.specifiesBodyContent()) {
            templateBody = responseDefinition.getBody();
        } else if (responseDefinition.specifiesBodyFile()) {
            TextFile file = files.getTextFileNamed(responseDefinition.getBodyFileName());
            templateBody = file.readContentsAsString();
        }
        return Optional.ofNullable(templateBody);
    }

}
