package io.mycat.mycat2.sqlannotations;

import io.mycat.mycat2.cmds.interceptor.SQLAnnotationCmd;
import io.mycat.mycat2.cmds.interceptor.SQLCacheCmd;

public class CacheResultMeta implements SQLAnnotationMeta {
	
	/**
	 * 缓存时间
	 */
	private long cacheTime = 0;
		
	/**
	 * 访问次数
	 */
	private long accessCount = 0;


	public long getCacheTime() {
		return cacheTime;
	}


	public void setCacheTime(long cacheTime) {
		this.cacheTime = cacheTime;
	}


	public long getAccessCount() {
		return accessCount;
	}


	public void setAccessCount(long accessCount) {
		this.accessCount = accessCount;
	}


	@Override
	public SQLAnnotationCmd getSQLAnnotationCmd() {
		return new SQLCacheCmd();
	}
}
