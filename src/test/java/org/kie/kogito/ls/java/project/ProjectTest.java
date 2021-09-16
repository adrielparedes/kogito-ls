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

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectTest.class);
    private static URI URI;

    private Project project;

    @BeforeAll
    public static void beforeClass() {
        Path projectDirectory = Paths.get("src", "test", "resources", "testProject");
        String absolutePath = projectDirectory.toFile().getAbsolutePath();
        URI = java.net.URI.create("file://" + absolutePath);
        LOGGER.info(URI.toString());
    }

    @BeforeEach
    public void setUp() {
        this.project = new Project();
        this.project.initialize(URI);
    }

    @Test
    public void testUriValid() {
        {
            boolean valid = this.project.validateUri(java.net.URI.create("/a/random/uri"));
            assertThat(valid).isFalse();
        }
        {
            boolean valid = this.project.validateUri(java.net.URI.create("http://a/random/uri"));
            assertThat(valid).isFalse();
        }
        {
            boolean valid = this.project.validateUri(java.net.URI.create("file:///a/random/uri"));
            assertThat(valid).isTrue();
        }
    }

    @Test
    public void testValidateMavenProject() {
        {
            boolean valid = this.project.validateMavenProject(URI);
            assertThat(valid).isTrue();
        }
        {
            boolean valid = this.project.validateMavenProject(java.net.URI.create(URI + "/src"));
            assertThat(valid).isFalse();
        }
    }

    @Test
    public void testFindJavaFile() {
        Optional<JavaClass> javaFileUri = this.project.getAnyJavaFile(URI);
        assertThat(javaFileUri).isNotEmpty();
        assertThat(javaFileUri.get().getFileName()).isEqualTo("Activator.java");
    }

    @Test
    public void testCannotFindJavaFile() {
        Optional<JavaClass> javaFileUri = this.project.getAnyJavaFile(Paths.get(URI.getPath() + "/src/main/java/org/kogito/test/project/empty").toUri());
        assertThat(javaFileUri).isEmpty();
    }
}