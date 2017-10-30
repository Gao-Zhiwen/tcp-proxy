package io.mycat.mycat2.beans.conf;

/**
 * Desc:
 *
 * @date: 24/10/2017
 * @author: gaul
 */
public class AnnotationBean {
    private boolean enable;
    private String[] allow;
    private AnnotationSchemaBean[] schemas;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String[] getAllow() {
        return allow;
    }

    public void setAllow(String[] allow) {
        this.allow = allow;
    }

    public AnnotationSchemaBean[] getSchemas() {
        return schemas;
    }

    public void setSchemas(AnnotationSchemaBean[] schemas) {
        this.schemas = schemas;
    }
}
