package io.mycat.mycat2.beans.conf;

import io.mycat.proxy.Configurable;

import java.util.Map;

/**
 * Desc: 对应action.yml文件，用于动态注解的处理类定义
 *
 * @date: 24/10/2017
 * @author: gaul
 */
public class ActionConfig implements Configurable {
    private Map<String, String> actions;

    public Map<String, String> getActions() {
        return actions;
    }

    public void setActions(Map<String, String> actions) {
        this.actions = actions;
    }
}
