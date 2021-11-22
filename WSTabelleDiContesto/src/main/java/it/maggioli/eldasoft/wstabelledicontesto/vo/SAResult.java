package it.maggioli.eldasoft.wstabelledicontesto.vo;

import java.util.List;

public class SAResult {
	
	private boolean esito;
	private String error;
	private List<SaInfo> data;
	
	public boolean isEsito() {
		return esito;
	}
	public void setEsito(boolean esito) {
		this.esito = esito;
	}

	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public List<SaInfo> getData() {
		return data;
	}
	public void setData(List<SaInfo> data) {
		this.data = data;
	}
}
