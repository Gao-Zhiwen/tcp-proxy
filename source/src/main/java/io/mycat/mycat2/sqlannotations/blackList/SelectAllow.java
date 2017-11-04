package io.mycat.mycat2.sqlannotations.blackList;

import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.cmds.interceptor.SQLAnnotationChain;
import io.mycat.mycat2.cmds.interceptor.SQLAnnotationCmd;
import io.mycat.mycat2.sqlannotations.SQLAnnotation;
import io.mycat.mycat2.sqlparser.BufferSQLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yanjunli on 2017/9/24.
 */
public class SelectAllow extends SQLAnnotation<Boolean> {
    private static final Logger logger = LoggerFactory.getLogger(SelectAllow.class);
    public static final SelectAllow INSTANCE = new SelectAllow();

    public SelectAllow() {
        logger.debug("=>SelectAllow 对象本身的构造 初始化");
    }

    @Override
    public void init(Boolean args) {
        logger.debug("=>SelectAllow 动态注解初始化。 " + args);
        BlackListMeta meta = new BlackListMeta();
        meta.setAllow((boolean) args);
        setSqlAnnoMeta(meta);
    }

    @Override
    public boolean apply(MycatSession context, SQLAnnotationChain chain) {
        BlackListMeta meta = (BlackListMeta) getSqlAnnoMeta();
        if (!meta.isAllow()
                && ((BufferSQLContext.SELECT_SQL == context.sqlContext.getSQLType())
                        || (BufferSQLContext.SELECT_INTO_SQL == context.sqlContext.getSQLType()))
                || (BufferSQLContext.SELECT_FOR_UPDATE_SQL == context.sqlContext.getSQLType())) {

            SQLAnnotationCmd blockSqlCmd = getSqlAnnoMeta().getSQLAnnotationCmd();
            blockSqlCmd.setSqlAnnotationChain(chain);
            blockSqlCmd.setErrMsg("select not allow ");
            chain.addCmdChain(this, blockSqlCmd);
            return false;
        }
        return true;
    }
}
