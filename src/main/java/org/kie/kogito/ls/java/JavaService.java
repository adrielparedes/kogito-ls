package org.kie.kogito.ls.java;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.ls.service.Service;

@ApplicationScoped
public class JavaService extends Service {

    private final String uri = ".java";

    @Inject
    public JavaService(JavaTextDocumentService textDocumentService, JavaWorkspaceService workspaceService) {
        super(textDocumentService, workspaceService);
    }

    @Override
    public boolean isAcceptable(String uri) {
        return uri.endsWith(this.uri);
    }
}
