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

package org.kie.kogito.ls.service;

import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4j.services.WorkspaceService;

public abstract class Service {

    protected TextDocumentService textDocumentService;
    protected WorkspaceService workspaceService;

    public Service() {
    }

    public Service(TextDocumentService textDocumentService, WorkspaceService workspaceService) {

        this.textDocumentService = textDocumentService;
        this.workspaceService = workspaceService;
    }

    public abstract boolean isAcceptable(String uri);

    public TextDocumentService getTextDocumentService() {
        return this.textDocumentService;
    }

    public WorkspaceService getWorkspaceService() {
        return this.workspaceService;
    }

    /**
     * The higher the number the higher the priority
     *
     * @return priority number
     */
    public int getPriority() {
        return 0;
    }

    @Override
    public String toString() {
        return "Service{" +
                this.getClass().getSimpleName() +
                '}';
    }
}
