package io.mycat.mycat2.cmds.strategy;

import io.mycat.mycat2.cmds.ComChangeUserCmd;
import io.mycat.mycat2.cmds.ComFieldListCmd;
import io.mycat.mycat2.cmds.ComInitDB;
import io.mycat.mycat2.cmds.ComPingCmd;
import io.mycat.mycat2.cmds.ComQuitCmd;
import io.mycat.mycat2.cmds.ComStatisticsCmd;
import io.mycat.mycat2.cmds.DirectPassthrouhCmd;
import io.mycat.mycat2.cmds.NotSupportCmd;
import io.mycat.mycat2.cmds.manager.MycatShowConfigsCmd;
import io.mycat.mycat2.cmds.manager.MycatSwitchReplCmd;
import io.mycat.mycat2.cmds.sqlCmds.SqlComBeginCmd;
import io.mycat.mycat2.cmds.sqlCmds.SqlComCommitCmd;
import io.mycat.mycat2.cmds.sqlCmds.SqlComRollBackCmd;
import io.mycat.mycat2.cmds.sqlCmds.SqlComShutdownCmd;
import io.mycat.mycat2.cmds.sqlCmds.SqlComStartCmd;
import io.mycat.mycat2.sqlparser.BufferSQLContext;
import io.mycat.mysql.packet.MySQLPacket;

public class DBInOneServerCmdStrategy extends AbstractCmdStrategy{	
	
	public static final DBInOneServerCmdStrategy INSTANCE = new DBInOneServerCmdStrategy();

	@Override
	protected void initMyCmdHandler() {
		MY_COMMAND_MAP.put(MySQLPacket.COM_QUIT,         			   ComQuitCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_INIT_DB,      			   ComInitDB.INSTANCE);
//		MY_COMMAND_MAP.put(MySQLPacket.COM_QUERY,        			   DirectPassthrouhCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_FIELD_LIST,   			   ComFieldListCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_CREATE_DB,    			   NotSupportCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_DROP_DB,      			   NotSupportCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_REFRESH,      			   DirectPassthrouhCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_SHUTDOWN,     			   NotSupportCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_STATISTICS,   			   ComStatisticsCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_PROCESS_INFO, 			   DirectPassthrouhCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_CONNECT,      			   NotSupportCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_PROCESS_KILL, 			   DirectPassthrouhCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_DEBUG,        			   NotSupportCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_PING,         			   ComPingCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_TIME,         			   NotSupportCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_DELAYED_INSERT,           NotSupportCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_CHANGE_USER,              ComChangeUserCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_BINLOG_DUMP,              DirectPassthrouhCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_TABLE_DUMP,               DirectPassthrouhCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_CONNECT_OUT,              NotSupportCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_REGISTER_SLAVE,           NotSupportCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_STMT_PREPARE,             DirectPassthrouhCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_STMT_EXECUTE,             DirectPassthrouhCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_STMT_SEND_LONG_DATA,      DirectPassthrouhCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_STMT_CLOSE,      		   DirectPassthrouhCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_STMT_RESET,      		   DirectPassthrouhCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_SET_OPTION,      		   DirectPassthrouhCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_STMT_FETCH,      		   DirectPassthrouhCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_DAEMON,      			   NotSupportCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_BINLOG_DUMP_GTID,         DirectPassthrouhCmd.INSTANCE);
		MY_COMMAND_MAP.put(MySQLPacket.COM_RESET_CONNECTION,         DirectPassthrouhCmd.INSTANCE);
	}

	@Override
	protected void initMySqlCmdHandler() {
		MYSQL_COMMAND_MAP.put(BufferSQLContext.INSERT_SQL, DirectPassthrouhCmd.INSTANCE);
		MYSQL_COMMAND_MAP.put(BufferSQLContext.UPDATE_SQL, DirectPassthrouhCmd.INSTANCE);
		MYSQL_COMMAND_MAP.put(BufferSQLContext.COMMIT_SQL, SqlComCommitCmd.INSTANCE);
		MYSQL_COMMAND_MAP.put(BufferSQLContext.ROLLBACK_SQL, SqlComRollBackCmd.INSTANCE);
		MYSQL_COMMAND_MAP.put(BufferSQLContext.SELECT_SQL, DirectPassthrouhCmd.INSTANCE);
		MYSQL_COMMAND_MAP.put(BufferSQLContext.BEGIN_SQL, SqlComBeginCmd.INSTANCE);
		MYSQL_COMMAND_MAP.put(BufferSQLContext.START_SQL, SqlComStartCmd.INSTANCE);
		MYSQL_COMMAND_MAP.put(BufferSQLContext.USE_SQL, SqlComStartCmd.INSTANCE);
		MYSQL_COMMAND_MAP.put(BufferSQLContext.SHUTDOWN_SQL, SqlComShutdownCmd.INSTANCE);
		MYSQL_COMMAND_MAP.put(BufferSQLContext.MYCAT_SWITCH_REPL, MycatSwitchReplCmd.INSTANCE);
		MYSQL_COMMAND_MAP.put(BufferSQLContext.MYCAT_SHOW_CONFIGS, MycatShowConfigsCmd.INSTANCE);
	}
}
