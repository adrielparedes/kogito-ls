package org.kie.kogito.ls.java;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageServer;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;
import org.kie.kogito.ls.InitializationParamsStore;
import org.kie.kogito.ls.noop.NoopTextDocumentService;
import org.kie.kogito.ls.noop.NoopWorkspaceService;
import org.kie.kogito.ls.util.ClosableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class JavaLanguageServerClientProducer {

    private Logger logger = LoggerFactory.getLogger(JavaLanguageServerClientProducer.class);

    private TextDocumentService textDocumentService;
    private WorkspaceService workspaceService;
    private InitializationParamsStore initializationParamsStore;
    private JavaLanguageServerClient javaLanguageServerClient;
    private LanguageServer proxy;

    @Inject
    public JavaLanguageServerClientProducer(InitializationParamsStore initializationParamsStore, JavaLanguageServerClient javaLanguageServerClient) {

        this.initializationParamsStore = initializationParamsStore;
        this.javaLanguageServerClient = javaLanguageServerClient;
        this.initialize();
    }

    private void initialize() {
        Socket socket = null;
        Launcher<LanguageServer> client;
        try {
            socket = new Socket("127.0.0.1", 5036);
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();
            client = LSPLauncher.createClientLauncher(javaLanguageServerClient, in, out, true, new PrintWriter(System.out));
            client.startListening();
            proxy = client.getRemoteProxy();
            proxy.initialize(initializationParamsStore.getInitializeParams());
            workspaceService = proxy.getWorkspaceService();
            textDocumentService = proxy.getTextDocumentService();
            logger.info("JDT Proxy successfully created");
        } catch (Exception e) {
            logger.info("Error trying to create proxy to Java JDT Server", e);
            ClosableUtil.close(socket);
            textDocumentService = null;
            workspaceService = null;
        }
    }

    @Produces
    @Java
    public TextDocumentService produceTextDocumentService() {
        if (textDocumentService == null) {
            this.initialize();
        }
        if (textDocumentService == null) {
            logger.info("Returning NoopTextDocumentService");
            return new NoopTextDocumentService();
        } else {
            return this.textDocumentService;
        }
    }

    @Produces
    @Java
    public WorkspaceService produceWorkspaceService() {
        if (textDocumentService == null) {
            this.initialize();
        }
        if (textDocumentService == null) {
            logger.info("Returning NoopWorkspaceService");
            return new NoopWorkspaceService();
        } else {
            return this.workspaceService;
        }
    }
}
