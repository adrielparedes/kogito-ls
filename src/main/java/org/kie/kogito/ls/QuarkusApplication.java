package org.kie.kogito.ls;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.inject.Inject;

import io.quarkus.runtime.annotations.QuarkusMain;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.kie.kogito.ls.util.ClosableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusMain
public class QuarkusApplication implements io.quarkus.runtime.QuarkusApplication {

    private Logger logger = LoggerFactory.getLogger(QuarkusApplication.class);

    private KogitoLanguageServer kogitoLanguageServer;

    @Inject
    public QuarkusApplication(KogitoLanguageServer kogitoLanguageServer) {
        this.kogitoLanguageServer = kogitoLanguageServer;
    }

    @Override
    public int run(String... args) {
        ServerSocket serverSocket = null;
        Socket socket = null;
        try {
            serverSocket = new ServerSocket(8100);

            while (true) {
                socket = serverSocket.accept();
                logger.info("Starting Kogito Language Server");
                Launcher<LanguageClient> server = LSPLauncher.createServerLauncher(kogitoLanguageServer, socket.getInputStream(), socket.getOutputStream());
                logger.info("Server Started. Listening...");
                server.startListening().get();
            }
        } catch (Exception e) {
            logger.error("Error", e);
            return 1;
        } finally {
            ClosableUtil.close(socket);
            ClosableUtil.close(serverSocket);
        }
    }
}
