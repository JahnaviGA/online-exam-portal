package com.training.portal.request_response;

import lombok.Data;

@Data
public class AnswerRequest {

	private Long questionId;
	
	private Long answerId;
}
