package org.chaos.spikyfy.spiky.dto;

import java.util.Map;

public class OutputMessageDTO {
	private Boolean successful;
	private Map<String, Object> output;
	
	public Boolean getSuccessful() {
		return successful;
	}
	public void setSuccessful(Boolean successful) {
		this.successful = successful;
	}
	public Map<String, Object> getOutput() {
		return output;
	}
	public void setOutput(Map<String, Object> output) {
		this.output = output;
	}
}
