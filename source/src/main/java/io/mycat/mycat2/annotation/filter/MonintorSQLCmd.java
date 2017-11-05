package io.mycat.mycat2.annotation.filter;

import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.sqlannotations.MonintorSQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * SQL 监控需要做成异步
 * 
 * @author yanjunli
 *
 */
public class MonintorSQLCmd extends SQLAnnotationCmd {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonintorSQLCmd.class);
    public static final MonintorSQLCmd INSTANCE = new MonintorSQLCmd();

    @Override
    public boolean procssSQL(MycatSession session) throws IOException {
        LOGGER.debug("=====>   MonitorSQLCmd   processSQL");
        return super.procssSQL(session);
    }

    @Override
    public boolean onFrontWriteFinished(MycatSession session) throws IOException {
        LOGGER.debug("=====>   MonitorSQLCmd   onFrontWriteFinished");
        return super.onFrontWriteFinished(session);
    }
}
