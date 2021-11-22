package it.maggioli.eldasoft.wstabelledicontesto.rest;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import it.maggioli.eldasoft.wstabelledicontesto.bl.StazioniAppaltantiManager;
import it.maggioli.eldasoft.wstabelledicontesto.vo.DatiGeneraliStazioneAppaltanteEntry;
import it.maggioli.eldasoft.wstabelledicontesto.vo.InserimentoResult;
import it.maggioli.eldasoft.wstabelledicontesto.vo.TokenValidationResult;
import it.maggioli.eldasoft.wstabelledicontesto.vo.ValidateEntry;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Path("/StazioniAppaltanti")
@Component
@Api(value = "/StazioniAppaltanti")
public class StazioniAppaltantiRestService {

	private StazioniAppaltantiManager stazioniAppaltantiManager;

	/** Logger di classe. */
	protected Logger logger = LoggerFactory
			.getLogger(StazioniAppaltantiRestService.class);

	@Required
	@Autowired
	public void setStazioniAppaltantiManager(
			StazioniAppaltantiManager stazioniAppaltantiManager) {
		this.stazioniAppaltantiManager = stazioniAppaltantiManager;
	}

	@POST
	@Path("/Pubblica")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(nickname = "StazioniAppaltanti.pubblica", value = "Inserisce o modifica i dati relativi alle stazioni appaltanti", response = InserimentoResult.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione effettuata con successo"),
			@ApiResponse(code = 400, message = "Controlli falliti sui parametri in input (si veda l'attributo error della risposta)"),
			@ApiResponse(code = 500, message = "Errori durante l'esecuzione dell'operazione (si veda l'attributo error della risposta)") })
	public Response pubblica(
			@ApiParam(value = "Stazioni appaltanti da pubblicare [Model=DatiGeneraliStazioneAppaltanteEntry]", required = true) DatiGeneraliStazioneAppaltanteEntry stazione,
			@ApiParam(value = "Token", required = true) @QueryParam("token") String token)
			throws ParseException, IOException {

		InserimentoResult risultato = null;

		TokenValidationResult tokenValidate = validateToken(token);
		if (!tokenValidate.isValidated()) {
			risultato = new InserimentoResult();
			risultato.setError(tokenValidate.getError());
			return Response.status(HttpStatus.UNAUTHORIZED.value())
					.entity(risultato).build();
		}

		// FASE PRELIMINARE DI CONTROLLO DEI PARAMETRI PRIMA DI INOLTRARE ALLA
		// BUSINESS LOGIC
		if (logger.isDebugEnabled()) {
			logger.debug("pubblica CF stazione(" + stazione.getCodiceFiscale()
					+ "," + token + "): inizio metodo");
		}

		List<ValidateEntry> controlli = new ArrayList<ValidateEntry>();

		this.stazioniAppaltantiManager.validatePubblicaSA(stazione, controlli);
		if (controlli.size() > 0) {
			risultato = new InserimentoResult();
			risultato.setError(InserimentoResult.ERROR_VALIDATION);
			risultato.setValidate(controlli);
		} else {
			// procedo con l'inserimento
			try {
				this.stazioniAppaltantiManager.pubblica(stazione, tokenValidate, controlli, false);
				if (controlli.size() > 0) {
					risultato = new InserimentoResult();
					risultato.setError(InserimentoResult.ERROR_VALIDATION);
					risultato.setValidate(controlli);
				} else {
					risultato = new InserimentoResult();
				}
				
			} catch (Exception ex) {
				logger.error(
						"Errore inaspettato durante la pubblicazione della stazione",
						ex);
				risultato = new InserimentoResult();
				risultato.setError(InserimentoResult.ERROR_UNEXPECTED + ": "
						+ ex.getMessage());
			}
		}
		HttpStatus status = HttpStatus.OK;
		if (risultato != null && risultato.getError() != null) {
			if (InserimentoResult.ERROR_VALIDATION.equals(risultato.getError())) {
				status = HttpStatus.BAD_REQUEST;
			} else {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}

		logger.debug("pubblica: fine metodo [http status " + status.value()
				+ "]");
		return Response.status(status.value()).entity(risultato).build();
	}

	@POST
	@Path("/PubblicaConStorico")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(nickname = "StazioniAppaltanti.pubblicaConStorico", value = "Inserisce o modifica i dati relativi alle stazioni appaltanti", response = InserimentoResult.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Operazione effettuata con successo"),
			@ApiResponse(code = 400, message = "Controlli falliti sui parametri in input (si veda l'attributo error della risposta)"),
			@ApiResponse(code = 500, message = "Errori durante l'esecuzione dell'operazione (si veda l'attributo error della risposta)") })
	public Response pubblicaConStorico(
			@ApiParam(value = "Stazioni appaltanti da pubblicare [Model=DatiGeneraliStazioneAppaltanteEntry]", required = true) DatiGeneraliStazioneAppaltanteEntry stazione,
			@ApiParam(value = "Token", required = true) @QueryParam("token") String token)
			throws ParseException, IOException {

		InserimentoResult risultato = null;

		TokenValidationResult tokenValidate = validateToken(token);
		if (!tokenValidate.isValidated()) {
			risultato = new InserimentoResult();
			risultato.setError(tokenValidate.getError());
			return Response.status(HttpStatus.UNAUTHORIZED.value())
					.entity(risultato).build();
		}

		// FASE PRELIMINARE DI CONTROLLO DEI PARAMETRI PRIMA DI INOLTRARE ALLA
		// BUSINESS LOGIC
		if (logger.isDebugEnabled()) {
			logger.debug("pubblica con storico CF stazione(" + stazione.getCodiceFiscale()
					+ "," + token + "): inizio metodo");
		}

		List<ValidateEntry> controlli = new ArrayList<ValidateEntry>();

		this.stazioniAppaltantiManager.validatePubblicaSA(stazione, controlli);
		if (controlli.size() > 0) {
			risultato = new InserimentoResult();
			risultato.setError(InserimentoResult.ERROR_VALIDATION);
			risultato.setValidate(controlli);
		} else {
			// procedo con l'inserimento
			try {
				this.stazioniAppaltantiManager.pubblica(stazione, tokenValidate, controlli, true);
				if (controlli.size() > 0) {
					risultato = new InserimentoResult();
					risultato.setError(InserimentoResult.ERROR_VALIDATION);
					risultato.setValidate(controlli);
				} else {
					risultato = new InserimentoResult();
				}
				
			} catch (Exception ex) {
				logger.error(
						"Errore inaspettato durante la pubblicazione della stazione",
						ex);
				risultato = new InserimentoResult();
				risultato.setError(InserimentoResult.ERROR_UNEXPECTED + ": "
						+ ex.getMessage());
			}
		}
		HttpStatus status = HttpStatus.OK;
		if (risultato != null && risultato.getError() != null) {
			if (InserimentoResult.ERROR_VALIDATION.equals(risultato.getError())) {
				status = HttpStatus.BAD_REQUEST;
			} else {
				status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
		}

		logger.debug("pubblicaConStorico: fine metodo [http status " + status.value()
				+ "]");
		return Response.status(status.value()).entity(risultato).build();
	}
	
	public TokenValidationResult validateToken(String token) {
		TokenValidationResult result = new TokenValidationResult();
		try {
			String jwtKey = "";
			jwtKey = this.stazioniAppaltantiManager.getJWTKey();
			Jws<Claims> claims = Jwts.parser().setSigningKey(jwtKey)
					.parseClaimsJws(token);
			boolean canEdit = new Boolean(claims.getBody().get("modificaArchivioStazioneAppaltante").toString());
			if(!canEdit){
				result.setError("Non si possiede il permesso di creazione/modifica della stazione appaltante.");
			}
			String clientId = claims.getBody().get("aud").toString();
			String syscon = claims.getBody().get("syscon").toString();
			result.setCanEdit(canEdit);
			result.setClientId(clientId);
			result.setValidated(true);
			result.setSyscon(syscon);
			return result;
		} catch (Exception e) {
			result.setValidated(false);
			result.setError("Il token non è valido.");
			return result;
		}
	}

}
