package org.eclipse.remote.internal.jsch.core;

import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.remote.core.IRemoteProcess;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class JSchCommandShell implements IRemoteProcess {

	private final ChannelShell shell;
	
	public JSchCommandShell(Session session) throws JSchException {
		shell = (ChannelShell)session.openChannel("shell");
	}
	
	@Override
	public void destroy() {
		shell.disconnect();
	}

	@Override
	public int exitValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public InputStream getErrorStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream getOutputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int waitFor() throws InterruptedException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isCompleted() {
		return shell.isClosed();
	}

}
