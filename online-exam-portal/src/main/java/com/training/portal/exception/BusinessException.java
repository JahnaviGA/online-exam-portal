package com.training.portal.exception;

public class BusinessException  extends RuntimeException{

private static final long serialVersionUID = 1L;
	
	private ErrorDetails responseDetail;
	private String customMessage;

    public BusinessException(ErrorDetails responseDetail) {
        this.responseDetail = responseDetail;
    }
    
    
    public BusinessException(String customMessage) {
        this.customMessage = customMessage;
    }

	public ErrorDetails getResponseDetail() {
		return responseDetail;
	}


	public String getCustomMessage() {
		return customMessage;
	}


	public void setCustomMessage(String customMessage) {
		this.customMessage = customMessage;
	}
	
	
}
