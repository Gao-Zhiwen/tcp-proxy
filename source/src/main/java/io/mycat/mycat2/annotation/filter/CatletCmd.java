package io.mycat.mycat2.annotation.filter;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.mycat2.MySQLCommand;
import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.sqlparser.BufferSQLContext;
import io.mycat.proxy.ProxyRuntime;
import io.mycat.util.ErrorCode;

public class CatletCmd extends SQLAnnotationCmd {
	private static final Logger LOGGER = LoggerFactory.getLogger(CatletCmd.class);
	public static final CatletCmd INSTANCE = new CatletCmd();

	@Override
	public boolean procssSQL(MycatSession session) throws IOException {
		
		LOGGER.debug("=====>   CatletCmd   processSQL");
		
		BufferSQLContext context = session.sqlContext;
		
		if(BufferSQLContext.SELECT_SQL != context.getSQLType()){
			String errmsg = " sqlType is invalid . sqlcache  type must be select !";
			session.sendErrorMsg(ErrorCode.ER_INVALID_DEFAULT,errmsg);
			LOGGER.error(errmsg);
			return true;
		}
		
//		if(BufferSQLContext.ANNOTATION_CATLET != context.getAnnotationType()){
//			
//			String errmsg = " annotationType is invalid . annotationType must be ANNOTATION_CATLET !";
//			session.sendErrorMsg(ErrorCode.ER_INVALID_DEFAULT,errmsg);
//			LOGGER.error(errmsg);
//			return true;
//		}
		String clazz =  context.getAnnotationStringValue(BufferSQLContext.ANNOTATION_CATLET);
		//System.out.println(context.getCatletName());
		//String clazz = "io.mycat.mycat2.cmds.HBTDemoCmd2";
		try {
			MySQLCommand target = (MySQLCommand) ProxyRuntime.INSTANCE.getCatletLoader().getInstanceofClass(clazz);
			super.sqlAnnoChain.setTarget(target);
			return super.procssSQL(session);
		} catch (Exception e) {
			String errmsg = String.format(" cant not find %s in catlet home !", clazz);
			session.sendErrorMsg(ErrorCode.ER_INVALID_DEFAULT,errmsg);
			LOGGER.error(errmsg);
			e.printStackTrace();
		}
		return true;
	}
	 @Override
	 public boolean onFrontWriteFinished(MycatSession session) throws IOException {
		 LOGGER.debug("========================> CatletCmd onFrontWriteFinished");
		 return super.onFrontWriteFinished(session);
	 }
	
}
