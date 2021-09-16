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
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivationFileVisitor extends SimpleFileVisitor<Path> {

    private final Logger logger = LoggerFactory.getLogger(ActivationFileVisitor.class);
    protected static final String IMPORT_ACTIVATOR = "import org.kogito.KogitoActivator;";
    protected static final String ANNOTATION_ACTIVATOR = "@KogitoActivator";
    protected static final String JAVA_EXTENSION = ".java";

    private boolean present;
    private Path activatorPath;

    public ActivationFileVisitor() {
        this.present = false;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        String filePath = file.toString();
        if (!filePath.endsWith(JAVA_EXTENSION)) {
            return FileVisitResult.CONTINUE;
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Java file found: " + filePath);
        }

        long linesThatMatch = Files.lines(file).filter(this::containsActivator).count();

        if (linesThatMatch >= 2) {
            this.present = true;
            if (logger.isDebugEnabled()) {
                logger.debug("Activator found: " + filePath);
            }
            this.activatorPath = file;
            return FileVisitResult.TERMINATE;
        } else {
            return FileVisitResult.CONTINUE;
        }
    }

    protected boolean containsActivator(String line) {
        return line.contains(IMPORT_ACTIVATOR) || line.contains(ANNOTATION_ACTIVATOR);
    }

    public boolean isPresent() {
        return present;
    }

    public Path getActivatorFile() {
        return activatorPath;
    }
}
