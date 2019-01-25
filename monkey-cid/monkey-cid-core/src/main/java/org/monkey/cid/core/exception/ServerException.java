package org.monkey.cid.core.exception;

import org.monkey.cid.core.base.ResponseCode;

public class ServerException extends GlobalException {
	
	private static final long serialVersionUID = -8967695202238540345L;

	public ServerException(String message) {
		super(message, ResponseCode.SERVER_ERROR_CODE);
	}

}
