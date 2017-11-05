package io.mycat.mycat2.annotation;

import io.mycat.mycat2.MycatConfig;
import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.annotation.block.BlockHandler;
import io.mycat.mycat2.beans.conf.ActionConfig;
import io.mycat.mycat2.beans.conf.AnnotationConfig;
import io.mycat.mycat2.annotation.filter.SQLAnnotationCmd;
import io.mycat.proxy.ConfigEnum;
import io.mycat.proxy.ProxyRuntime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Desc: 动态注解的处理类，入口
 *
 * @date: 25/10/2017
 * @author: gaozhiwen
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

    private Map<String, Map<String, String>> schemaBlockCache = new HashMap<>();
    private Map<String, Map<String, Map<String, String>>> schemaFilterCache = new HashMap<>();

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
        blockInstanceCache.clear();
        filterInstanceCache.clear();

        globalBlocksCache.clear();
        globalFiltersCache.clear();

        schemaBlockCache.clear();
        schemaFilterCache.clear();
    }

    /**
     * 初始化cache
     * 
     * @param actionConfig
     * @param annotationConfig
     */
    private void initCache(ActionConfig actionConfig, AnnotationConfig annotationConfig) {
        // 设置全局block
        Optional.ofNullable(actionConfig.getActions().getBlocks()).ifPresent(bean -> Stream.of(bean)
//                .filter(block -> !"true".equals(block.getValue()))
                .forEach(block -> {
                    try {
                        blockInstanceCache.put(block.getName(), (BlockHandler) getInstance(block.getClassName()));
                        globalBlocksCache.put(block.getName(), block.getValue());
                    } catch (Exception e) {
                        LOGGER.error("error to get block instance for {}", block.getClassName(), e);
                    }
                }));

        // 设置全局filter
        Optional.ofNullable(actionConfig.getActions().getFilters())
                .ifPresent(bean -> Stream.of(bean).forEach(filter -> {
                    try {
                        filterInstanceCache.put(filter.getName(), (SQLAnnotationCmd) getInstance(filter.getClassName()));
                        globalFiltersCache.put(filter.getName(), filter.getParam());
                    } catch (Exception e) {
                        LOGGER.error("error to get filter instance for {}", filter.getClassName(), e);
                    }
                }));

        // 设置schema
        Optional.ofNullable(annotationConfig.getAnnotations().getSchemas()).ifPresent(
                bean -> Stream.of(bean).filter(schema -> schema.isEnable()).forEach(schema -> {
                    // 设置schema级别的block
                    schemaBlockCache.put(schema.getName(), new HashMap());
                    schemaBlockCache.get(schema.getName()).putAll(globalBlocksCache);
                    Optional.ofNullable(schema.getBlocks()).ifPresent(blocks ->
                        blocks.forEach((name, value) -> {
                            if (blockInstanceCache.get(name) == null) {
                                LOGGER.error("maybe error, please check annotations.yml");
                            } else {
                                schemaBlockCache.get(schema.getName()).put(name, value);
                            }
                        }));
                    // 设置schema级别的filter
                    schemaFilterCache.put(schema.getName(), new HashMap<>());
                    schemaFilterCache.get(schema.getName()).putAll(globalFiltersCache);
                    Optional.ofNullable(schema.getFilters()).ifPresent(filters ->
                        Stream.of(filters).forEach(filter -> {
                            if (filterInstanceCache.get(filter.getName()) == null) {
                                LOGGER.error("maybe error, please check annotations.yml");
                            } else {
                                schemaFilterCache.get(schema.getName()).put(filter.getName(), filter.getParam());
                            }
                        }));
                }));
    }

    private Object getInstance(String className) throws Exception {
        Class clazz = Class.forName(className);
        Field field = clazz.getField("INSTANCE");
        return field.get(null);
    }

    /**
     * 处理block类
     *
     * @param session
     * @return
     */
    public boolean parseBlock(MycatSession session) {
        boolean result = true;
        Map<String, String> schemaMap = schemaBlockCache.get(session.schema.getName());
        Map<String, String> schemaBlocks = schemaMap == null ? globalBlocksCache : schemaMap;
        for (Map.Entry<String, String> entry : schemaBlocks.entrySet()) {
            BlockHandler blockHandler = blockInstanceCache.get(entry.getKey());
            if (blockHandler != null && !blockHandler.handle(session, entry.getValue())) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * 匹配filter类，将匹配上的结果放入chain中
     *
     * @param session
     * @param chain
     * @return
     */
    public void parseFilter(MycatSession session, SQLAnnotationChain chain) {
        Map<String, Map<String, String>> schemaMap = schemaFilterCache.get(session.schema.getName());
        Map<String, Map<String, String>> schemaFilters = schemaMap == null ? globalFiltersCache : schemaMap;
        for (Map.Entry<String, Map<String, String>> entry : schemaFilters.entrySet()) {
            SQLAnnotationCmd cmd = filterInstanceCache.get(entry.getKey());
            // todo 匹配match
            // 动态注解的filter匹配
            chain.addCmdChain(cmd, entry.getValue());
        }
    }
}
