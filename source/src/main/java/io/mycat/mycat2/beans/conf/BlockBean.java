package io.mycat.mycat2.beans.conf;

/**
 * Desc:
 *
 * @date: 24/10/2017
 * @author: gaul
 */
public class BlockBean {
    private String name;
    private String className;
    private String value = "true";

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "BlockBean{" + "name='" + name + '\'' + ", className='" + className + '\''
                + ", value='" + value + '\'' + '}';
    }
}
