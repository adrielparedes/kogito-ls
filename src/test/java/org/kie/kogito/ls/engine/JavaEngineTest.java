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

import javax.inject.Inject;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kie.kogito.ls.engine.templates.Templates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
class JavaEngineTest {

    @Inject
    private Templates templates;

    private final Logger logger = LoggerFactory.getLogger(JavaEngineTest.class);
    private JavaEngine javaEngine;
    private String uri = "file:///aaa/src/main/java/pkg/MainClass.java";

    @BeforeEach
    public void setUp() {
        this.javaEngine = new JavaEngine(templates);
    }

    @Test
    public void testGetClassName() {
        String result = this.javaEngine.getClassName(uri);
        assertThat(result).isEqualTo("MainClass");
    }

    @Test
    public void testImportPosition() {
        BuildInformation info = this.javaEngine.buildImportClass(uri, "java.util.");
        int position = info.getPosition();
        assertThat(position).isEqualTo(17);
    }

    @Test
    public void testEmptyImportPosition() {
        BuildInformation info = this.javaEngine.buildImportClass(uri, "");
        int position = info.getPosition();
        assertThat(position).isEqualTo(7);
    }

    @Test
    public void testPublicMethodPosition() {
        BuildInformation info = this.javaEngine.buildPublicContent(uri, "org.kie.MyClass", "get");
        int position = info.getPosition();
        assertThat(position).isEqualTo(27);
    }

    @Test
    public void testEmptyPublicMethodPosition() {
        BuildInformation info = this.javaEngine.buildPublicContent(uri, "org.kie.MyClass", "");
        int position = info.getPosition();
        assertThat(position).isEqualTo(24);
    }
}