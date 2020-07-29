package org.kie.kogito.ls.service;

import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

public abstract class Service {

    protected TextDocumentService textDocumentService;
    protected WorkspaceService workspaceService;

    public Service() {
    }

    public Service(TextDocumentService textDocumentService, WorkspaceService workspaceService) {

        this.textDocumentService = textDocumentService;
        this.workspaceService = workspaceService;
    }

    public abstract boolean isAcceptable(String uri);

    public TextDocumentService getTextDocumentService() {
        return this.textDocumentService;
    }

    public WorkspaceService getWorkspaceService() {
        return this.workspaceService;
    }

    /**
     * The higher the number the higher the priority
     *
     * @return priority number
     */
    public int getPriority() {
        return 0;
    }

    @Override
    public String toString() {
        return "Service{" +
                this.getClass().getSimpleName() +
                '}';
    }
}
