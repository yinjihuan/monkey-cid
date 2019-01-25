package org.monkey.cid.core.exception;

import org.monkey.cid.core.base.ResponseCode;

public class ParamException extends GlobalException {
	
	private static final long serialVersionUID = -4570951822417437818L;

	public ParamException(String message) {
		super(message, ResponseCode.PARAM_ERROR_CODE);
	}

}
