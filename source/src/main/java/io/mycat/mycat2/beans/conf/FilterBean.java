package io.mycat.mycat2.beans.conf;

import java.util.Map;

/**
 * Desc:
 *
 * @date: 24/10/2017
 * @author: gaul
 */
public class FilterBean {
    private String name;
    private String className;
    private Map<String, String> param;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void setParam(Map<String, String> param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "FilterBean{" + "name='" + name + '\'' + ", className='" + className + '\''
                + ", param=" + param + '}';
    }
}
