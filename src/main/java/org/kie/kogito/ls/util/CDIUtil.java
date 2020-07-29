package org.kie.kogito.ls.util;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.inject.Instance;

public class CDIUtil {

    public static <T> List<T> instancesToList(Instance<T> instance) {
        return instance.stream().collect(Collectors.toList());
    }
}
