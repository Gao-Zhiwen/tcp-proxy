package io.mycat.mycat2.annotation.block;

import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.sqlparser.BufferSQLContext;

/**
 * Created by yanjunli on 2017/9/24.
 */
public class TruncateAllow extends BlockHandler {
    public static final TruncateAllow INSTANCE = new TruncateAllow();

    @Override
    protected String getErrorMessage() {
        return "truncate not allow";
    }

    @Override
    protected boolean checkIfBlock(String annoValue) {
        return "true".equals(annoValue);
    }

    @Override
    protected boolean checkIfTypeMatch(MycatSession session) {
        return BufferSQLContext.TRUNCATE_SQL == session.sqlContext.getSQLType();
    }
}
