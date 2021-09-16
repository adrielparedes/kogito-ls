/*
 * Copyright 2021 Red Hat Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.kie.kogito.ls.hub;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.DidChangeWorkspaceFoldersParams;
import org.eclipse.lsp4j.ExecuteCommandParams;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.WorkspaceSymbolParams;
import org.eclipse.lsp4j.services.WorkspaceService;
import org.kie.kogito.ls.service.Service;
import org.kie.kogito.ls.util.MarshallerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class WorkspaceServiceHub extends ServiceHub implements WorkspaceService {

    private final Logger logger = LoggerFactory.getLogger(WorkspaceServiceHub.class);

    @Inject
    public WorkspaceServiceHub(Instance<Service> services) {
        super(services);
    }

    @Override
    public CompletableFuture<Object> executeCommand(ExecuteCommandParams params) {
        logger.info(MarshallerUtil.toJson(params));
        return CompletableFuture.supplyAsync(() -> null);
    }

    @Override
    public CompletableFuture<List<? extends SymbolInformation>> symbol(WorkspaceSymbolParams params) {
        logger.info(MarshallerUtil.toJson(params));
        return null;
    }

    @Override
    public void didChangeConfiguration(DidChangeConfigurationParams params) {
        logger.info(MarshallerUtil.toJson(params));
        this.getServices().forEach(s -> s.getWorkspaceService().didChangeConfiguration(params));
    }

    @Override
    public void didChangeWatchedFiles(DidChangeWatchedFilesParams params) {
        logger.info(MarshallerUtil.toJson(params));
        this.getServices().forEach(s -> s.getWorkspaceService().didChangeWatchedFiles(params));
    }

    @Override
    public void didChangeWorkspaceFolders(DidChangeWorkspaceFoldersParams params) {
        logger.info(MarshallerUtil.toJson(params));
        this.getServices().forEach(s -> s.getWorkspaceService().didChangeWorkspaceFolders(params));
    }
}
