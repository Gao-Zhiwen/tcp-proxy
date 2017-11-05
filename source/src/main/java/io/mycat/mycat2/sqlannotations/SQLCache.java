package io.mycat.mycat2.sqlannotations;

import io.mycat.mycat2.annotation.SQLAnnotationChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.mycat2.MycatSession;

public class SQLCache extends SQLAnnotation {

	public static final SQLCache INSTANCE = new SQLCache();
	
	private static final Logger logger = LoggerFactory.getLogger(SQLCache.class);
		
	/**
	 * 组装 mysqlCommand
	 */
	@Override
	public boolean apply(MycatSession context,SQLAnnotationChain chain) {
		return true;
	}

	@Override
	public void init(Object args) {

	}
}
