package org.kie.kogito.ls.noop;

import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.services.WorkspaceService;

public class NoopWorkspaceService implements WorkspaceService {

    @Override
    public void didChangeConfiguration(DidChangeConfigurationParams params) {
        
    }

    @Override
    public void didChangeWatchedFiles(DidChangeWatchedFilesParams params) {

    }
}
