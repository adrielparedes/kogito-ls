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

package org.kie.kogito.ls.engine.templates;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.quarkus.qute.Engine;
import io.quarkus.qute.Template;

@ApplicationScoped
public class Templates {

    private Engine engine;
    public static String TEMPLATE_CLASS = "templateClass.java";
    public static String TEMPLATE_PUBLIC = "templatePublic.java";

    @Inject
    public Templates(Engine engine) {

        this.engine = engine;
    }

    public Template getTemplate(String templateName) {
        return this.engine.getTemplate(templateName);
    }
}