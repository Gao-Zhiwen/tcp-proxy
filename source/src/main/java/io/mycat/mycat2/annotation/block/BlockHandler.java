package io.mycat.mycat2.annotation.block;

import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.beans.GlobalBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Desc: 动态注解拦截类接口定义
 *
 * @date: 04/11/2017
 * @author: gaul
 */
public abstract class BlockHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(BlockHandler.class);

    public final boolean handle(MycatSession session, String annoValue) {
        LOGGER.debug("handle block");
        if (checkIfTypeMatch(session) && !checkIfBlock(annoValue)) {
            session.getSessionAttrMap().put(GlobalBean.ANNOTATION_BLOCK_ERR_MSG, getErrorMessage());
            return false;
        }
        return true;
    }

    protected abstract String getErrorMessage();

    protected abstract boolean checkIfBlock(String annoValue);

    protected abstract boolean checkIfTypeMatch(MycatSession session);
}
