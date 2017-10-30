package io.mycat.mycat2.beans.conf;

import java.util.Map;

/**
 * Desc: 对应annotation.yml文件，用于动态注解的配置
 *
 * @date: 24/10/2017
 * @author: gaul
 */
public class AnnotationConfig {
    private AnnotationBean annotations;

    public AnnotationBean getAnnotations() {
        return annotations;
    }

    public void setAnnotations(AnnotationBean annotations) {
        this.annotations = annotations;
    }
}
