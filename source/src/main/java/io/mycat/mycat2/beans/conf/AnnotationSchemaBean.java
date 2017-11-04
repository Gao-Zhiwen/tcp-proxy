package io.mycat.mycat2.beans.conf;

import java.util.Arrays;
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
    private FilterBean[] filters;
    private AnnotationMatchBean[] matches;

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

    public FilterBean[] getFilters() {
        return filters;
    }

    public void setFilters(FilterBean[] filters) {
        this.filters = filters;
    }

    public AnnotationMatchBean[] getMatches() {
        return matches;
    }

    public void setMatches(AnnotationMatchBean[] matches) {
        this.matches = matches;
    }

    @Override
    public String toString() {
        return "AnnotationSchemaBean{" + "name='" + name + ", enable=" + enable + ", blocks="
                + blocks + ", filters=" + Arrays.toString(filters) + ", matches="
                + Arrays.toString(matches) + '}';
    }
}
