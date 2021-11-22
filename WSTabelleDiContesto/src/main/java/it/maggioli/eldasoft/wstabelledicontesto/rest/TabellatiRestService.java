package it.maggioli.eldasoft.wstabelledicontesto.rest;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.maggioli.eldasoft.tabellati.TabellatiManager;
import it.maggioli.eldasoft.tabellati.vo.RigaResult;
import it.maggioli.eldasoft.tabellati.vo.TabellatoProvinceResult;
import it.maggioli.eldasoft.tabellati.vo.TabellatoResult;
import it.maggioli.eldasoft.wstabelledicontesto.bl.StazioniAppaltantiManager;
import it.maggioli.eldasoft.wstabelledicontesto.vo.SAResult;

/**
 * Servizio REST esposto al path "/Tabellati".
 */
@Path("/Tabellati")
@Component
@Api(value = "/Tabellati")
public class TabellatiRestService {
	/** Logger di classe. */
	private Logger logger = LoggerFactory.getLogger(TabellatiRestService.class);

	/**
	 * Wrapper della business logic a cui viene demandata la gestione
	 */
	private TabellatiManager tabellatiManager;

	/**
	 * @param tabellatiManager tabellatiManager da settare internamente alla classe.
	 */
	@Required
	@Autowired
	public void setTabellatiManager(TabellatiManager tabellatiManager) {
		this.tabellatiManager = tabellatiManager;
	}

	private StazioniAppaltantiManager stazioniAppaltantiManager;

	@Required
	@Autowired
	public void setStazioniAppaltantiManager(StazioniAppaltantiManager stazioniAppaltantiManager) {
		this.stazioniAppaltantiManager = stazioniAppaltantiManager;
	}

