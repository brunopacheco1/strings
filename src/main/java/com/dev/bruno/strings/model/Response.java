package com.dev.bruno.strings.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Response {

	@ApiModelProperty(value="Successful request?", example="false")
	private Boolean success = false;
	
	@ApiModelProperty(value="Response message, when necessary.", example="Not expected error.")
	private String message;

	public Response(Boolean success, String message) {
		super();
		
		this.success = success;
		this.message = message;
	}
	
	public Response(Boolean success) {
		super();
		
		this.success = success;
	}
	
	public Response(String message) {
		super();
		
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
}