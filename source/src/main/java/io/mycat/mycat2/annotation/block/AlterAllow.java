package io.mycat.mycat2.annotation.block;

import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.sqlparser.BufferSQLContext;

/**
 * Created by yanjunli on 2017/9/24.
 */
public class AlterAllow extends BlockHandler {
    public static final AlterAllow INSTANCE = new AlterAllow();

    @Override
    protected String getErrorMessage() {
        return "alter not allow";
    }

    @Override
    protected boolean checkIfBlock(String annoValue) {
        return "true".equals(annoValue);
    }

    @Override
    protected boolean checkIfTypeMatch(MycatSession session) {
        return BufferSQLContext.ALTER_SQL == session.sqlContext.getSQLType();
    }
}
