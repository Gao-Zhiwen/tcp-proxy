package io.mycat.mycat2.beans.conf;

import java.util.Arrays;

/**
 * Desc:
 *
 * @date: 24/10/2017
 * @author: gaul
 */
public class AnnotationBean {
    private boolean enable = true;
    private AnnotationSchemaBean[] schemas;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public AnnotationSchemaBean[] getSchemas() {
        return schemas;
    }

    public void setSchemas(AnnotationSchemaBean[] schemas) {
        this.schemas = schemas;
    }

    @Override
    public String toString() {
        return "AnnotationBean{" + "enable=" + enable + ", schemas=" + Arrays.toString(schemas)
                + '}';
    }
}
