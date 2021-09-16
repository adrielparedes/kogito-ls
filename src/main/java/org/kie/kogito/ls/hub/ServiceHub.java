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

package org.kie.kogito.ls.hub;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.enterprise.inject.Instance;

import org.kie.kogito.ls.service.Service;
import org.kie.kogito.ls.util.CDIUtil;
import org.kie.kogito.ls.util.MarshallerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ServiceHub {

    private final Logger logger = LoggerFactory.getLogger(ServiceHub.class);
    protected List<Service> services;

    public ServiceHub() {
    }

    public ServiceHub(Instance<Service> services) {
        this.services = CDIUtil.instancesToList(services);
        this.logger.info("{}", services);
    }

    public Service getService(String uri) {
        if (logger.isDebugEnabled()) {
            logger.debug("Searching Service for uri: {}", uri);
        }
        Optional<Service> found = this.services.stream()
                .filter((service -> service.isAcceptable(uri)))
                .max(Comparator.comparingInt(Service::getPriority));
        found.ifPresent((f) -> logger.info("Service Found: {}", f));
        return found.orElseThrow(() -> new TextDocumentServiceException("No implementation found for URI: " + uri));
    }

    protected List<Service> getServices() {
        return this.services;
    }
}
