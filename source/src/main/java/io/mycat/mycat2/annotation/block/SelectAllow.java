package io.mycat.mycat2.annotation.block;

import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.sqlparser.BufferSQLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yanjunli on 2017/9/24.
 */
public class SelectAllow extends BlockHandler {
    public static final SelectAllow INSTANCE = new SelectAllow();

    @Override
    protected String getErrorMessage() {
        return "select not allow";
    }

    @Override
    protected boolean checkIfBlock(String annoValue) {
        return "true".equals(annoValue);
    }

    @Override
    protected boolean checkIfTypeMatch(MycatSession session) {
        return BufferSQLContext.SELECT_SQL == session.sqlContext.getSQLType()
                || BufferSQLContext.SELECT_INTO_SQL == session.sqlContext.getSQLType()
                || BufferSQLContext.SELECT_FOR_UPDATE_SQL == session.sqlContext.getSQLType();
    }
}
