package io.mycat.mycat2.beans.conf;

import java.util.Arrays;

/**
 *
 *
 * @date: 24/10/2017
 * @author: gaul
 */
public class ActionBean {
    private BlockBean[] blocks;
    private FilterBean[] filters;

    public BlockBean[] getBlocks() {
        return blocks;
    }

    public void setBlocks(BlockBean[] blocks) {
        this.blocks = blocks;
    }

    public FilterBean[] getFilters() {
        return filters;
    }

    public void setFilters(FilterBean[] filters) {
        this.filters = filters;
    }

    @Override
    public String toString() {
        return "ActionBean{" + "blocks=" + Arrays.toString(blocks) + ", filters="
                + Arrays.toString(filters) + '}';
    }
}
