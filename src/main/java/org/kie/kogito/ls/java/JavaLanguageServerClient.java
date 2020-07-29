package org.kie.kogito.ls.java;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.lsp4j.ApplyWorkspaceEditParams;
import org.eclipse.lsp4j.ApplyWorkspaceEditResponse;
import org.eclipse.lsp4j.ConfigurationParams;
import org.eclipse.lsp4j.MessageActionItem;
import org.eclipse.lsp4j.MessageParams;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.RegistrationParams;
import org.eclipse.lsp4j.SemanticHighlightingParams;
import org.eclipse.lsp4j.ShowMessageRequestParams;
import org.eclipse.lsp4j.UnregistrationParams;
import org.eclipse.lsp4j.WorkspaceFolder;
import org.eclipse.lsp4j.services.LanguageClient;
import org.kie.kogito.ls.InitializationParamsStore;
import org.kie.kogito.ls.util.MarshallerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class JavaLanguageServerClient implements LanguageClient {

    private Logger logger = LoggerFactory.getLogger(JavaLanguageServerClient.class);
    private InitializationParamsStore initializationParamsStore;

    @Inject
    public JavaLanguageServerClient(InitializationParamsStore initializationParamsStore) {

        this.initializationParamsStore = initializationParamsStore;
    }

    @Override
    public CompletableFuture<ApplyWorkspaceEditResponse> applyEdit(ApplyWorkspaceEditParams params) {
        ApplyWorkspaceEditResponse res = new ApplyWorkspaceEditResponse();
        res.setApplied(initializationParamsStore.getInitializeParams().getCapabilities().getWorkspace().getApplyEdit());
        return CompletableFuture.supplyAsync(() -> res);
    }

    @Override
    public CompletableFuture<Void> registerCapability(RegistrationParams params) {
        return null;
    }

    @Override
    public CompletableFuture<Void> unregisterCapability(UnregistrationParams params) {
        return null;
    }

    @Override
    public void telemetryEvent(Object o) {

    }

    @Override
    public void publishDiagnostics(PublishDiagnosticsParams publishDiagnosticsParams) {
    }

    @Override
    public void showMessage(MessageParams messageParams) {
        logger.info("{}", MarshallerUtil.toJson(messageParams));
    }

    @Override
    public CompletableFuture<MessageActionItem> showMessageRequest(ShowMessageRequestParams showMessageRequestParams) {
        return null;
    }

    @Override
    public void logMessage(MessageParams messageParams) {
        logger.info("{}", MarshallerUtil.toJson(messageParams));
    }

    @Override
    public CompletableFuture<List<WorkspaceFolder>> workspaceFolders() {
        return CompletableFuture.supplyAsync(() -> (initializationParamsStore.getInitializeParams().getWorkspaceFolders()));
    }

    @Override
    public CompletableFuture<List<Object>> configuration(ConfigurationParams configurationParams) {
        return null;
    }

    @Override
    public void semanticHighlighting(SemanticHighlightingParams params) {

    }
}
