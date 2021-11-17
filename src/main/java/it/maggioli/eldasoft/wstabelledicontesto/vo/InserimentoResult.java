package it.maggioli.eldasoft.wstabelledicontesto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Risultato inserimento
 *
 * @author Mirco.Franzoni
 */
@XmlRootElement
@XmlType(propOrder = {
		"error",
		"validate"
})
@ApiModel(description="Contenitore per il risultato di una operazione di inserimento")
public class InserimentoResult implements Serializable {
	/**
	 * UID.
	 */
	private static final long serialVersionUID = -6611269573839884401L;

	/** Codice di errore in fase di controllo di validazione dei dati */
	public static final String ERROR_VALIDATION = "errore validazione dati";
	
	/** Codice indicante un errore inaspettato. */
	public static final String ERROR_UNEXPECTED = "unexpected-error";

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@ApiModelProperty(value="Eventuale messaggio di errore in caso di operazione fallita")
	private String error;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@ApiModelProperty(value="Eventuale lista dei controlli di validazione che hanno generato errore")
	private List<ValidateEntry> validate;

	public void setError(String error) {
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setValidate(List<ValidateEntry> validate) {
		this.validate = validate;
	}

	public List<ValidateEntry> getValidate() {
		return validate;
	}

}