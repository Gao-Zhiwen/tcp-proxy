package io.mycat.mycat2.cmds.strategy;

import io.mycat.mycat2.MySQLCommand;
import io.mycat.mycat2.MycatSession;
import io.mycat.mycat2.annotation.AnnotationProcessor;
import io.mycat.mycat2.beans.GlobalBean;
import io.mycat.mycat2.cmds.CmdStrategy;
import io.mycat.mycat2.cmds.DirectPassthrouhCmd;
import io.mycat.mycat2.cmds.interceptor.SQLAnnotationChain;
import io.mycat.mycat2.sqlannotations.CacheResult;
import io.mycat.mycat2.sqlannotations.CacheResultMeta;
import io.mycat.mycat2.sqlannotations.CatletMeta;
import io.mycat.mycat2.sqlannotations.CatletResult;
import io.mycat.mycat2.sqlannotations.SQLAnnotation;
import io.mycat.mycat2.sqlparser.BufferSQLContext;
import io.mycat.mycat2.sqlparser.BufferSQLParser;
import io.mycat.mysql.packet.ErrorPacket;
import io.mycat.mysql.packet.MySQLPacket;
import io.mycat.util.ErrorCode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCmdStrategy implements CmdStrategy {
    /**
     * 进行MySQL命令的处理的容器
     */
    protected Map<Byte, MySQLCommand> MY_COMMAND_MAP = new HashMap<>();

    /**
     * 进行SQL命令的处理的容器
     */
    protected Map<Byte, MySQLCommand> MYSQL_COMMAND_MAP = new HashMap<>();

    private Map<Byte, SQLAnnotation> staticAnnontationMap = new HashMap<>();

    /**
     * sqlparser
     */
    protected BufferSQLParser parser = new BufferSQLParser();

    public AbstractCmdStrategy() {
        initMyCmdHandler();
        initMySqlCmdHandler();
        initStaticAnnotation();
    }

    private void initStaticAnnotation() {
        CacheResultMeta cacheResultMeta = new CacheResultMeta();
        SQLAnnotation cacheResult = new CacheResult();
        cacheResult.setSqlAnnoMeta(cacheResultMeta);
        staticAnnontationMap.put(BufferSQLContext.ANNOTATION_SQL_CACHE, cacheResult);

        //hbt静态注解
        SQLAnnotation catlet = new CatletResult();
        catlet.setSqlAnnoMeta(new CatletMeta());
        staticAnnontationMap.put(BufferSQLContext.ANNOTATION_CATLET, catlet );
    }

    protected abstract void initMyCmdHandler();

    protected abstract void initMySqlCmdHandler();

    @Override
    public final boolean matchMySqlCommand(MycatSession session) throws IOException {
        MySQLCommand command = null;
        if (MySQLPacket.COM_QUERY == (byte) session.curMSQLPackgInf.pkgType) {
            /**
             * sqlparser
             */
            BufferSQLParser parser = new BufferSQLParser();
            int rowDataIndex = session.curMSQLPackgInf.startPos + MySQLPacket.packetHeaderSize + 1;
            int length = session.curMSQLPackgInf.pkgLength - MySQLPacket.packetHeaderSize - 1;

            BufferSQLContext context = session.sqlContext;
            parser.parse(session.proxyBuffer.getBuffer(), rowDataIndex, length, context);
            byte sqltype = context.getSQLType() != 0 ? context.getSQLType() : context.getCurSQLType();
            command = MYSQL_COMMAND_MAP.get(sqltype);
        } else {
            command = MY_COMMAND_MAP.get((byte) session.curMSQLPackgInf.pkgType);
        }
        if (command == null) {
            command = DirectPassthrouhCmd.INSTANCE;
        }

        // 处理动态注解的block
        if (AnnotationProcessor.INSTANCE.enable && !AnnotationProcessor.INSTANCE.parseBlock(session)) {
            ErrorPacket errPkg = new ErrorPacket();
            errPkg.packetId = 1;
            errPkg.errno = ErrorCode.ERR_WRONG_USED;
            errPkg.message = (String) session.getSessionAttrMap().remove(GlobalBean.ANNOTATION_BLOCK_ERR_MSG);
            session.responseOKOrError(errPkg);
            return false;
        }

        /**
         * 设置原始处理命令 1. 设置目标命令 2. 处理动态注解 3. 处理静态注解 4. 构建命令或者注解链。 如果没有注解链，直接返回目标命令
         */
        SQLAnnotationChain chain = new SQLAnnotationChain();
        session.curSQLCommand = chain.setTarget(command)
                .processDynamicAnno(session)
                .processStaticAnno(session, staticAnnontationMap)
                .build();
        return true;
    }
}
