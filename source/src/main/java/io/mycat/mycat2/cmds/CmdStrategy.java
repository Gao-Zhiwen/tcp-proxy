package io.mycat.mycat2.cmds;

import io.mycat.mycat2.MycatSession;

import java.io.IOException;

/**
 * 
 * @author yanjunli
 *
 */
public interface CmdStrategy {
	boolean matchMySqlCommand(MycatSession session) throws IOException;
}
