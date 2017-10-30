package io.mycat.mycat2.beans.conf;

import java.util.Map;

/**
 * Desc:
 *
 * @date: 24/10/2017
 * @author: gaul
 */
public class AnnotationMatchBean {
    public enum SqlTypeEnum {
        SELECT, UPDATE, DELETE, INSERT;
    }

    public enum ConditionEnum {
        AND, OR;
    }

    private String name;
    private boolean enable;
    private SqlTypeEnum sqlType;
    private Map<ConditionEnum, String> where;
    private String[] tables;
    private Map<String, Map<String, String>> actions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public SqlTypeEnum getSqlType() {
        return sqlType;
    }

    public void setSqlType(SqlTypeEnum sqlType) {
        this.sqlType = sqlType;
    }

    public Map<ConditionEnum, String> getWhere() {
        return where;
    }

    public void setWhere(Map<ConditionEnum, String> where) {
        this.where = where;
    }

    public String[] getTables() {
        return tables;
    }

    public void setTables(String[] tables) {
        this.tables = tables;
    }

    public Map<String, Map<String, String>> getActions() {
        return actions;
    }

    public void setActions(Map<String, Map<String, String>> actions) {
        this.actions = actions;
    }
}
