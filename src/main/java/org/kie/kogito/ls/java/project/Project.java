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

package org.kie.kogito.ls.java.project;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.kie.kogito.ls.logger.LoggerUtil.debug;

@ApplicationScoped
public class Project {

    public static final int MAX_DEPTH = 30;
    public static final String SRC_MAIN_JAVA = "src/main/java";

    private final List<String> validSchemes = Collections.singletonList("file");
    private final Logger logger = LoggerFactory.getLogger(Project.class);

    private URI uri;

    public void initialize(URI uri) {
        boolean valid = this.validateProject(uri);
        if (valid) {
            this.uri = uri;
        } else {
            throw new InvalidProjectException("The Project in the given URI is not valid: " + uri.toString());
        }
    }

    public void initialize(String uri) {
        this.initialize(URI.create(uri));
    }

    protected boolean validateProject(URI uri) {
        return this.validateUri(uri) && this.validateMavenProject(uri);
    }

    protected boolean validateMavenProject(URI uri) {
        String uriWithoutSchema = uri.getPath();
        Path path = Paths.get(uriWithoutSchema, "pom.xml");
        return Files.exists(path);
    }

    protected boolean validateUri(URI uri) {
        return this.validateUri(this.validSchemes, uri);
    }

    protected boolean validateUri(List<String> schemes, URI uri) {
        boolean valid = schemes.stream().anyMatch(scheme -> scheme.equals(uri.getScheme()));
        debug(logger, l -> l.debug("Is URI <{}> valid: {}", uri.toString(), valid));
        if (!valid) {
            debug(logger, l -> l.debug("Valid Schemes are {}", schemes));
        }
        return valid;
    }

    public Optional<JavaClass> getAnyJavaFile(URI baseUri) {
        try {
            debug(logger, l -> l.debug("Searching for Java classes in {}", baseUri));
            Stream<Path> found = Files.find(Paths.get(baseUri), MAX_DEPTH, (path, attrs) -> path.toString().endsWith(".java"));
            return found.findAny().map(JavaClass::new);
        } catch (IOException e) {
            logger.error("There are not Java Files in this project", e);
        }
        return Optional.empty();
    }
}
