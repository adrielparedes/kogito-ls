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

package org.kie.kogito.ls.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.ls.util.ClosableUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class SocketFactory {

    public static final String EDITOR = "Editor";
    public static final String JDT_LS = "JDT.ls";
    private Logger logger = LoggerFactory.getLogger(SocketFactory.class);
    private ServerSocket editorSocket;
    private ServerSocket jdtSocket;

    private SocketConfiguration socketConfiguration;

    @Inject
    public SocketFactory(SocketConfiguration socketConfiguration) {

        this.socketConfiguration = socketConfiguration;
    }

    @PostConstruct
    public void initialize() {
        this.editorSocket = createServerSocket(EDITOR, socketConfiguration.editorPort());
        this.jdtSocket = createServerSocket(JDT_LS, socketConfiguration.jdtPort());
    }

    public Socket acceptEditorSocket() {
        return this.accept(EDITOR, this.editorSocket);
    }

    public Socket acceptJDTSocket() {
        return this.accept(JDT_LS, this.jdtSocket);
    }

    private ServerSocket createServerSocket(String description, int port) {
        try {
            logger.info("Creating Server Socket for {} on port {}", description, port);
            return new ServerSocket(port);
        } catch (IOException e) {
            throw new ServerSocketException("Error trying to create Socket on port: " + port, e);
        }
    }

    private Socket accept(String description, ServerSocket serverSocket) {
        try {
            return serverSocket.accept();
        } catch (IOException e) {
            throw new ServerSocketException("Can't accept " + description + " socket", e);
        }
    }

    @PreDestroy
    public void destroy() {
        ClosableUtil.close(this.editorSocket);
        ClosableUtil.close(this.jdtSocket);
    }
}
