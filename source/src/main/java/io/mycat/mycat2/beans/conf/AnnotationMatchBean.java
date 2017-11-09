package io.mycat.mycat2.beans.conf;

import io.mycat.mycat2.sqlparser.NewSQLContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Desc:
 *
 * @date: 24/10/2017
 * @author: gaul
 */
public class AnnotationMatchBean {
    public enum SqlTypeEnum {
        SELECT(NewSQLContext.SELECT_SQL),
        UPDATE(NewSQLContext.UPDATE_SQL),
        DELETE(NewSQLContext.DELETE_SQL),
        INSERT(NewSQLContext.INSERT_SQL);
        private byte type;
        SqlTypeEnum(byte type) {
            this.type = type;
        }
        public byte getType() {
            return type;
        }
    }

    public enum ConditionEnum {
        AND, OR;
    }

    private String name;
    private boolean enable;
    private SqlTypeEnum sqlType;
    private Map<ConditionEnum, String> conditions;
    private List<String> tables;
    private Map<String, Map<String, String>> filters;

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

    public Map<ConditionEnum, String> getConditions() {
        return conditions;
    }

    public void setConditions(Map<ConditionEnum, String> conditions) {
        this.conditions = conditions;
    }

    public List<String> getTables() {
        return tables;
    }

    public void setTables(List<String> tables) {
        this.tables = tables;
    }

    public Map<String, Map<String, String>> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Map<String, String>> filters) {
        this.filters = filters;
    }
}
