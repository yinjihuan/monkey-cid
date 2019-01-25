package org.monkey.cid.core.exception;

import org.monkey.cid.core.base.ResponseCode;

public class NullException extends GlobalException {
	
	private static final long serialVersionUID = -3026346243152125509L;

	public NullException(String message) {
		super(message, ResponseCode.NOT_FOUND_CODE);
	}

}
