package it.maggioli.eldasoft.wstabelledicontesto.vo;
/*
 * Copyright (c) Maggioli S.p.A.
 * Tutti i diritti sono riservati.
 *
 * Questo codice sorgente e' materiale confidenziale di proprieta' di Maggioli S.p.A.
 * In quanto tale non puo' essere distribuito liberamente ne' utilizzato a meno di
 * aver prima formalizzato un accordo specifico con Maggioli.
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;

/**
 * Dati generali di una stazione appaltante.
 * 
 * @author Alessandro.Sernagiotto
 */
@XmlRootElement
@XmlType(propOrder = {  "denominazione", "indirizzo", "civico", "codiceIstat", "localita",
		"provincia", "cap", "codiceNazione", "telefono", "fax", "codiceFiscale", "tipoAmministrazione",
		"email", "pec", "iscuc", "cfAnac", "storico" })
@Alias("datigeneralistazioneappaltanteentry")
@ApiModel(description = "Contenitore per i dati generali della stazione appaltante")
public class DatiGeneraliStazioneAppaltanteEntry implements Serializable {
	/**
	 * UID.
	 */
	private static final long serialVersionUID = -6600269573839884401L;
	@ApiModelProperty(hidden = true)
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
	
	@ApiModelProperty(value = "Codice Unità Organizzativa")
	@Size(max=20)
	private String codiceUnitaOrganizzativa;
	
	@ApiModelProperty(value="Storico anagrafica")
	@Size(min=0)
	private List<StoricoAnagraficaEntry> storico = new ArrayList<StoricoAnagraficaEntry>();
	  
	@XmlTransient
	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = StringUtils.stripToNull(codice);
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = StringUtils.stripToNull(denominazione);
	}

	public String getCodiceIstat() {
		return codiceIstat;
	}
	
	public void setCodiceIstat(String codiceIstat) {
		this.codiceIstat = StringUtils.stripToNull(codiceIstat);
	}
	
	public Integer getCodiceNazione() {
		return codiceNazione;
	}
	
	public void setCodiceNazione(Integer codiceNazione) {
		this.codiceNazione = codiceNazione;
	}

	public String getIndirizzo() {
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo) {
		this.indirizzo = StringUtils.stripToNull(indirizzo);
	}

	public String getCivico() {
		return civico;
	}

	public void setCivico(String civico) {
		this.civico = StringUtils.stripToNull(civico);
	}

	public String getLocalita() {
		return localita;
	}

	public void setLocalita(String localita) {
		this.localita = StringUtils.stripToNull(localita);
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = StringUtils.stripToNull(provincia);
	}

	public String getCap() {
		return cap;
	}

	public void setCap(String cap) {
		this.cap = StringUtils.stripToNull(cap);
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = StringUtils.stripToNull(telefono);
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = StringUtils.stripToNull(fax);
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = StringUtils.stripToNull(codiceFiscale);
	}

	public Long getTipoAmministrazione() {
		return tipoAmministrazione;
	}

	public void setTipoAmministrazione(Long tipoAmministrazione) {
		this.tipoAmministrazione = tipoAmministrazione;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = StringUtils.stripToNull(email);
	}

	public String getPec() {
		return pec;
	}

	public void setPec(String pec) {
		this.pec = StringUtils.stripToNull(pec);
	}
	
	public String getIscuc() {
		return iscuc;
	}

	public void setIscuc(String iscuc) {
		this.iscuc = StringUtils.stripToNull(iscuc);
	}

	public String getCfAnac() {
		return cfAnac;
	}

	public void setCfAnac(String cfAnac) {
		this.cfAnac = StringUtils.stripToNull(cfAnac);
	}

	public void setStorico(List<StoricoAnagraficaEntry> storico) {
		this.storico = storico;
	}

	public List<StoricoAnagraficaEntry> getStorico() {
		return storico;
	}

	public void setCodiceUnitaOrganizzativa(String codiceUnitaOrganizzativa) {
		this.codiceUnitaOrganizzativa = StringUtils.stripToNull(codiceUnitaOrganizzativa);
	}

	public String getCodiceUnitaOrganizzativa() {
		if (codiceUnitaOrganizzativa != null) {
			return codiceUnitaOrganizzativa.toUpperCase();
		} else
		return null;
	}
	
}

