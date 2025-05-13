package ch.sofa.lodo.data.models;

import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
public class SmsResponse {

	@JsonProperty("StatusCode")
	private int statusCode;

	@JsonProperty("StatusInfo")
	private String statusInfo;

	public SmsResponse() {
	}

	public SmsResponse(int statusCode, String statusInfo) {
		this.statusCode = statusCode;
		this.statusInfo = statusInfo;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(String statusInfo) {
		this.statusInfo = statusInfo;
	}
}
