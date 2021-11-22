package it.maggioli.eldasoft.wstabelledicontesto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {
		"nome",
		"messaggio"
})
@ApiModel(description="Errore di validazione")
public class ValidateEntry implements Serializable {
	/**
	 * UID.
	 */
	private static final long serialVersionUID = -4433185026855332865L;

	@ApiModelProperty(value="Nome campo validato")
	private String nome;
	@ApiModelProperty(value="Messaggio di errore")  
	private String messaggio;
	
	public ValidateEntry(String nome, String messaggio) {
		this.nome = nome;
		this.messaggio = messaggio;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getNome() {
		return nome;
	}
	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
	public String getMessaggio() {
		return messaggio;
	}
}
