package io.mycat.mycat2.sqlannotations;

import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.annotation.SQLAnnotationChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jamie on 2017/9/24.
 */
public class TableCheck extends SQLAnnotation {

    private static final Logger logger = LoggerFactory.getLogger(TableCheck.class);

    public TableCheck() {
        logger.debug("=>TableCheck 对象本身的构造 初始化");
    }

    @Override
    public void init(Object args) {
        logger.debug("=>TableCheck 动态注解初始化");
    }

    @Override
    public boolean apply(MycatSession context, SQLAnnotationChain chain) {
        return true;
    }
}
