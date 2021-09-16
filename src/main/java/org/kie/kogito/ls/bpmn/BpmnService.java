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

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.kie.kogito.ls.service.Service;

@ApplicationScoped
public class BpmnService extends Service {

    List<String> allowedUri = Arrays.asList(".bpmn", ".bpmn2");

    @Inject
    public BpmnService(BpmnTextDocumentService textDocumentService, BpmnWorkspaceService workspaceService) {
        super(textDocumentService, workspaceService);
    }

    @Override
    public boolean isAcceptable(String uri) {
        return allowedUri.stream().anyMatch(uri::endsWith);
    }
}
