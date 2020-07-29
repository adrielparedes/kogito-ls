package org.kie.kogito.ls;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.lsp4j.InitializeParams;

@ApplicationScoped
public class InitializationParamsStore {

    private InitializeParams initializeParams;

    public InitializeParams getInitializeParams() {
        return initializeParams;
    }

    public void setInitializeParams(InitializeParams initializeParams) {
        this.initializeParams = initializeParams;
    }
}
