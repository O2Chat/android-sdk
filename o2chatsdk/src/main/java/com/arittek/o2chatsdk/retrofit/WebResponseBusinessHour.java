package com.arittek.o2chatsdk.retrofit;

import com.arittek.o2chatsdk.model.chat.BusinessHourModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class WebResponseBusinessHour<T> {

	@SerializedName("isSuccess")
	@Expose
	private boolean isSuccess;

	@SerializedName("message")
	@Expose
	private String message;

	public BusinessHourModel getBusinessHourModel() {
		return businessHourModel;
	}

	@SerializedName("result")
	@Expose
	private BusinessHourModel businessHourModel;

	@SerializedName("id")
	@Expose
	private int id;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean success) {
		isSuccess = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}