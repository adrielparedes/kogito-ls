package org.kie.kogito.ls.log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionKind;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionItemKind;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class LogTextDocumentService implements TextDocumentService {

    private Logger logger = LoggerFactory.getLogger(LogTextDocumentService.class);

    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {
        List<CompletionItem> completionItems = new ArrayList<>();

        CompletionItem completionItem = new CompletionItem();
        completionItem.setDetail("Detail");
        completionItem.setLabel("label");
        completionItem.setSortText("2");

        CompletionItem completionItem2 = new CompletionItem();
        completionItem2.setDetail("xxxxx");
        completionItem2.setLabel("The XML Text");
        completionItem2.setKind(CompletionItemKind.Method);
        completionItem2.setSortText("1");
        completionItem2.setInsertText("<?xml version=\"1.0\" encoding=\"UTF-8\"?>  \n" +
                                         "<emails>  \n" +
                                         "<email>  \n" +
                                         "  <to>Vimal</to>  \n" +
                                         "  <from>Sonoo</from>  \n" +
                                         "  <heading>Hello</heading>  \n" +
                                         "  <body>Hello brother, how are you!</body>  \n" +
                                         "</email>  \n" +
                                         "<email>  \n" +
                                         "  <to>Peter</to>  \n" +
                                         "  <from>Jack</from>  \n" +
                                         "  <heading>Birth day wish</heading>  \n" +
                                         "  <body>Happy birth day Tom!</body>  \n" +
                                         "</email>  \n" +
                                         "<email>  \n" +
                                         "  <to>James</to>  \n" +
                                         "  <from>Jaclin</from>  \n" +
                                         "  <heading>Morning walk</heading>  \n" +
                                         "  <body>Please start morning walk to stay fit!</body>  \n" +
                                         "</email>  \n" +
                                         "<email>  \n" +
                                         "  <to>Kartik</to>  \n" +
                                         "  <from>Kumar</from>  \n" +
                                         "  <heading>Health Tips</heading>  \n" +
                                         "  <body>Smoking is injurious to health!</body>  \n" +
                                         "</email>  \n" +
                                         "</emails>  ");
        completionItem2.setDocumentation("<?xml version=\"1.0\" encoding=\"UTF-8\"?>  \n" +
                                              "<emails>  \n" +
                                              "<email>  \n" +
                                              "  <to>Vimal</to>  \n" +
                                              "  <from>Sonoo</from>  \n" +
                                              "  <heading>Hello</heading>  \n" +
                                              "  <body>Hello brother, how are you!</body>  \n" +
                                              "</email>  \n" +
                                              "<email>  \n" +
                                              "  <to>Peter</to>  \n" +
                                              "  <from>Jack</from>  \n" +
                                              "  <heading>Birth day wish</heading>  \n" +
                                              "  <body>Happy birth day Tom!</body>  \n" +
                                              "</email>  \n" +
                                              "<email>  \n" +
                                              "  <to>James</to>  \n" +
                                              "  <from>Jaclin</from>  \n" +
                                              "  <heading>Morning walk</heading>  \n" +
                                              "  <body>Please start morning walk to stay fit!</body>  \n" +
                                              "</email>  \n" +
                                              "<email>  \n" +
                                              "  <to>Kartik</to>  \n" +
                                              "  <from>Kumar</from>  \n" +
                                              "  <heading>Health Tips</heading>  \n" +
                                              "  <body>Smoking is injurious to health!</body>  \n" +
                                              "</email>  \n" +
                                              "</emails>  ");

        completionItems.add(completionItem);
        completionItems.add(completionItem2);
        return CompletableFuture.supplyAsync(() -> Either.forLeft(completionItems));
    }

    @Override
    public CompletableFuture<List<Either<Command, CodeAction>>> codeAction(CodeActionParams params) {
        List<Either<Command, CodeAction>> actions = new ArrayList<>();

        CodeAction codeAction = new CodeAction();
        codeAction.setKind(CodeActionKind.Refactor);
        codeAction.setTitle("Refactor");

        actions.add(Either.forRight(codeAction));

        return CompletableFuture.supplyAsync(() -> actions);
    }

    @Override
    public void didOpen(DidOpenTextDocumentParams params) {
        logger.info("{}", params);
    }

    @Override
    public void didChange(DidChangeTextDocumentParams params) {
        logger.info("{}", params);
    }

    @Override
    public void didClose(DidCloseTextDocumentParams params) {
        logger.info("{}", params);
    }

    @Override
    public void didSave(DidSaveTextDocumentParams params) {
        logger.info("{}", params);
    }
}