	/**
	 * Estrae le righe del tabellato richiesto mediante chiamata GET e risposta
	 * JSON.
	 *
	 * @param cod      codifica del tabellato
	 * @param language lingua di restituzione del del tabellato
	 * @return JSON contenente la classe TabellatoResult
	 */
	@GET
	@Path("/Valori")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(nickname = "TabellatiRestService.valori", value = "Estrae le voci del tabellato sulla base dei criteri di filtro impostati", response = TabellatoResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operazione effettuata con successo"),
			@ApiResponse(code = 404, message = "Il tabellato richiesto non � stato trovato (si veda l'attributo error della risposta)"),
			@ApiResponse(code = 500, message = "Errori durante l'esecuzione dell'operazione (si veda l'attributo error della risposta)") })
	public Response valori(
			@ApiParam(value = "codice descrittivo del tabellato richiesto", allowableValues = "Indizione,TipologiaSA,TipologiaProcedura,Area,Fase,TipoInvio,TipoAvviso,SN,TipoAppalto,CriterioAggiudicazione,TipoRealizzazione,SceltaContraente,SceltaContraente50,MotivoCompletamento,TipologiaAggiudicatario,RuoloAssociazione,TipologiaCC,Categorie,Classe,Settore,FormaGiuridica,TipoProgramma,Determinazioni,Ambito,Causa,StatoRealizzazione,DestinazioneUso,TipologiaIntervento,CategoriaIntervento,Priorita,Finalita,StatoProgettazione,TrasferimentoImmobile,ImmobileDisponibile,ProgrammaDismissione,TipoProprieta,TipologiaCapitalePrivato,TipoDisponibilita,VariatoAcquisti,VariatoInterventi,MesePrevisto,TipologiaInterventoDM112011,CategoriaInterventoDM112011,FinalitaDM112011,StatoProgettazioneDM112011,TipologiaCapitalePrivatoDM112011,UnitaMisura,AcquistoRicompreso,ProceduraAffidamento,Acquisto,Valutazione,PrestazioniComprese,ModalitaAcquisizioneForniture,TipologiaLavoro,Condizione,TipologiaAmministrazione,FunzioniDelegate", required = true) @QueryParam("cod") String cod,
			@ApiParam(value = "lingua delle voci ritornate") @DefaultValue("it") @QueryParam("language") String language) {
		if (logger.isDebugEnabled()) {
			logger.debug("valori(" + cod + "): inizio metodo");
		}
		TabellatoResult risultato = this.tabellatiManager.getValori(cod, language);
		HttpStatus status = HttpStatus.OK;
		if (risultato.getError() != null) {
			if (TabellatoResult.ERROR_NOT_FOUND.equals(risultato.getError())) {
				status = HttpStatus.NOT_FOUND;
			} else {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}
		logger.debug("valori: fine metodo [http status " + status.value() + "]");
		return Response.status(status.value()).entity(risultato).build();
	}

	/**
	 * Estrae la riga del tabellato richiesto mediante chiamata GET e risposta JSON.
	 *
	 * @param cod      codifica del tabellato
	 * @param valore   valore nel tabellato
	 * @param language lingua di restituzione del del tabellato
	 * @return JSON contenente la classe TabellatoEntry
	 */
	@GET
	@Path("/Valore")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(nickname = "TabellatiRestService.valore", value = "Estrae la voce del tabellato sulla base dei criteri di filtro impostati", response = RigaResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operazione effettuata con successo"),
			@ApiResponse(code = 400, message = "Controlli falliti sui parametri in input (si veda l'attributo error della risposta)"),
			@ApiResponse(code = 404, message = "Il tabellato richiesto non � stato trovato (si veda l'attributo error della risposta)"),
			@ApiResponse(code = 500, message = "Errori durante l'esecuzione dell'operazione (si veda l'attributo error della risposta)") })
	public Response valore(
			@ApiParam(value = "codice descrittivo del tabellato richiesto", allowableValues = "Indizione,TipologiaSA,TipologiaProcedura,Area,Fase,TipoInvio,TipoAvviso,SN,TipoAppalto,CriterioAggiudicazione,TipoRealizzazione,SceltaContraente,SceltaContraente50,MotivoCompletamento,TipologiaAggiudicatario,RuoloAssociazione,TipologiaCC,Categorie,Classe,Settore,FormaGiuridica,Entita,Stato,TipoProgramma,Determinazioni,Ambito,Causa,StatoRealizzazione,DestinazioneUso,TipologiaIntervento,CategoriaIntervento,Priorita,Finalita,StatoProgettazione,TrasferimentoImmobile,ImmobileDisponibile,ProgrammaDismissione,TipoProprieta,TipologiaCapitalePrivato,TipoDisponibilita,VariatoAcquisti,VariatoInterventi,MesePrevisto,TipologiaInterventoDM112011,CategoriaInterventoDM112011,FinalitaDM112011,StatoProgettazioneDM112011,TipologiaCapitalePrivatoDM112011,UnitaMisura,AcquistoRicompreso,ProceduraAffidamento,Acquisto,Valutazione,PrestazioniComprese,ModalitaAcquisizioneForniture,TipologiaLavoro,Condizione,TipologiaAmministrazione,FunzioniDelegate", required = true) @QueryParam("cod") String cod,
			@ApiParam(value = "codice della riga del tabellato", required = true) @QueryParam("valore") String valore,
			@ApiParam(value = "lingua delle voci ritornate") @DefaultValue("it") @QueryParam("language") String language,
			@ApiParam(value = "Token", required = true) @QueryParam("token") String token) {
		if (logger.isDebugEnabled()) {
			logger.debug("valori(" + cod + "," + valore + "): inizio metodo");
		}
		cod = StringUtils.stripToNull(cod);
		valore = StringUtils.stripToNull(valore);
		RigaResult risultato = this.tabellatiManager.getValore(cod, valore, language);
		HttpStatus status = HttpStatus.OK;
		if (risultato.getError() != null) {
			if (RigaResult.ERROR_NOT_FOUND.equals(risultato.getError())) {
				status = HttpStatus.NOT_FOUND;
			} else if (RigaResult.INVALID_VALUE.equals(risultato.getError())) {
				status = HttpStatus.BAD_REQUEST;
			} else {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}
		logger.debug("valore: fine metodo [http status " + status.value() + "]");
		return Response.status(status.value()).entity(risultato).build();
	}

	/**
	 * Estrae le regioni mediante chiamata GET e risposta JSON.
	 *
	 * @param language lingua di restituzione del del tabellato
	 * @return JSON contenente la classe TabellatoResult
	 */
	@GET
	@Path("/Regioni")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(nickname = "TabellatiRestService.regioni", value = "Estrae le regioni", response = TabellatoResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operazione effettuata con successo"),
			@ApiResponse(code = 500, message = "Errori durante l'esecuzione dell'operazione (si veda l'attributo error della risposta)") })
	public Response regioni(
			@ApiParam(value = "lingua delle voci ritornate") @DefaultValue("it") @QueryParam("language") String language) {
		if (logger.isDebugEnabled()) {
			logger.debug("regioni(" + language + "): inizio metodo");
		}
		TabellatoResult risultato = this.tabellatiManager.getRegioni(language);
		HttpStatus status = HttpStatus.OK;
		if (risultato.getError() != null) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		logger.debug("regioni: fine metodo [http status " + status.value() + "]");
		return Response.status(status.value()).entity(risultato).build();
	}

	/**
	 * Estrae le righe del tabellato richiesto mediante chiamata GET e risposta
	 * JSON.
	 *
	 * @param regione  regione
	 * @param language lingua di restituzione del del tabellato
	 * @return JSON contenente la classe TabellatoResult
	 */
	@GET
	@Path("/Province")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(nickname = "TabellatiRestService.province", value = "Estrae le province sulla base dei criteri di filtro impostati", response = TabellatoResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operazione effettuata con successo"),
			@ApiResponse(code = 404, message = "Il tabellato richiesto non � stato trovato (si veda l'attributo error della risposta)"),
			@ApiResponse(code = 500, message = "Errori durante l'esecuzione dell'operazione (si veda l'attributo error della risposta)") })
	public Response province(
			@ApiParam(value = "codice istat regione", allowableValues = "001,002,003,004,005,006,007,008,009,010,011,012,013,014,015,016,017,018,019,020") @DefaultValue("") @QueryParam("regione") String regione,
			@ApiParam(value = "lingua delle voci ritornate") @DefaultValue("it") @QueryParam("language") String language) {
		if (logger.isDebugEnabled()) {
			logger.debug("province(" + regione + "): inizio metodo");
		}
		TabellatoResult risultato = this.tabellatiManager.getProvince(regione, language);
		HttpStatus status = HttpStatus.OK;
		if (risultato.getError() != null) {
			if (TabellatoResult.ERROR_NOT_FOUND.equals(risultato.getError())) {
				status = HttpStatus.NOT_FOUND;
			} else {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}
		logger.debug("province: fine metodo [http status " + status.value() + "]");
		return Response.status(status.value()).entity(risultato).build();
	}

	@GET
	@Path("/ProvinceIstat")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(nickname = "TabellatiRestService.provinceIstat", value = "Estrae le province sulla base dei criteri di filtro impostati, includendo il codice istat", response = TabellatoProvinceResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operazione effettuata con successo"),
			@ApiResponse(code = 404, message = "Il tabellato richiesto non � stato trovato (si veda l'attributo error della risposta)"),
			@ApiResponse(code = 500, message = "Errori durante l'esecuzione dell'operazione (si veda l'attributo error della risposta)") })
	public Response provinceIstat(
			@ApiParam(value = "lingua delle voci ritornate") @DefaultValue("it") @QueryParam("language") String language) {
		if (logger.isDebugEnabled()) {
			logger.debug("provinceIstat: inizio metodo");
		}
		TabellatoProvinceResult risultato = this.tabellatiManager.getProvinceIstat(language);
		HttpStatus status = HttpStatus.OK;
		if (risultato.getError() != null) {
			if (TabellatoResult.ERROR_NOT_FOUND.equals(risultato.getError())) {
				status = HttpStatus.NOT_FOUND;
			} else {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}
		logger.debug("province: fine metodo [http status " + status.value() + "]");
		return Response.status(status.value()).entity(risultato).build();
	}

	@GET
	@Path("/Atti")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(nickname = "TabellatiRestService.atti", value = "Estrae gli atti pubblicabili", response = TabellatoResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operazione effettuata con successo"),
			@ApiResponse(code = 500, message = "Errori durante l'esecuzione dell'operazione (si veda l'attributo error della risposta)") })
	public Response atti(
			@ApiParam(value = "lingua delle voci ritornate") @DefaultValue("it") @QueryParam("language") String language) {
		if (logger.isDebugEnabled()) {
			logger.debug("atti(" + language + "): inizio metodo");
		}
		TabellatoResult risultato = this.tabellatiManager.getAtti(language);
		HttpStatus status = HttpStatus.OK;
		if (risultato.getError() != null) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		logger.debug("atti: fine metodo [http status " + status.value() + "]");
		return Response.status(status.value()).entity(risultato).build();
	}

	@GET
	@Path("/GetSAbyNameOccurrence")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operazione effettuata con successo"),
			@ApiResponse(code = 401, message = "Non sono presenti i parametri obbligatori nella request"),
			@ApiResponse(code = 500, message = "Errori durante l'esecuzione dell'operazione (si veda l'attributo error della risposta)") })
	public Response GetSAbyCFOccurrence(@QueryParam("saName") String saName) {
		if (logger.isDebugEnabled()) {
			logger.debug("inizio metodo autocomplete SA name by name");
		}
		SAResult risultato = new SAResult();
		HttpStatus status = HttpStatus.OK;
		if (saName == null || "".equals(saName)) {
			risultato.setEsito(false);
			risultato.setError("il campo saName è obbligatorio");
			status = HttpStatus.BAD_REQUEST;
		} else {
			risultato = this.stazioniAppaltantiManager.getSAbyName(saName);

			if (risultato.isEsito() == false) {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}
		logger.debug("fine metodo [http status " + status.value() + "]");
		return Response.status(status.value()).entity(risultato).build();
	}

	@GET
	@Path("/GetSAbyCFOccurrence")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Operazione effettuata con successo"),
			@ApiResponse(code = 401, message = "Non sono presenti i parametri obbligatori nella request"),
			@ApiResponse(code = 500, message = "Errori durante l'esecuzione dell'operazione (si veda l'attributo error della risposta)") })
	public Response GetSAbyNameOccurrence(@QueryParam("saCf") String saCf) {
		if (logger.isDebugEnabled()) {
			logger.debug("inizio metodo autocomplete SA name by CF");
		}
		SAResult risultato = new SAResult();
		HttpStatus status = HttpStatus.OK;
		if (saCf == null || "".equals(saCf)) {
			risultato.setEsito(false);
			risultato.setError("il campo saCf è obbligatorio");
			status = HttpStatus.BAD_REQUEST;
		} else {

			risultato = this.stazioniAppaltantiManager.getSAbyCF(saCf);		
			if (risultato.isEsito() == false) {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}
		logger.debug("fine metodo [http status " + status.value() + "]");
		return Response.status(status.value()).entity(risultato).build();
	}

	/**
	 * Estrae i dati per la costruzione dell'albero cvp richiesto mediante chiamata
	 * GET e risposta JSON.
	 *
	 * @param regione  regione
	 * @param language lingua di restituzione del del tabellato
	 * @return JSON contenente la classe TabellatoResult
	 */
	/*
	 * @GET
	 * 
	 * @Path("/AlberoCpv")
	 * 
	 * @Produces(MediaType.APPLICATION_JSON)
	 * 
	 * @ApiOperation(nickname = "TabellatiRestService.province", value =
	 * "Estrae le province sulla base dei criteri di filtro impostati", response =
	 * TabellatoResult.class)
	 * 
	 * @ApiResponses(value = {@ApiResponse(code = 200, message =
	 * "Operazione effettuata con successo"),
	 * 
	 * @ApiResponse(code = 400, message =
	 * "Controlli falliti sui parametri in input (si veda l'attributo error della risposta)"
	 * ),
	 * 
	 * @ApiResponse(code = 500, message =
	 * "Errori durante l'esecuzione dell'operazione (si veda l'attributo error della risposta)"
	 * ) }) public Response alberoCpv(
	 * 
	 * @QueryParam("cpvvpgrpdiv") String cpvvpgrpdiv,
	 * 
	 * @QueryParam("livello") String livello,
	 * 
	 * @QueryParam("id") String id,
	 * 
	 * @QueryParam("cpvvpcod") String cpvvpcod,
	 * 
	 * @QueryParam("textsearch") String textsearch,
	 * 
	 * @ApiParam(value =
	 * "lingua delle voci ritornate") @DefaultValue("it") @QueryParam("language")
	 * String language) { if (logger.isDebugEnabled()) { logger.debug("alberoCpv(" +
	 * cpvvpgrpdiv + "," + livello + "," + id + "," + cpvvpcod + "," + textsearch +
	 * "): inizio metodo"); } TabellatoResult risultato =
	 * this.tabellatiManager.getProvince(regione, language); HttpStatus status =
	 * HttpStatus.OK; if (risultato.getError() != null) { if
	 * (TabellatoResult.ERROR_NOT_FOUND.equals(risultato.getError())) { status =
	 * HttpStatus.NOT_FOUND; } else { status = HttpStatus.INTERNAL_SERVER_ERROR; } }
	 * logger.debug("alberoCpv: fine metodo [http status " + status.value() + "]");
	 * return Response.status(status.value()).entity(risultato).build(); }
	 */
}
