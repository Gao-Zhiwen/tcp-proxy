package io.mycat.mycat2.annotation;

import io.mycat.mycat2.MySQLCommand;
import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.annotation.filter.SQLAnnotationCmd;
import io.mycat.mycat2.sqlannotations.SQLAnnotation;
import io.mycat.mycat2.sqlparser.BufferSQLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SQLAnnotationChain {
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLAnnotationChain.class);
    private MySQLCommand target;

    private List<SQLAnnotationCmd> cmdList = new ArrayList<>();
    private List<Map<String, String>> paramList = new ArrayList<>();

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
        if (AnnotationProcessor.INSTANCE.enable) {
            AnnotationProcessor.INSTANCE.parseFilter(session, this);
        }
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
        if (staticAnno != null) {
            addCmdChain(staticAnno, null, false); //todo 将null使用静态注解中的参数map替换
        }
        return this;
    }

    /**
     * 4. 构建 命令 或者命令链
     * 
     * @return
     */
    public MySQLCommand build() {
        if (cmdList.isEmpty()) {
            return target;
        }

        SQLAnnotationCmd annoCmd = new SQLAnnotationCmd();
        annoCmd.setSqlAnnotationChain(this);
        return annoCmd;
    }

    public void addCmdChain(SQLAnnotationCmd cmd, Map<String, String> param, boolean replace) {
        cmd.setSqlAnnotationChain(this);
        int index = cmdList.indexOf(cmd);
        if (index == -1) {
            // -1表示不存在该注解，则直接插入
            cmdList.add(cmd);
            paramList.add(param);
        } else {
            // 不为-1表示存在该注解，则根据是否replace去处理是否需要替换
            if (replace) {
                paramList.add(index, param);
            }
        }
    }

    public MySQLCommand next() {
        if (cmdList.isEmpty() || cmdIndex >= cmdList.size()) {
            cmdIndex = 0;
            return target;
        }
        return cmdList.get(cmdIndex++);
    }

    public Map<String, String> getParam() {
        if (cmdIndex >= paramList.size()) {
            LOGGER.error("cmdIndex: {} is larger than paramList size: {}", cmdIndex, paramList.size());
            return null;
        }
        return paramList.get(cmdIndex);
    }
}
