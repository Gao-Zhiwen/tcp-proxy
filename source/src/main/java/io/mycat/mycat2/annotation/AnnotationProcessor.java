package io.mycat.mycat2.annotation;

import io.mycat.mycat2.MycatConfig;
import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.annotation.block.BlockHandler;
import io.mycat.mycat2.annotation.filter.SQLAnnotationCmd;
import io.mycat.mycat2.beans.conf.ActionBean;
import io.mycat.mycat2.beans.conf.ActionConfig;
import io.mycat.mycat2.beans.conf.AnnotationConfig;
import io.mycat.mycat2.beans.conf.AnnotationMatchBean;
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

        initInstanceCache(actionConfig);

        enable = annotationConfig.getAnnotations().isEnable();
        if (!enable) {
            return;
        }

        initAnnotationCache(annotationConfig);
    }

    private void clearCache() {
        blockInstanceCache.clear();
        filterInstanceCache.clear();

        globalBlocksCache.clear();
        globalFiltersCache.clear();

        schemaBlockCache.clear();
        schemaFilterCache.clear();
    }

    private void initInstanceCache(ActionConfig actionConfig) {
        ActionBean actionBean = actionConfig.getActions();
        iterator(actionBean.getBlocks(), blockInstanceCache, BlockHandler.class);
        iterator(actionBean.getFilters(), filterInstanceCache, SQLAnnotationCmd.class);
    }

    private <T> void iterator(Map<String, String> map, Map<String, T> cache, Class<T> t) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                cache.put(entry.getKey(), (T) getInstance(entry.getValue()));
            } catch (Exception e) {
                LOGGER.error("error to instance for: {}", entry.getValue(), e);
                throw new RuntimeException("instance error");
            }
        }
    }

    /**
     * 初始化cache
     * 
     * @param annotationConfig
     */
    private void initAnnotationCache(AnnotationConfig annotationConfig) {
        // 设置全局参数
        Optional.ofNullable(annotationConfig.getAnnotations().getGlobal())
                .ifPresent(bean -> Stream.of(bean).forEach(block -> {
                    globalBlocksCache.putAll(block.getBlocks());
                    globalFiltersCache.putAll(block.getFilters());
                }));

        // 设置schema
        annotationConfig.getAnnotations().getSchemas().stream().filter(schema -> schema.isEnable())
                .forEach(schema -> {
                    // 设置schema级别的block
                    schemaBlockCache.put(schema.getName(), new HashMap());
                    schemaBlockCache.get(schema.getName()).putAll(globalBlocksCache);
                    Optional.ofNullable(schema.getBlocks())
                            .ifPresent(blocks -> blocks.forEach((name, value) -> {
                                if (blockInstanceCache.get(name) == null) {
                                    LOGGER.error("maybe error, please check annotations.yml");
                                } else {
                                    schemaBlockCache.get(schema.getName()).put(name, value);
                                }
                            }));
                    // 设置schema级别的filter
                    schemaFilterCache.put(schema.getName(), new HashMap());
                    schemaFilterCache.get(schema.getName()).putAll(globalFiltersCache);
                    Optional.ofNullable(schema.getFilters())
                            .ifPresent(filters -> filters.forEach((name, value) -> {
                                if (filterInstanceCache.get(name) == null) {
                                    LOGGER.error("maybe error, please check annotations.yml");
                                } else {
                                    schemaFilterCache.get(schema.getName()).put(name, value);
                                }
                            }));
                });
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

        // 非空，表示存在该schema的annotation配置，需要做match匹配
        if (schemaMap != null) {
            AnnotationConfig annotationConfig = ProxyRuntime.INSTANCE.getConfig().getConfig(ConfigEnum.ANNOTATION);
            annotationConfig.getAnnotations().getSchemas().stream()
                    .filter(schema -> session.schema.getName().equals(schema.getName()))
                    .forEach(schema -> Optional.ofNullable(schema.getMatches())
                            .ifPresent(matches -> matches.stream().filter(match -> matchFilter(session, match))
                                    .forEach(match -> schemaFilters.putAll(match.getFilters()))));
        }

        for (Map.Entry<String, Map<String, String>> entry : schemaFilters.entrySet()) {
            SQLAnnotationCmd cmd = filterInstanceCache.get(entry.getKey());
            if (cmd == null) {
                LOGGER.error("maybe error, please check annotations.yml");
                continue;
            }
            // 动态注解的filter匹配
            chain.addCmdChain(cmd, entry.getValue(), true);
        }
    }

    private boolean matchFilter(MycatSession session, AnnotationMatchBean match) {
        // 过滤match的filters配置为空，是为了过滤没必要的匹配
        if (match.getFilters() == null || match.getFilters().isEmpty()) {
            return false;
        }
        // 判断是否enable
        if (!match.isEnable()) {
            return false;
        }
        // 匹配sqlType
        if (match.getSqlType().getType() != session.sqlContext.getSQLType()) {
            return false;
        }
        // 匹配tables，如果tables没有配置，认为匹配
        if (match.getTables() != null && match.getTables().size() > 0) {
            int tableCount = session.sqlContext.getTableCount();
            boolean tableMatch = false;
            for (int i = 0; i < tableCount; i++) {
                String tableName = session.sqlContext.getTableName(i);
                if (match.getTables().contains(tableName)) {
                    tableMatch = true;
                    break;
                }
            }
            if (!tableMatch) {
                return false;
            }
        }
        // 匹配condition
        if (match.getConditions() != null) {
            for (Map.Entry<AnnotationMatchBean.ConditionEnum, String> entry : match.getConditions().entrySet()) {
                session.sqlContext.getAnnotationCondition();
            }
        }
        LOGGER.debug("dynamic annotation matches for {}", match.getName());
        return true;
    }
}
