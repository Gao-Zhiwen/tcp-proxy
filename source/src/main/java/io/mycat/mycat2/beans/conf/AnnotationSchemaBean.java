package io.mycat.mycat2.beans.conf;

/**
 * Desc:
 *
 * @date: 24/10/2017
 * @author: gaul
 */
public class AnnotationSchemaBean {
    private String name;
    private String[] allow;
    private AnnotationMatchBean[] matches;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getAllow() {
        return allow;
    }

    public void setAllow(String[] allow) {
        this.allow = allow;
    }

    public AnnotationMatchBean[] getMatches() {
        return matches;
    }

    public void setMatches(AnnotationMatchBean[] matches) {
        this.matches = matches;
    }
}
