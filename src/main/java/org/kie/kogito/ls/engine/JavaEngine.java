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
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.qute.TemplateInstance;
import org.kie.kogito.ls.engine.templates.TemplateParameters;
import org.kie.kogito.ls.engine.templates.Templates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class JavaEngine {

    private Logger logger = LoggerFactory.getLogger(JavaEngine.class);
    private Templates templates;

    @Inject
    public JavaEngine(Templates templates) {

        this.templates = templates;
    }

    public BuildInformation buildImportClass(String uri, String importText) {
        TemplateParameters item = new TemplateParameters();
        item.className = getClassName(uri);
        item.completeText = importText;
        TemplateInstance instance = this.templates.getTemplate(Templates.TEMPLATE_CLASS).data("item", item);
        String content = instance.render();
        return new BuildInformation(uri, getContent(uri), content, 3, getEndOfLinePosition(content, 3));
    }

    public BuildInformation buildPublicContent(String uri, String fqdn, String completeText) {
        TemplateParameters item = new TemplateParameters();
        item.className = getClassName(uri);
        item.fqdn = fqdn;
        item.completeText = completeText;
        TemplateInstance instance = this.templates.getTemplate(Templates.TEMPLATE_PUBLIC).data("item", item);
        String content = instance.render();
        return new BuildInformation(uri, getContent(uri), content, 8, getEndOfLinePosition(content, 8));
    }

    protected int getEndOfLinePosition(String content, int lineNumber) {
        String[] split = content.split("\n");
        logger.info(split[lineNumber]);
        return split[lineNumber].length();
    }

    protected String getClassName(String url) {
        String fileName = Paths.get(url).getFileName().toString();
        if (fileName.contains(".")) {
            return fileName.substring(0, fileName.lastIndexOf("."));
        } else {
            return fileName;
        }
    }

    protected String getContent(String uri) {
        try {
            return Files.readString(Path.of(URI.create(uri)));
        } catch (IOException e) {
            logger.error("Can't read content from: " + uri, e);
            return "";
        }
    }
}
