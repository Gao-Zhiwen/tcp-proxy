package io.mycat.mycat2.annotation;

import io.mycat.mycat2.MycatConfig;
import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.annotation.block.BlockHandler;
import io.mycat.mycat2.beans.conf.*;
import io.mycat.mycat2.cmds.interceptor.SQLAnnotationCmd;
import io.mycat.mycat2.sqlparser.BufferSQLContext;
import io.mycat.proxy.ConfigEnum;
import io.mycat.proxy.ProxyRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Desc: 动态注解的处理类，入口
 *
 * @date: 25/10/2017
 * @author: gaul
 */
public class AnnotationProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(AnnotationProcessor.class);
    public static final AnnotationProcessor INSTANCE = new AnnotationProcessor();

    // 动态注解是否开启标志
    public boolean enable;

    private Map<String, BlockHandler> blockInstanceCache = new HashMap<>();
    private Map<String, SQLAnnotationCmd> filterInstanceCache = new HashMap<>();

    // 缓存全局的拦截类
    private Map<String, String> globalBlocksCache = new HashMap<>();
    // 缓存全局的环绕类
    private Map<String, Map<String, String>> globalFiltersCache = new HashMap<>();
    private Map<String, AnnotationSchemaBean> schemaCache = new HashMap<>();

    /**
     * 动态注解初始化准备，解析并处理配置
     */
    public void init() {
        LOGGER.debug("init for dynamic annotation");
        clearCache();

        MycatConfig config = ProxyRuntime.INSTANCE.getConfig();
        ActionConfig actionConfig = config.getConfig(ConfigEnum.ACTION);
        AnnotationConfig annotationConfig = config.getConfig(ConfigEnum.ANNOTATION);

        enable = annotationConfig.getAnnotations().isEnable();
        if (!enable) {
            return;
        }

        initCache(actionConfig, annotationConfig);
    }

    private void clearCache() {
        globalBlocksCache.clear();
        globalFiltersCache.clear();
        schemaCache.clear();
    }

    /**
     * 初始化cache
     * 
     * @param actionConfig
     * @param annotationConfig
     */
    private void initCache(ActionConfig actionConfig, AnnotationConfig annotationConfig) {
        // 设置全局block
        BlockBean[] blocks = actionConfig.getActions().getBlocks();
        if (blocks != null) {
            Stream.of(blocks).forEach(block -> {
                try {
                    blockInstanceCache.put(block.getName(), (BlockHandler) getInstance(block.getClassName()));
                } catch (Exception e) {
                    LOGGER.error("error to get block instance for {}", block.getClassName(), e);
                }
                globalBlocksCache.put(block.getName(), block.getValue());
            });
        }

        // 设置全局filter
        FilterBean[] filters = actionConfig.getActions().getFilters();
        if (filters != null) {
            Stream.of(filters).forEach(filter -> {
                try {
                    filterInstanceCache.put(filter.getName(), (SQLAnnotationCmd) getInstance(filter.getClassName()));
                } catch (Exception e) {
                    LOGGER.error("error to get filter instance for {}", filter.getClassName(), e);
                }
                globalFiltersCache.put(filter.getName(), filter.getParam());
            });
        }

        AnnotationSchemaBean[] schemas = annotationConfig.getAnnotations().getSchemas();
        if (schemas != null) {
            Stream.of(schemas).filter(schema -> schema.isEnable()).forEach(schema -> {
                // 设置schema级别的block
                if (schema.getBlocks() == null) {
                    schema.setBlocks(globalBlocksCache);
                } else {
                    globalBlocksCache.forEach((name, value) -> {
                        if (!schema.getBlocks().containsKey(name)) {
                            schema.getBlocks().put(name, value);
                        }
                    });
                }
                schemaCache.put(schema.getName(), schema);
            });
        }
    }

    private Object getInstance(String className) throws Exception {
        Class clazz = Class.forName(className);
        Field field = clazz.getField("INSTANCE");
        return field.get(null);
    }

    public boolean parseBlock(MycatSession session) {
        String schema = session.schema.getName();
        AnnotationSchemaBean schemaBean = schemaCache.get(schema);
        if (schemaBean == null) {
            return true;
        }

        boolean result = true;
        Map<String, String> schemaBlocks = schemaBean.getBlocks();
        for (Map.Entry<String, String> entry : schemaBlocks.entrySet()) {
            BlockHandler blockHandler = blockInstanceCache.get(entry.getKey());
            if (!blockHandler.handle(session, entry.getValue())) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * 返回false代表没有匹配的action
     * 
     * @param context
     * @param session
     * @return
     */
    public boolean parseFilter(BufferSQLContext context, MycatSession session) {
        return true;
    }
}
