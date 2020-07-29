package org.kie.kogito.ls.log;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.ls.service.Service;

@ApplicationScoped
public class LogService extends Service {

    @Inject
    public LogService(LogTextDocumentService textDocumentService, LogWorkspaceService workspaceService) {

        super(textDocumentService, workspaceService);
    }

    @Override
    public boolean isAcceptable(String uri) {
        return true;
    }

    @Override
    public int getPriority() {
        return -1000;
    }
}
