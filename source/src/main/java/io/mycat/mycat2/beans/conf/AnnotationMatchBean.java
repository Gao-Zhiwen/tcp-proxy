package io.mycat.mycat2.beans.conf;

import java.util.Arrays;
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
    private Map<ConditionEnum, String> conditions;
    private String[] tables;
    private FilterBean[] filters;

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

    public String[] getTables() {
        return tables;
    }

    public void setTables(String[] tables) {
        this.tables = tables;
    }

    public FilterBean[] getFilters() {
        return filters;
    }

    public void setFilters(FilterBean[] filters) {
        this.filters = filters;
    }

    @Override
    public String toString() {
        return "AnnotationMatchBean{" + "name='" + name + '\'' + ", enable=" + enable + ", sqlType="
                + sqlType + ", conditions=" + conditions + ", tables=" + Arrays.toString(tables)
                + ", filters=" + Arrays.toString(filters) + '}';
    }
}
