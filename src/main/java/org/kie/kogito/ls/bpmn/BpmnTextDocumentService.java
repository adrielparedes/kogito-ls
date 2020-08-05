package org.kie.kogito.ls.bpmn;

import java.util.ArrayList;
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
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.Position;
import org.eclipse.lsp4j.TextDocumentContentChangeEvent;
import org.eclipse.lsp4j.TextDocumentIdentifier;
import org.eclipse.lsp4j.VersionedTextDocumentIdentifier;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.kie.kogito.ls.java.JavaTextDocumentService;
import org.kie.kogito.ls.util.MarshallerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class BpmnTextDocumentService implements TextDocumentService {

    private Logger logger = LoggerFactory.getLogger(BpmnTextDocumentService.class);
    private JavaTextDocumentService javaTextDocumentService;

    @Inject
    public BpmnTextDocumentService(JavaTextDocumentService javaTextDocumentService) {

        this.javaTextDocumentService = javaTextDocumentService;
    }

    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {

        String uri = "file:///Users/aparedes/data/dev/workspace/leandroberetta/right-lyrics/albums-service/src/main/java/io/veicot/rightlyrics/model/Album.java";

        Position pos = new Position();
        pos.setLine(1);
        pos.setCharacter(7 + position.getPosition().getCharacter());
        CompletionParams completionParams = new CompletionParams();
        TextDocumentIdentifier id = new TextDocumentIdentifier();
        id.setUri(uri);

        CompletionContext context = new CompletionContext();
        context.setTriggerKind(CompletionTriggerKind.Invoked);
        completionParams.setTextDocument(id);
        completionParams.setPosition(pos);
        completionParams.setContext(context);

        logger.info("Complete: {}", MarshallerUtil.toJson(pos));

        return this.javaTextDocumentService.completion(completionParams);
    }

    @Override
    public void didOpen(DidOpenTextDocumentParams params) {

    }

    @Override
    public void didChange(DidChangeTextDocumentParams params) {
        String text = "package io.veicot.rightlyrics.model;\n" +
                "import " + params.getContentChanges().get(0).getText() +
                "\npublic class Album {}";

        String uri = "file:///Users/aparedes/data/dev/workspace/leandroberetta/right-lyrics/albums-service/src/main/java/io/veicot/rightlyrics/model/Album.java";
        DidChangeTextDocumentParams paramsMock = new DidChangeTextDocumentParams();
        VersionedTextDocumentIdentifier textDocument = new VersionedTextDocumentIdentifier();
        textDocument.setUri(uri);
        paramsMock.setTextDocument(textDocument);
        List<TextDocumentContentChangeEvent> contentChanges = new ArrayList<>();
        TextDocumentContentChangeEvent changeEvent = new TextDocumentContentChangeEvent();
        changeEvent.setText(text);
        contentChanges.add(changeEvent);
        paramsMock.setContentChanges(contentChanges);

        this.javaTextDocumentService.didChange(paramsMock);
    }

    @Override
    public void didClose(DidCloseTextDocumentParams params) {

    }

    @Override
    public void didSave(DidSaveTextDocumentParams params) {

    }
}
