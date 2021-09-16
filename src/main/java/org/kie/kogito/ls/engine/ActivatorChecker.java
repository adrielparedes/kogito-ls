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

package org.kie.kogito.ls.engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.kie.kogito.ls.InitializationParamsStore;

@ApplicationScoped
public class ActivatorChecker {

    private final Logger logger = Logger.getLogger(ActivatorChecker.class);
    private InitializationParamsStore initializationParamsStore;
    private String activatorUri = "";
    private boolean present = false;

    @Inject
    public ActivatorChecker(InitializationParamsStore initializationParamsStore) {

        this.initializationParamsStore = initializationParamsStore;
    }

    public void check() {
        ActivationFileVisitor visitor = new ActivationFileVisitor();
        try {
            Files.walkFileTree(Paths.get(getRootUri()), visitor);
        } catch (IOException e) {
            logger.error("Error trying to read workspace tree", e);
        }
        this.present = visitor.isPresent();
        if (this.present) {
            this.activatorUri = visitor.getActivatorFile().toAbsolutePath().toString();
        }
    }

    public boolean existActivator() {
        return this.present;
    }

    public String getActivatorUri() {
        if (StringUtils.isNotEmpty(this.activatorUri)) {
            return "file://" + this.activatorUri;
        } else {
            throw new ActivatorCheckerException("Activator URI is not present");
        }
    }

    private String getRootUri() {
        return this.initializationParamsStore.getInitializeParams().getRootPath();
    }
}
