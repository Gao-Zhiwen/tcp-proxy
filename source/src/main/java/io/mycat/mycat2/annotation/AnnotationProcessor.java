package io.mycat.mycat2.annotation;

import io.mycat.mycat2.MycatConfig;
import io.mycat.mycat2.beans.conf.ActionConfig;
import io.mycat.mycat2.beans.conf.AnnotationConfig;
import io.mycat.proxy.ConfigEnum;
import io.mycat.proxy.ProxyRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Desc: 动态注解的处理类，入口
 *
 * @date: 25/10/2017
 * @author: gaul
 */
public class AnnotationProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationProcessor.class);
    public static final AnnotationProcessor INSTANCE = new AnnotationProcessor();

    /**
     * 动态注解初始化准备，解析并处理配置
     */
    public void init() {
        LOGGER.debug("init for dynamic annotation");
        MycatConfig config = ProxyRuntime.INSTANCE.getConfig();
        ActionConfig actionConfig = config.getConfig(ConfigEnum.ACTION);
        AnnotationConfig annoConfig = config.getConfig(ConfigEnum.ANNOTATION);

        if (!annoConfig.getAnnotations().isEnable()) {
            // 未开启动态注解
            LOGGER.info("dynamic annotation is off");
            return;
        }
    }
}
