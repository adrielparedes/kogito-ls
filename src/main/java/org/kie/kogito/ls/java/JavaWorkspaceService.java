package org.kie.kogito.ls.java;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.DidChangeWorkspaceFoldersParams;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.WorkspaceSymbolParams;
import org.eclipse.lsp4j.services.WorkspaceService;
import org.kie.kogito.ls.util.MarshallerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class JavaWorkspaceService implements WorkspaceService {

    private final Logger logger = LoggerFactory.getLogger(JavaWorkspaceService.class);
    private final WorkspaceService workspaceService;

    @Inject
    public JavaWorkspaceService(@Java WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @Override
    public CompletableFuture<Object> executeCommand(ExecuteCommandParams params) {
        return this.workspaceService.executeCommand(params);
    }

    @Override
    public CompletableFuture<List<? extends SymbolInformation>> symbol(WorkspaceSymbolParams params) {
        return this.workspaceService.symbol(params);
    }

    @Override
    public void didChangeConfiguration(DidChangeConfigurationParams didChangeConfigurationParams) {
        this.workspaceService.didChangeConfiguration(didChangeConfigurationParams);
    }

    @Override
    public void didChangeWatchedFiles(DidChangeWatchedFilesParams didChangeWatchedFilesParams) {
        logger.info("{}", MarshallerUtil.toJson(didChangeWatchedFilesParams));
        this.workspaceService.didChangeWatchedFiles(didChangeWatchedFilesParams);
    }

    @Override
    public void didChangeWorkspaceFolders(DidChangeWorkspaceFoldersParams params) {
        this.workspaceService.didChangeWorkspaceFolders(params);
    }
}
