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

public class JavaClass {

    private Path path;

    public JavaClass(Path path) {
        this.path = path;
    }

    public String getPackage() {
        String fileName = this.path.getFileName().toString();
        String fullPath = this.path.toString();
        String pack = fullPath.substring(fullPath.indexOf(Project.SRC_MAIN_JAVA) + Project.SRC_MAIN_JAVA.length(), fullPath.indexOf(fileName));

        if (pack.endsWith("/")) {
            pack = pack.substring(0, pack.length() - 1);
        }
        if (pack.startsWith("/")) {
            pack = pack.substring(1);
        }

        pack = pack.replaceAll("/", ".");
        return pack;
    }

    public String getFileName() {
        return this.path.getFileName().toString();
    }
}
