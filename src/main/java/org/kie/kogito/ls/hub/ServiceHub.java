package org.kie.kogito.ls.hub;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javax.enterprise.inject.Instance;

import org.kie.kogito.ls.service.Service;
import org.kie.kogito.ls.util.CDIUtil;
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
