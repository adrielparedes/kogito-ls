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

import java.io.PrintWriter;
import java.net.Socket;

import javax.inject.Inject;

import io.quarkus.runtime.annotations.QuarkusMain;
import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.launch.LSPLauncher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.kie.kogito.ls.socket.SocketFactory;
import org.kie.kogito.ls.util.ClosableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@QuarkusMain
public class QuarkusApplication implements io.quarkus.runtime.QuarkusApplication {

    private Logger logger = LoggerFactory.getLogger(QuarkusApplication.class);

    private KogitoLanguageServerImpl kogitoLanguageServer;
    private SocketFactory socketFactory;

    @Inject
    public QuarkusApplication(KogitoLanguageServerImpl kogitoLanguageServer, SocketFactory socketFactory) {
        this.kogitoLanguageServer = kogitoLanguageServer;
        this.socketFactory = socketFactory;
    }

    @Override
    public int run(String... args) {
        Socket socket = null;
        try {

            while (true) {
                socket = socketFactory.acceptEditorSocket();
                logger.info("Starting Kogito Language Server");

                Launcher<LanguageClient> server = LSPLauncher.createServerLauncher(kogitoLanguageServer,
                                                                                   socket.getInputStream(), socket.getOutputStream(),
                                                                                   true,
                                                                                   new PrintWriter(System.out));
                logger.info("Server Started. Listening...");
                server.startListening().get();
            }
        } catch (Exception e) {
            logger.error("Error", e);
            return 1;
        } finally {
            ClosableUtil.close(socket);
        }
    }
}
