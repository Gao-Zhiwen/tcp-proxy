package io.mycat.mycat2.annotation.block;

import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.sqlparser.BufferSQLContext;

/**
 * Created by yanjunli on 2017/9/24.
 */
public class InsertAllow extends BlockHandler {
    public static final InsertAllow INSTANCE = new InsertAllow();

    @Override
    protected String getErrorMessage() {
        return "insert not allow";
    }

    @Override
    protected boolean checkIfBlock(String annoValue) {
        return "true".equals(annoValue);
    }

    @Override
    protected boolean checkIfTypeMatch(MycatSession session) {
        return BufferSQLContext.INSERT_SQL == session.sqlContext.getSQLType();
    }
}
