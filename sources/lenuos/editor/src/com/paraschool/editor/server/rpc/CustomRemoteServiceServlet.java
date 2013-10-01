package com.paraschool.editor.server.rpc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.ShiroException;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.UnexpectedException;

@SuppressWarnings("serial")
public class CustomRemoteServiceServlet extends RemoteServiceServlet {
	
	protected Log logger = LogFactory.getLog(getClass());	
	
	@Override
	public String processCall(String payload) throws SerializationException {
		try{
			return super.processCall(payload);
		}catch (UnexpectedException e) {			
			if(e.getCause() instanceof ShiroException) {
				RPCRequest rpcRequest = RPC.decodeRequest(payload, null, this);
				return RPC.encodeResponseForFailure(rpcRequest.getMethod(), SecurityExceptionFactory.get((ShiroException)e.getCause()),
						rpcRequest.getSerializationPolicy(), rpcRequest.getFlags());
			}
			logger.debug("RPC call has an exception\n"+e);
			throw e;
		}
		
	}

	
}
