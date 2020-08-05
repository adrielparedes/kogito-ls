package org.kie.kogito.ls.bpmn;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.ls.service.Service;

@ApplicationScoped
public class BpmnService extends Service {

    List<String> allowedUri = Arrays.asList(".bpmn", ".bpmn2");

    @Inject
    public BpmnService(BpmnTextDocumentService textDocumentService, BpmnWorkspaceService workspaceService) {
        super(textDocumentService, workspaceService);
    }

    @Override
    public boolean isAcceptable(String uri) {
        return allowedUri.stream().anyMatch(uri::endsWith);
    }
}
