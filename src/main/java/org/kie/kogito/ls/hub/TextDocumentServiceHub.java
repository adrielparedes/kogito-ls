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

import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.CodeLensParams;
import org.eclipse.lsp4j.ColorInformation;
import org.eclipse.lsp4j.ColorPresentation;
import org.eclipse.lsp4j.ColorPresentationParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.DeclarationParams;
import org.eclipse.lsp4j.DefinitionParams;
import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.DocumentColorParams;
import org.eclipse.lsp4j.DocumentFormattingParams;
import org.eclipse.lsp4j.DocumentHighlight;
import org.eclipse.lsp4j.DocumentHighlightParams;
import org.eclipse.lsp4j.DocumentLink;
import org.eclipse.lsp4j.DocumentLinkParams;
import org.eclipse.lsp4j.DocumentOnTypeFormattingParams;
import org.eclipse.lsp4j.DocumentRangeFormattingParams;
import org.eclipse.lsp4j.DocumentSymbol;
import org.eclipse.lsp4j.DocumentSymbolParams;
import org.eclipse.lsp4j.FoldingRange;
import org.eclipse.lsp4j.FoldingRangeRequestParams;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.HoverParams;
import org.eclipse.lsp4j.ImplementationParams;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.PrepareRenameParams;
import org.eclipse.lsp4j.PrepareRenameResult;
import org.eclipse.lsp4j.Range;
import org.eclipse.lsp4j.ReferenceParams;
import org.eclipse.lsp4j.RenameParams;
import org.eclipse.lsp4j.SelectionRange;
import org.eclipse.lsp4j.SelectionRangeParams;
import org.eclipse.lsp4j.SignatureHelp;
import org.eclipse.lsp4j.SignatureHelpParams;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.TypeDefinitionParams;
import org.eclipse.lsp4j.WillSaveTextDocumentParams;
import org.eclipse.lsp4j.WorkspaceEdit;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.kie.kogito.ls.service.Service;
import org.kie.kogito.ls.util.MarshallerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class TextDocumentServiceHub extends ServiceHub implements TextDocumentService {

    private final Logger logger = LoggerFactory.getLogger(TextDocumentServiceHub.class);

    @Inject
    public TextDocumentServiceHub(Instance<Service> services) {
        super(services);
    }

    @Override
    public CompletableFuture<WorkspaceEdit> rename(RenameParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().rename(params);
    }

    @Override
    public CompletableFuture<List<Either<Command, CodeAction>>> codeAction(CodeActionParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().codeAction(params);
    }

    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {
        if (logger.isDebugEnabled()) {
            logger.debug("{}", MarshallerUtil.toJson(position));
        }
        return getService(position.getTextDocument().getUri()).getTextDocumentService().completion(position);
    }

    @Override
    public void didOpen(DidOpenTextDocumentParams params) {
        logger.info("{}", MarshallerUtil.toJson(params));
        getService(params.getTextDocument().getUri()).getTextDocumentService().didOpen(params);
    }

    @Override
    public void didChange(DidChangeTextDocumentParams params) {
        logger.info("{}", MarshallerUtil.toJson(params));
        getService(params.getTextDocument().getUri()).getTextDocumentService().didChange(params);
    }

    @Override
    public void didClose(DidCloseTextDocumentParams params) {
        logger.info("{}", MarshallerUtil.toJson(params));
        getService(params.getTextDocument().getUri()).getTextDocumentService().didClose(params);
    }

    @Override
    public void didSave(DidSaveTextDocumentParams params) {
        getService(params.getTextDocument().getUri()).getTextDocumentService().didSave(params);
    }

    @Override
    public CompletableFuture<CodeLens> resolveCodeLens(CodeLens unresolved) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<CompletionItem> resolveCompletionItem(CompletionItem unresolved) {
        logger.info("{}", MarshallerUtil.toJson(unresolved));
        return null;
    }

    @Override
    public CompletableFuture<Hover> hover(HoverParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().hover(params);
    }

    @Override
    public CompletableFuture<SignatureHelp> signatureHelp(SignatureHelpParams params) {
        logger.info("{}", MarshallerUtil.toJson(params));
        return getService(params.getTextDocument().getUri()).getTextDocumentService().signatureHelp(params);
    }

    @Override
    public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> declaration
            (DeclarationParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().declaration(params);
    }

    @Override
    public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> definition
            (DefinitionParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().definition(params);
    }

    @Override
    public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> typeDefinition
            (TypeDefinitionParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().typeDefinition(params);
    }

    @Override
    public CompletableFuture<Either<List<? extends Location>, List<? extends LocationLink>>> implementation
            (ImplementationParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().implementation(params);
    }

    @Override
    public CompletableFuture<List<? extends Location>> references(ReferenceParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().references(params);
    }

    @Override
    public CompletableFuture<List<? extends DocumentHighlight>> documentHighlight(DocumentHighlightParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().documentHighlight(params);
    }

    @Override
    public CompletableFuture<List<Either<SymbolInformation, DocumentSymbol>>> documentSymbol(DocumentSymbolParams
                                                                                                     params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().documentSymbol(params);
    }

    @Override
    public CompletableFuture<List<? extends CodeLens>> codeLens(CodeLensParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().codeLens(params);
    }

    @Override
    public CompletableFuture<List<? extends TextEdit>> formatting(DocumentFormattingParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().formatting(params);
    }

    @Override
    public CompletableFuture<List<? extends TextEdit>> rangeFormatting(DocumentRangeFormattingParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().rangeFormatting(params);
    }

    @Override
    public CompletableFuture<List<? extends TextEdit>> onTypeFormatting(DocumentOnTypeFormattingParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().onTypeFormatting(params);
    }

    @Override
    public void willSave(WillSaveTextDocumentParams params) {
        getService(params.getTextDocument().getUri()).getTextDocumentService().willSave(params);
    }

    @Override
    public CompletableFuture<List<TextEdit>> willSaveWaitUntil(WillSaveTextDocumentParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().willSaveWaitUntil(params);
    }

    @Override
    public CompletableFuture<List<DocumentLink>> documentLink(DocumentLinkParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().documentLink(params);
    }

    @Override
    public CompletableFuture<DocumentLink> documentLinkResolve(DocumentLink params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<List<ColorInformation>> documentColor(DocumentColorParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().documentColor(params);
    }

    @Override
    public CompletableFuture<List<ColorPresentation>> colorPresentation(ColorPresentationParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().colorPresentation(params);
    }

    @Override
    public CompletableFuture<List<FoldingRange>> foldingRange(FoldingRangeRequestParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().foldingRange(params);
    }

    @Override
    public CompletableFuture<Either<Range, PrepareRenameResult>> prepareRename(PrepareRenameParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().prepareRename(params);
    }

    @Override
    public CompletableFuture<List<SelectionRange>> selectionRange(SelectionRangeParams params) {
        return getService(params.getTextDocument().getUri()).getTextDocumentService().selectionRange(params);
    }
}