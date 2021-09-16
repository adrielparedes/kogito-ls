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

package org.kie.kogito.ls.bpmn;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CompletionContext;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.CompletionTriggerKind;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.VersionedTextDocumentIdentifier;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.kie.kogito.ls.InitializationParamsStore;
import org.kie.kogito.ls.engine.ActivatorChecker;
import org.kie.kogito.ls.engine.BuildInformation;
import org.kie.kogito.ls.engine.JavaEngine;
import org.kie.kogito.ls.java.JavaTextDocumentService;
import org.kie.kogito.ls.util.MarshallerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class BpmnTextDocumentService implements TextDocumentService {

    private Logger logger = LoggerFactory.getLogger(BpmnTextDocumentService.class);
    private final InitializationParamsStore initializationParamsStore;
    private final JavaTextDocumentService javaTextDocumentService;
    private final JavaEngine javaEngine;
    private final ActivatorChecker activatorChecker;

    @Inject
    public BpmnTextDocumentService(InitializationParamsStore initializationParamsStore,
                                   JavaTextDocumentService javaTextDocumentService,
                                   JavaEngine javaEngine,
                                   ActivatorChecker activatorChecker) {
        this.initializationParamsStore = initializationParamsStore;
        this.javaTextDocumentService = javaTextDocumentService;
        this.javaEngine = javaEngine;
        this.activatorChecker = activatorChecker;
    }

    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {

        String uri = this.activatorChecker.getActivatorUri();
//
//        BuildInformation buildInformation = javaEngine.buildImportClass(uri, "");
//
//        logger.info("Position: {}", MarshallerUtil.toJson(position));
//        logger.info("buildInformation: {}", MarshallerUtil.toJson(buildInformation));
//
//        Position pos = new Position();
//        pos.setLine(buildInformation.getLine());
//        pos.setCharacter(buildInformation.getPosition() + position.getPosition().getCharacter());
//        TextDocumentIdentifier id = new TextDocumentIdentifier();
//        id.setUri(uri);
//
//        CompletionContext context = new CompletionContext();
//        context.setTriggerKind(CompletionTriggerKind.Invoked);
//
//        CompletionParams completionParams = new CompletionParams();
//        completionParams.setTextDocument(id);
//        completionParams.setPosition(pos);
//        completionParams.setContext(context);
        position.getTextDocument().setUri(uri);
        CompletableFuture<Either<List<CompletionItem>, CompletionList>> javaCompletion = this.javaTextDocumentService.completion(position);
        position.getPosition().setLine(3);
        position.getPosition().setCharacter(18);
        logger.info("Complete: {}", MarshallerUtil.toJson(position));
        logger.info("Complete: {}", MarshallerUtil.toJson(javaCompletion));
        return javaCompletion;
    }

    @Override
    public void didOpen(DidOpenTextDocumentParams params) {

    }

    @Override
    public CompletableFuture<List<Either<Command, CodeAction>>> codeAction(CodeActionParams params) {
        String uri = this.activatorChecker.getActivatorUri();
        params.getTextDocument().setUri(uri);
        return this.javaTextDocumentService.codeAction(params);
    }

    @Override
    public void didChange(DidChangeTextDocumentParams params) {
//        params.getContentChanges().get(0).getText();
//
        String uri = this.activatorChecker.getActivatorUri();
//
//        BuildInformation buildInformation = this.javaEngine.buildImportClass(uri, params.getContentChanges().get(0).getText());
//        logger.info("Position: {}", MarshallerUtil.toJson(params));
//        logger.info("buildInformation: {}", MarshallerUtil.toJson(buildInformation));
//
//        VersionedTextDocumentIdentifier textDocument = new VersionedTextDocumentIdentifier();
//        textDocument.setUri(uri);
//
//        TextDocumentContentChangeEvent changeEvent = new TextDocumentContentChangeEvent();
//        changeEvent.setText(buildInformation.getText());
//
//        List<TextDocumentContentChangeEvent> contentChanges = new ArrayList<>();
//        contentChanges.add(changeEvent);
//
//        DidChangeTextDocumentParams paramsMock = new DidChangeTextDocumentParams();
//        paramsMock.setTextDocument(textDocument);
//        paramsMock.setContentChanges(contentChanges);

        params.getTextDocument().setUri(uri);
        logger.info("Did Change: {}", MarshallerUtil.toJson(params));
        this.javaTextDocumentService.didChange(params);
    }

    @Override
    public void didClose(DidCloseTextDocumentParams params) {

    }

    @Override
    public void didSave(DidSaveTextDocumentParams params) {

    }
}
