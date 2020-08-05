package org.kie.kogito.ls.socket;

import io.quarkus.arc.config.ConfigProperties;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ConfigProperties(prefix = "org.kie.kogito.socket")
public interface SocketConfiguration {

    @ConfigProperty(name = "editor")
    int editorPort();

    @ConfigProperty(name = "jdt")
    int jdtPort();
}
