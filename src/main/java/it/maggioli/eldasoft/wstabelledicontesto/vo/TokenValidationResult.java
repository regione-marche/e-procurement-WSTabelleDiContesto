package it.maggioli.eldasoft.wstabelledicontesto.vo;

public class TokenValidationResult {

	private boolean validated;
	private String clientId;
	private String error;
	private boolean canEdit;
	private String syscon;
	
	public boolean isValidated() {
		return validated;
	}
	public void setValidated(boolean validated) {
		this.validated = validated;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public boolean isCanEdit() {
		return canEdit;
	}
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	public String getSyscon() {
		return syscon;
	}
	public void setSyscon(String syscon) {
		this.syscon = syscon;
	}
	
}