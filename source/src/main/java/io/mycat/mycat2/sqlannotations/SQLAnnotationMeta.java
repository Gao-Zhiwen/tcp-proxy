package io.mycat.mycat2.sqlannotations;


import io.mycat.mycat2.annotation.filter.SQLAnnotationCmd;

/**
 * sql 注解元数据
 * @author yanjunli
 *
 */
public interface SQLAnnotationMeta {
	SQLAnnotationCmd getSQLAnnotationCmd();
}
