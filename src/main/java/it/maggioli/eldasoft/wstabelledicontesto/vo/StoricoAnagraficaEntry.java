/*
 * Created on 01/giu/2017
 *
 * Copyright (c) Maggioli S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di Maggioli S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di
 * aver prima formalizzato un accordo specifico con Maggioli.
 */
package it.maggioli.eldasoft.wstabelledicontesto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Dati storico anagrafica UFFINT.
 *
 * @author Mirco.Franzoni
 */
@XmlType(propOrder = {
    "dataFineValidita", "denominazione", "indirizzo", "civico", "codiceIstat", "localita", "provincia", "cap",
    "codiceNazione", "telefono", "fax", "codiceFiscale", "tipoAmministrazione", "email",
    "pec", "iscuc", "cfAnac"
})
    
@ApiModel(description="Dati relativi allo storico anagrafica della stazione appaltante")
public class StoricoAnagraficaEntry implements Serializable {
	/**
	 * UID.
	 */
	private static final long serialVersionUID = -4433185026855332865L;

	@ApiModelProperty(value = "Id",hidden=true)
	private Long id;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@ApiModelProperty(value="Data fine validità")  
	private Date dataFineValidita;
	
	@ApiModelProperty(value = "Codice Stazione appaltante", hidden=true)
	private String codice;
	
	@ApiModelProperty(value = "Denominazione")
	private String denominazione;
	
	@ApiModelProperty(value = "Indirizzo (via/piazza/corso)")
	private String indirizzo;
	
	@ApiModelProperty(value = "Numero civico")
	private String civico;
	
	@ApiModelProperty(value = "Codice istat comune")
	private String codiceIstat;
	
	@ApiModelProperty(value = "Localita'dell'intestatario")
	private String localita;
	
	@ApiModelProperty(value = "Provincia dell'intestatario	(Tabellati/Province)")
	private String provincia;  //PROV. - Agx15
	
	@ApiModelProperty(value = "Codice di avviamento postale")
	private String cap;

	@ApiModelProperty(value = "Codice nazione")
	private Integer codiceNazione;
	
	@ApiModelProperty(value = "Telefono")
	private String telefono;
	
	@ApiModelProperty(value = "FAX")
	private String fax;
	
	@ApiModelProperty(value = "Codice fiscale")
	private String codiceFiscale;
	
	@ApiModelProperty(value = "Tipo di amministrazione	(/Tabellati/Valori?cod=TipologiaAmministrazione)")
	private Long tipoAmministrazione; // Tipo G_044	
	
	@ApiModelProperty(value = "Indirizzo E-mail")
	private String email;
	
	@ApiModelProperty(value = "Indirizzo PEC")
	private String pec;
	
	@ApiModelProperty(value = "Determina se sia una centrakle unica di committenza (/Tabellati/Valori?cod=SN)")
	private String iscuc;
	
	@ApiModelProperty(value = "Codice fiscale fittizio assegnato da ANAC")
	private String cfAnac;

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setCivico(String civico) {
		this.civico = civico;
	}

	public String getCivico() {
		return civico;
	}

	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = codiceIstat;
	}

	public String getCodiceIstat() {
		return codiceIstat;
	}

	public void setLocalita(String localita) {
		this.localita = localita;
	}

	public String getLocalita() {
		return localita;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setCap(String cap) {
		this.cap = cap;
	}

	public String getCap() {
		return cap;
	}

	public void setCodiceNazione(Integer codiceNazione) {
		this.codiceNazione = codiceNazione;
	}

	public Integer getCodiceNazione() {
		return codiceNazione;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getFax() {
		return fax;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setTipoAmministrazione(Long tipoAmministrazione) {
		this.tipoAmministrazione = tipoAmministrazione;
	}

	public Long getTipoAmministrazione() {
		return tipoAmministrazione;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setPec(String pec) {
		this.pec = pec;
	}

	public String getPec() {
		return pec;
	}

	public void setIscuc(String iscuc) {
		this.iscuc = iscuc;
	}

	public String getIscuc() {
		return iscuc;
	}

	public void setCfAnac(String cfAnac) {
		this.cfAnac = cfAnac;
	}

	public String getCfAnac() {
		return cfAnac;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setDataFineValidita(Date dataFineValidita) {
		this.dataFineValidita = dataFineValidita;
	}

	public Date getDataFineValidita() {
		return dataFineValidita;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getCodice() {
		return codice;
	}
	

}
