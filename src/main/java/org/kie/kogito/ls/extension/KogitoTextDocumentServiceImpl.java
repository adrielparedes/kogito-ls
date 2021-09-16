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

package org.kie.kogito.ls.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.lsp4j.CompletionContext;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.CompletionTriggerKind;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.TextDocumentItem;
import org.eclipse.lsp4j.VersionedTextDocumentIdentifier;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.kie.kogito.ls.InitializationParamsStore;
import org.kie.kogito.ls.engine.ActivatorChecker;
import org.kie.kogito.ls.engine.BuildInformation;
import org.kie.kogito.ls.engine.JavaEngine;
import org.kie.kogito.ls.java.Java;
import org.kie.kogito.ls.util.MarshallerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.stream.Collectors.toList;

@ApplicationScoped
public class KogitoTextDocumentServiceImpl implements KogitoTextDocumentService {

    private final Logger logger = LoggerFactory.getLogger(KogitoTextDocumentServiceImpl.class);
    private final TextDocumentService textDocumentService;
    private final ActivatorChecker activatorChecker;
    private final JavaEngine javaEngine;
    private final InitializationParamsStore initializationParamsStore;

    @Inject
    public KogitoTextDocumentServiceImpl(@Java TextDocumentService textDocumentService,
                                         ActivatorChecker activatorChecker, JavaEngine javaEngine,
                                         InitializationParamsStore initializationParamsStore) {
        this.textDocumentService = textDocumentService;
        this.activatorChecker = activatorChecker;
        this.javaEngine = javaEngine;
        this.initializationParamsStore = initializationParamsStore;
    }

    @Override
    public CompletableFuture<List<LanguageServiceResult>> getClasses(LanguageServiceRequest request) {
//        String uri = this.activatorChecker.getActivatorUri();

        String uri = "file://" + initializationParamsStore.getInitializeParams().getRootPath() + "/src/main/java/org/adriel/Sarasa.java";
        BuildInformation buildInformation = javaEngine.buildImportClass(uri, request.getCompleteText());

        {
            DidOpenTextDocumentParams didOpenTextDocumentParams = new DidOpenTextDocumentParams();
            TextDocumentItem textDocumentItem = new TextDocumentItem();
            textDocumentItem.setLanguageId("java");
            textDocumentItem.setText(buildInformation.getOriginalText());
            textDocumentItem.setUri(buildInformation.getUri());
            textDocumentItem.setVersion(1);
            didOpenTextDocumentParams.setTextDocument(textDocumentItem);
            this.textDocumentService.didOpen(didOpenTextDocumentParams);
        }

        {
            DidChangeTextDocumentParams didChangeTextDocumentParams = new DidChangeTextDocumentParams();
            TextDocumentContentChangeEvent textDocumentContentChangeEvent = new TextDocumentContentChangeEvent();
            textDocumentContentChangeEvent.setText(buildInformation.getText());
            VersionedTextDocumentIdentifier versionedTextDocumentIdentifier = new VersionedTextDocumentIdentifier();
            versionedTextDocumentIdentifier.setUri(buildInformation.getUri());
            versionedTextDocumentIdentifier.setVersion(2);
            didChangeTextDocumentParams.setTextDocument(versionedTextDocumentIdentifier);
            didChangeTextDocumentParams.setContentChanges(Collections.singletonList(textDocumentContentChangeEvent));
            this.textDocumentService.didChange(didChangeTextDocumentParams);
        }

        CompletionContext context = new CompletionContext();
        context.setTriggerKind(CompletionTriggerKind.Invoked);

        Position pos = new Position();
        pos.setLine(buildInformation.getLine());
        pos.setCharacter(buildInformation.getPosition());

        TextDocumentIdentifier textDocumentIdentifier = new TextDocumentIdentifier();
        textDocumentIdentifier.setUri(uri);

        CompletionParams completionParams = new CompletionParams();
        completionParams.setTextDocument(textDocumentIdentifier);
        completionParams.setPosition(pos);
        completionParams.setContext(context);

        CompletableFuture<Either<List<CompletionItem>, CompletionList>> javaCompletion = this.textDocumentService
                .completion(completionParams);

        logger.info(">>>>>>>>>>>> Complete pos: {}", MarshallerUtil.toJson(pos));
        logger.info(">>>>>>>>>>>> Complete build info: {}", MarshallerUtil.toJson(buildInformation));

        try {
            Either<List<CompletionItem>, CompletionList> completed = javaCompletion.get();
            logger.info(">>>>>>>>>>>> Complete completed: {}", MarshallerUtil.toJson(completed));
            List<CompletionItem> items = new ArrayList<>();
            if (completed.isLeft()) {
                items = completed.getLeft();
            } else if (completed.isRight()) {
                items = completed.getRight().getItems();
            }

            List<LanguageServiceResult> languageServiceResults = items.stream()
                    .filter(item -> item.getLabel().contains("-"))
                    .map(item -> new LanguageServiceResult(getFQCN(item.getLabel())))
                    .collect(toList());
            return CompletableFuture.supplyAsync(() -> languageServiceResults);
        } catch (Exception e) {
            logger.error("Error trying to get information from Java LSP", e);
            return CompletableFuture.supplyAsync(Collections::emptyList);
        }
    }

    public String getFQCN(String label) {
        String[] split = label.split("-");
        return split[1].trim() + "." + split[0].trim();
    }

    @Override
    public CompletableFuture<List<LanguageServiceResult>> getMethods(LanguageServiceRequest request) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<List<LanguageServiceResult>> getAttributes(LanguageServiceRequest request) {
        throw new UnsupportedOperationException();
    }
}
