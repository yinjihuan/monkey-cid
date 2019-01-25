package org.monkey.cid.core.exception;

import org.monkey.cid.core.base.ResponseCode;

public class NoAuthException extends GlobalException {
	
	private static final long serialVersionUID = 7023923040870802290L;

	public NoAuthException(String message) {
		super(message, ResponseCode.NO_AUTH_CODE);
	}

}
