package io.mycat.mycat2.annotation.block;

import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.sqlparser.BufferSQLContext;

/**
 * Created by yanjunli on 2017/9/24.
 */
public class CreateTableAllow extends BlockHandler {
    public static final CreateTableAllow INSTANCE = new CreateTableAllow();

    @Override
    protected String getErrorMessage() {
        return "create table not allow";
    }

    @Override
    protected boolean checkIfBlock(String annoValue) {
        return "true".equals(annoValue);
    }

    @Override
    protected boolean checkIfTypeMatch(MycatSession session) {
        return BufferSQLContext.CREATE_SQL == session.sqlContext.getSQLType();
    }
}
