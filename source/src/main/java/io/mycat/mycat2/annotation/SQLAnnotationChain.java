package io.mycat.mycat2.annotation;

import io.mycat.mycat2.MySQLCommand;
import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.annotation.filter.SQLAnnotationCmd;
import io.mycat.mycat2.sqlparser.BufferSQLContext;

import java.util.LinkedHashMap;
import java.util.Map;

public class SQLAnnotationChain {
    private MySQLCommand target;

    /**
     * queueMap 用于去重复
     */
    private Map<SQLAnnotationCmd, Map<String, String>> queueMap = new LinkedHashMap<>();

    /**
     * queue 列表当前索引值
     */
    private int cmdIndex = 0;

    /**
     * 1. 设置原始命令
     * 
     * @param target
     */
    public SQLAnnotationChain setTarget(MySQLCommand target) {
        this.target = target;
        return this;
    }

    /**
     * 2. 组装动态注解
     */
    public SQLAnnotationChain buildDynamicAnno(MycatSession session) {
        if (!AnnotationProcessor.INSTANCE.enable) {
            return this;
        }

        AnnotationProcessor.INSTANCE.parseFilter(session, this);
        return this;
    }

    /**
     * 3. 组装静态注解。构建步骤放在动态注解之后，可以保持动态注解的顺序
     * 
     * @param session
     * @param staticAnnontationMap
     * @return
     */
    public SQLAnnotationChain buildStaticAnno(MycatSession session, Map<Byte, SQLAnnotationCmd> staticAnnontationMap) {
        BufferSQLContext context = session.sqlContext;
        SQLAnnotationCmd staticAnno = staticAnnontationMap.get(context.getAnnotationType());
        /**
         * 处理静态注解
         */
        String content = context.getAnnotationContent();
        if (staticAnno != null && !queueMap.containsKey(staticAnno)) {
            addCmdChain(staticAnno, null); //todo
        }
        return this;
    }

    /**
     * 4. 构建 命令 或者命令链
     * 
     * @return
     */
    public MySQLCommand build() {
        if (queueMap.isEmpty()) {
            return target;
        }

        SQLAnnotationCmd annoCmd = new SQLAnnotationCmd();
        annoCmd.setSqlAnnotationChain(this);
        return annoCmd;
    }

    public void addCmdChain(SQLAnnotationCmd cmd, Map<String, String> param) {
        cmd.setSqlAnnotationChain(this);
        queueMap.put(cmd, param);
    }

    public MySQLCommand next() {
        if (queueMap.isEmpty() || cmdIndex >= queueMap.size()) {
            cmdIndex = 0;
            return target;
        }
//        return queueMap.get(cmdIndex++);
        return null; // todo
    }
}
