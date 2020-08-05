package org.kie.kogito.ls;

import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.lsp4j.CodeActionOptions;
import org.eclipse.lsp4j.CompletionOptions;
import org.eclipse.lsp4j.ExecuteCommandOptions;
import org.eclipse.lsp4j.InitializeParams;
import org.eclipse.lsp4j.InitializeResult;
import org.eclipse.lsp4j.InitializedParams;
import org.eclipse.lsp4j.SaveOptions;
import org.eclipse.lsp4j.ServerCapabilities;
import org.eclipse.lsp4j.TextDocumentSyncKind;
import org.eclipse.lsp4j.TextDocumentSyncOptions;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;
import org.kie.kogito.ls.hub.TextDocumentServiceHub;
import org.kie.kogito.ls.hub.WorkspaceServiceHub;
import org.kie.kogito.ls.util.MarshallerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class KogitoLanguageServer implements LanguageServer {

    private Logger logger = LoggerFactory.getLogger(KogitoLanguageServer.class);
    private final TextDocumentServiceHub textDocumentServiceHub;
    private final WorkspaceServiceHub workspaceServiceHub;
    private InitializationParamsStore initializationParamsStore;

    @Inject
    public KogitoLanguageServer(TextDocumentServiceHub textDocumentServiceHub,
                                WorkspaceServiceHub workspaceServiceHub,
                                InitializationParamsStore initializationParamsStore) {
        this.textDocumentServiceHub = textDocumentServiceHub;
        this.workspaceServiceHub = workspaceServiceHub;
        this.initializationParamsStore = initializationParamsStore;
    }

    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams params) {

        logger.info("Initialization Params: {}", MarshallerUtil.toJson(params));

        this.initializationParamsStore.setInitializeParams(params);

        InitializeResult results = new InitializeResult();

        ServerCapabilities serverCapabilities = new ServerCapabilities();
        CompletionOptions completionOptions = new CompletionOptions();
        completionOptions.setResolveProvider(true);
        serverCapabilities.setCompletionProvider(completionOptions);

        CodeActionOptions options = new CodeActionOptions();
        serverCapabilities.setCodeActionProvider(options);

        TextDocumentSyncOptions textDocumentSyncOptions = new TextDocumentSyncOptions();
        SaveOptions saveOptions = new SaveOptions();
        saveOptions.setIncludeText(true);
        textDocumentSyncOptions.setSave(saveOptions);
        textDocumentSyncOptions.setOpenClose(true);
        textDocumentSyncOptions.setWillSave(true);
        textDocumentSyncOptions.setWillSaveWaitUntil(true);
        textDocumentSyncOptions.setChange(TextDocumentSyncKind.Full);
        serverCapabilities.setTextDocumentSync(textDocumentSyncOptions);

        serverCapabilities.setExecuteCommandProvider(new ExecuteCommandOptions());

        results.setCapabilities(serverCapabilities);
        return CompletableFuture.supplyAsync(() -> results);
    }

    @Override
    public void initialized(InitializedParams params) {
        logger.info("Initialized Params: {}", MarshallerUtil.toJson(params));
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        return CompletableFuture.supplyAsync(() -> Boolean.TRUE);
    }

    @Override
    public void exit() {
        // TODO Auto-generated method stub
    }

    @Override
    public TextDocumentService getTextDocumentService() {
        return textDocumentServiceHub;
    }

    @Override
    public WorkspaceService getWorkspaceService() {
        return workspaceServiceHub;
    }
}