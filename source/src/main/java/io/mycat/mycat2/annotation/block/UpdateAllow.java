package io.mycat.mycat2.annotation.block;

import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.sqlparser.BufferSQLContext;

/**
 * Created by yanjunli on 2017/9/24.
 */
public class UpdateAllow extends BlockHandler {
    public static final UpdateAllow INSTANCE = new UpdateAllow();

    @Override
    protected String getErrorMessage() {
        return "update not allow";
    }

    @Override
    protected boolean checkIfBlock(String annoValue) {
        return "true".equals(annoValue);
    }

    @Override
    protected boolean checkIfTypeMatch(MycatSession session) {
        return BufferSQLContext.UPDATE_SQL == session.sqlContext.getSQLType();
    }
}
