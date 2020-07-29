package org.kie.kogito.ls.util;

import java.io.Closeable;
import java.io.IOException;

public class ClosableUtil {

    public static void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
