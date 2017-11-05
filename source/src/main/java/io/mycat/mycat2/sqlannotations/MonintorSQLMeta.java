package io.mycat.mycat2.sqlannotations;


import io.mycat.mycat2.annotation.filter.MonintorSQLCmd;
import io.mycat.mycat2.annotation.filter.SQLAnnotationCmd;

public class MonintorSQLMeta implements SQLAnnotationMeta {

	@Override
	public SQLAnnotationCmd getSQLAnnotationCmd() {
		return new MonintorSQLCmd();
	}

}
