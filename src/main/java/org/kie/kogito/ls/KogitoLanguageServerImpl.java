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
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;
import org.kie.kogito.ls.engine.ActivatorChecker;
import org.kie.kogito.ls.extension.KogitoTextDocumentService;
import org.kie.kogito.ls.extension.KogitoTextDocumentServiceImpl;
import org.kie.kogito.ls.hub.TextDocumentServiceHub;
import org.kie.kogito.ls.hub.WorkspaceServiceHub;
import org.kie.kogito.ls.util.MarshallerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class KogitoLanguageServerImpl implements KogitoLanguageServer {

    private final Logger logger = LoggerFactory.getLogger(KogitoLanguageServerImpl.class);
    private final TextDocumentServiceHub textDocumentServiceHub;
    private final WorkspaceServiceHub workspaceServiceHub;
    private final InitializationParamsStore initializationParamsStore;
    private final ActivatorChecker activatorChecker;
    private final KogitoTextDocumentServiceImpl kogitoTextDocumentService;

    @Inject
    public KogitoLanguageServerImpl(TextDocumentServiceHub textDocumentServiceHub,
                                    WorkspaceServiceHub workspaceServiceHub,
                                    InitializationParamsStore initializationParamsStore,
                                    ActivatorChecker activatorChecker,
                                    KogitoTextDocumentServiceImpl kogitoTextDocumentService) {
        this.textDocumentServiceHub = textDocumentServiceHub;
        this.workspaceServiceHub = workspaceServiceHub;
        this.initializationParamsStore = initializationParamsStore;
        this.activatorChecker = activatorChecker;
        this.kogitoTextDocumentService = kogitoTextDocumentService;
    }

    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams params) {

        this.initializationParamsStore.setInitializeParams(params);
//        this.activatorChecker.check();

        InitializeResult results = new InitializeResult();

        if (true) {
            logger.info("Initialization Params: {}", MarshallerUtil.toJson(params));

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
        } else {
            logger.error("Activator Class not found. Can't use Language Server Capabilities");
            ServerCapabilities serverCapabilities = new ServerCapabilities();
            CompletionOptions completionOptions = new CompletionOptions();
            completionOptions.setResolveProvider(false);
            serverCapabilities.setCompletionProvider(completionOptions);

            serverCapabilities.setCodeActionProvider(false);
            serverCapabilities.setCallHierarchyProvider(false);
            TextDocumentSyncOptions textDocumentSyncOptions = new TextDocumentSyncOptions();
            SaveOptions saveOptions = new SaveOptions();
            saveOptions.setIncludeText(false);
            textDocumentSyncOptions.setOpenClose(false);
            textDocumentSyncOptions.setWillSave(false);
            textDocumentSyncOptions.setWillSaveWaitUntil(false);
            textDocumentSyncOptions.setChange(TextDocumentSyncKind.None);
            serverCapabilities.setTextDocumentSync(textDocumentSyncOptions);
        }
        return CompletableFuture.supplyAsync(() -> results);
    }

    @Override
    public void initialized(InitializedParams params) {
//        logger.info("Initialized Params: {}", MarshallerUtil.toJson(params));
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

    @Override
    public KogitoTextDocumentService getKogitoTextDocumentService() {
        return this.kogitoTextDocumentService;
    }
}