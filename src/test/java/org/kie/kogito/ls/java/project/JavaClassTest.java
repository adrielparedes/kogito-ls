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

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

class JavaClassTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaClassTest.class);
    private static Path javaFilePath;

    @BeforeAll
    public static void beforeClass() {
        javaFilePath = Paths.get("src", "test", "resources", "testProject", "src", "main", "java", "org", "kogito", "test", "project", "RandomClass.java");
    }

    @Test
    public void testGetPackage() {
        JavaClass javaClass = new JavaClass(javaFilePath);
        String pack = javaClass.getPackage();
        assertThat(pack).isEqualTo("org.kogito.test.project");
    }
}