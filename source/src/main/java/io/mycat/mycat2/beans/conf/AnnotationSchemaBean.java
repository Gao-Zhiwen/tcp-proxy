package io.mycat.mycat2.beans.conf;

import java.util.List;
import java.util.Map;

/**
 * Desc:
 *
 * @date: 24/10/2017
 * @author: gaul
 */
public class AnnotationSchemaBean {
    private String name;
    private boolean enable = true;
    private Map<String, String> blocks;
    private Map<String, Map<String, String>> filters;
    private List<AnnotationMatchBean> matches;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Map<String, String> getBlocks() {
        return blocks;
    }

    public void setBlocks(Map<String, String> blocks) {
        this.blocks = blocks;
    }

    public Map<String, Map<String, String>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Map<String, String>> filters) {
        this.filters = filters;
    }

    public List<AnnotationMatchBean> getMatches() {
        return matches;
    }

    public void setMatches(List<AnnotationMatchBean> matches) {
        this.matches = matches;
    }
}
