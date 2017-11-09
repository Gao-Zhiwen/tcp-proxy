package io.mycat.mycat2.beans.conf;

import java.util.List;

/**
 * Desc:
 *
 * @date: 24/10/2017
 * @author: gaul
 */
public class AnnotationBean {
    private boolean enable = true;
    private AnnotationGlobalBean global;
    private List<AnnotationSchemaBean> schemas;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public AnnotationGlobalBean getGlobal() {
        return global;
    }

    public void setGlobal(AnnotationGlobalBean global) {
        this.global = global;
    }

    public List<AnnotationSchemaBean> getSchemas() {
        return schemas;
    }

    public void setSchemas(List<AnnotationSchemaBean> schemas) {
        this.schemas = schemas;
    }
}
