package io.mycat.mycat2.beans.conf;

import io.mycat.proxy.Configurable;

/**
 * Desc: 对应action.yml文件，用于动态注解的处理类定义
 *
 * @date: 24/10/2017
 * @author: gaul
 */
public class ActionConfig implements Configurable {
    private ActionBean actions;

    public ActionBean getActions() {
        return actions;
    }

    public void setActions(ActionBean actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "ActionConfig{" + "actions=" + actions + '}';
    }
}
