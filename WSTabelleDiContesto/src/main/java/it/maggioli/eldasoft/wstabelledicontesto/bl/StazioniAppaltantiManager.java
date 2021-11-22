package it.maggioli.eldasoft.wstabelledicontesto.bl;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.eldasoft.utils.sicurezza.CriptazioneException;
import it.eldasoft.utils.sicurezza.FactoryCriptazioneByte;
import it.eldasoft.utils.sicurezza.ICriptazioneByte;
import it.eldasoft.utils.utility.UtilityFiscali;
import it.maggioli.eldasoft.wstabelledicontesto.dao.SqlMapper;
import it.maggioli.eldasoft.wstabelledicontesto.dao.StazioniAppaltantiMapper;
import it.maggioli.eldasoft.wstabelledicontesto.vo.DatiGeneraliStazioneAppaltanteEntry;
import it.maggioli.eldasoft.wstabelledicontesto.vo.DatiUsrEinEntry;
import it.maggioli.eldasoft.wstabelledicontesto.vo.SAResult;
import it.maggioli.eldasoft.wstabelledicontesto.vo.SaInfo;
import it.maggioli.eldasoft.wstabelledicontesto.vo.StoricoAnagraficaEntry;
import it.maggioli.eldasoft.wstabelledicontesto.vo.TokenValidationResult;
import it.maggioli.eldasoft.wstabelledicontesto.vo.ValidateEntry;

@Component(value = "StazioniAppaltantiManager")
public class StazioniAppaltantiManager {

	private static final String CONFIG_CODICE_APP = "W9";
	private static final String CONFIG_CHIAVE_APP = "it.maggioli.eldasoft.wslogin.jwtKey";

	private Logger logger = LoggerFactory
			.getLogger(StazioniAppaltantiManager.class);

	@Autowired
	private StazioniAppaltantiMapper stazioniAppaltantiMapper;

	@Autowired
	private SqlMapper sqlMapper;

	public void setStazioniAppaltantiMapper(
			StazioniAppaltantiMapper stazioniAppaltantiMapper) {
		this.stazioniAppaltantiMapper = stazioniAppaltantiMapper;
	}

	public void setSqlMapper(SqlMapper sqlMapper) {
		this.sqlMapper = sqlMapper;
	}

	public String getJWTKey() throws CriptazioneException {

		String criptedKey = this.sqlMapper.getCipherKey(CONFIG_CODICE_APP,
				CONFIG_CHIAVE_APP);
		try {
			ICriptazioneByte decript = FactoryCriptazioneByte.getInstance(
					FactoryCriptazioneByte.CODICE_CRIPTAZIONE_LEGACY,
					criptedKey.getBytes(),
					ICriptazioneByte.FORMATO_DATO_CIFRATO);

			return new String(decript.getDatoNonCifrato());
		} catch (CriptazioneException e) {
			logger.error(
					"Errore in fase di decrypt della chiave hash per generazione token",
					e);
			throw e;
		}
	}

	public void pubblica(DatiGeneraliStazioneAppaltanteEntry entry, TokenValidationResult tokenValidation,
			List<ValidateEntry> controlli, boolean pubblicaStorico) throws Exception {

		List<String> codiceList = this.stazioniAppaltantiMapper.getCode(entry);
		String codice = null;
		DatiUsrEinEntry usrEinEntry = new DatiUsrEinEntry();
		usrEinEntry.setSyscon(Integer.parseInt(tokenValidation.getSyscon()));
		if (codiceList == null || codiceList.size() == 0) {
			if (tokenValidation.isCanEdit()) {
				codice = this.calcolaCodificaAutomatica("UFFINT", "CODEIN");
				entry.setCodice(codice);
				usrEinEntry.setCodice(codice);
				this.stazioniAppaltantiMapper.insertStazioneAppaltante(entry);
				//inserimento storico
				if(pubblicaStorico) {
					try {
						if (entry.getStorico() != null) {
							for(StoricoAnagraficaEntry storico:entry.getStorico()) {
								Integer i = this.sqlMapper.execute("SELECT MAX(ID) FROM STO_UFFINT");
								Long id = new Long(1);
								if (i != null) {
									id = new Long(i) + 1;
								}
								storico.setId(id);
								storico.setCodice(codice);
								this.stazioniAppaltantiMapper.insertStorico(storico);
							}
						}
					} catch(Exception ex) {
						logger.error("Errore durante l'inserimento dello storico anagrafica", ex);
					}
				}
				
				this.stazioniAppaltantiMapper.insertUsrEin(usrEinEntry); 
			} else {
				ValidateEntry item = new ValidateEntry("Stazione appaltante", tokenValidation.getError());
				controlli.add(item);
			}
		} else if (codiceList != null && codiceList.size() > 1) {
			ValidateEntry item = new ValidateEntry("Stazione appaltante multipla",
					"Il codice fiscale e l'unita' organizzativa della Stazione Appaltante sono duplicati");
			controlli.add(item);
		} else {
			//prima di procedere alla modifica devo verificare se
			//la SA appartiene all'utente oppure se l'utente e' "Amministrazione parametri di sistema" 
			entry.setCodice(codiceList.get(0));
			usrEinEntry.setCodice(codiceList.get(0));
			int usrEinExist = this.stazioniAppaltantiMapper.getEin(usrEinEntry).size();
			int isSuperUser = sqlMapper.count("USRSYS WHERE SYSCON = " + tokenValidation.getSyscon() + " AND (syspwbou LIKE '%ou89|%' OR syspwbou NOT LIKE '%ou214|%')");
			if (usrEinExist > 0 || isSuperUser > 0) {
				//proseguo con l'aggiornamento
				if (tokenValidation.isCanEdit()) {
					this.stazioniAppaltantiMapper.updateStazioneAppaltante(entry);
					if(pubblicaStorico) {
						//aggiornamento storico
						try {
							if (entry.getStorico() != null) {
								Long id = new Long(1);
								Integer i = null;
								Long lastId = null;
								for(StoricoAnagraficaEntry storico:entry.getStorico()) {
									if (lastId == null) {
										i = this.sqlMapper.execute("SELECT MIN(ID) FROM STO_UFFINT WHERE CODEIN='" + entry.getCodice() + "'");
									} else {
										i = this.sqlMapper.execute("SELECT MIN(ID) FROM STO_UFFINT WHERE CODEIN='" + entry.getCodice() + "' AND ID>" + lastId);
									}
									if (i == null) {
										i = this.sqlMapper.execute("SELECT MAX(ID) FROM STO_UFFINT");
										if (i != null) {
											id = new Long(i) + 1;
										}
									} else {
										this.sqlMapper.execute("DELETE FROM STO_UFFINT WHERE ID=" + i);
										id = new Long(i);
									}
									storico.setId(id);
									storico.setCodice(entry.getCodice());
									this.stazioniAppaltantiMapper.insertStorico(storico);
									lastId = id;
								}
								if (lastId != null) {
									this.sqlMapper.execute("DELETE FROM STO_UFFINT WHERE CODEIN='" + entry.getCodice() + "' AND ID>" + lastId);
								} else {
									this.sqlMapper.execute("DELETE FROM STO_UFFINT WHERE CODEIN='" + entry.getCodice() + "'");
								}
							}
						} catch(Exception ex) {
							logger.error("Errore durante l'aggiornamento dello storico anagrafica", ex);
						}
					}
				}
				if (usrEinExist == 0) {
					this.stazioniAppaltantiMapper.insertUsrEin(usrEinEntry); 
				}
			} else {
				//errore non si possiedono i diritti per questa SA
				ValidateEntry item = new ValidateEntry("Stazione appaltante",
				"non si possiedono le credenziali per questa Stazione Appaltante");
				controlli.add(item);
			}
		}
	}

	public void validatePubblicaSA(
			DatiGeneraliStazioneAppaltanteEntry stazioneAppaltante,
			List<ValidateEntry> controlli) throws IOException {

		if (stazioneAppaltante.getDenominazione() == null) {
			ValidateEntry item = new ValidateEntry(
					"stazioneAppaltante.denominazione", "Campo obbligatorio");
			controlli.add(item);
		} else if (stazioneAppaltante.getDenominazione().length() > 254) {
			ValidateEntry item = new ValidateEntry(
					"stazioneAppaltante.denominazione",
					"Il numero di caratteri eccede la lunghezza massima");
			controlli.add(item);
		}
		if (stazioneAppaltante.getCodiceFiscale() == null) {
			ValidateEntry item = new ValidateEntry(
					"stazioneAppaltante.codiceFiscale", "Campo obbligatorio");
			controlli.add(item);
		} else if (
				!(stazioneAppaltante.getCodiceFiscale().toUpperCase().startsWith("CFAVCP-") || 
						UtilityFiscali.isValidCodiceFiscale(stazioneAppaltante.getCodiceFiscale()) || 
						UtilityFiscali.isValidPartitaIVA(stazioneAppaltante.getCodiceFiscale()))) {
			ValidateEntry item = new ValidateEntry(
					"stazioneAppaltante.codiceFiscale",
					"Il dato non e' un codice fiscale valido");
			controlli.add(item);
		}
		if (stazioneAppaltante.getCodiceIstat() != null) {
			int i = sqlMapper.count("TABSCHE WHERE TABCOD='S2003' AND TABCOD1='09' AND TABCOD3='" + stazioneAppaltante.getCodiceIstat() + "'");
			if (i == 0){
				ValidateEntry item = new ValidateEntry("stazioneAppaltante.codiceIstat", "Valore non valido");
				controlli.add(item);
			} 
		}
		if (stazioneAppaltante.getCodiceUnitaOrganizzativa() != null && stazioneAppaltante.getCodiceUnitaOrganizzativa().length()>20) {
			ValidateEntry item = new ValidateEntry("codiceUnitaOrganizzativa", "Il numero di caratteri eccede la lunghezza massima");
			controlli.add(item);
		}
		//validazione storico anagrafica
		int i = 1;
		for(StoricoAnagraficaEntry storico:stazioneAppaltante.getStorico()) {
			this.validateStoricoAnagrafica(storico, "storico" + i, controlli);
			i++;
		}
	}

	private void validateStoricoAnagrafica(StoricoAnagraficaEntry storico, String riferimento, List<ValidateEntry> controlli) throws IOException {
		if (storico.getDenominazione() != null && storico.getDenominazione().length() > 254) {
			ValidateEntry item = new ValidateEntry(
					riferimento + ".denominazione",
					"Il numero di caratteri eccede la lunghezza massima");
			controlli.add(item);
		}
		/*if (storico.getCodiceFiscale() != null && 
				!(storico.getCodiceFiscale().toUpperCase().startsWith("CFAVCP-") || 
						UtilityFiscali.isValidCodiceFiscale(storico.getCodiceFiscale()) || 
						UtilityFiscali.isValidPartitaIVA(storico.getCodiceFiscale()))) {
			ValidateEntry item = new ValidateEntry(
					riferimento + ".codiceFiscale",
					"Il dato non e' un codice fiscale valido");
			controlli.add(item);
		}
		if (storico.getCodiceIstat() != null) {
			int i = sqlMapper.count("TABSCHE WHERE TABCOD='S2003' AND TABCOD1='09' AND TABCOD3='" + storico.getCodiceIstat() + "'");
			if (i == 0){
				ValidateEntry item = new ValidateEntry(riferimento + ".codiceIstat", "Valore non valido");
				controlli.add(item);
			} 
		}*/
	}
	
	public String calcolaCodificaAutomatica(String entita, String campoChiave) throws Exception {
		String codice = "1";
		String formatoCodice = null;
		String codcal = null;
		Long cont = null;
		try {
			String query = "select CODCAL, CONTAT from G_CONFCOD where NOMENT = '" + entita + "'";
			List<Map<String,Object>> confcod = sqlMapper.select(query);
			if (confcod!= null && confcod.size() > 0) {
				for(Map<String,Object> row:confcod) {
					if (row.containsKey("CODCAL")) {
						codcal = row.get("CODCAL").toString();
					} else {
						codcal = row.get("codcal").toString();
					}
					if (row.containsKey("CONTAT")) {
						cont = new Long(row.get("CONTAT").toString());
					} else {
						cont = new Long(row.get("contat").toString());
					}
					break;
				}
				boolean codiceUnivoco = false;
				int numeroTentativi = 0;
				StringBuffer strBuffer = null;
				long tmpContatore = cont.longValue();
				while (!codiceUnivoco
						&& numeroTentativi < 10) {
					strBuffer = new StringBuffer("");
					// Come prima cosa eseguo l'update del contatore
					tmpContatore++;
					sqlMapper.execute("update G_CONFCOD set contat = " + tmpContatore + " where NOMENT = '" + entita + "'");

					strBuffer = new StringBuffer("");
					formatoCodice = codcal;
					while (formatoCodice.length() > 0) {
						switch (formatoCodice.charAt(0)) {
						case '<': // Si tratta di un'espressione numerica
							String strNum = formatoCodice.substring(1, formatoCodice.indexOf('>'));
							if (strNum.charAt(0) == '0') {
								// Giustificato a destra
								for (int i = 0; i < (strNum.length() - String.valueOf(tmpContatore).length()); i++)
									strBuffer.append('0');
							}
							strBuffer.append(String.valueOf(tmpContatore));

							formatoCodice = formatoCodice.substring(formatoCodice.indexOf('>') + 1);
							break;
						case '"': // Si tratta di una parte costante
							strBuffer.append(formatoCodice.substring(1, formatoCodice.indexOf('"', 1)));
							formatoCodice = formatoCodice.substring(formatoCodice.indexOf('"', 1) + 1);
							break;
						}
					}
					int occorrenze = sqlMapper.count(entita + " WHERE " + campoChiave + " ='" + strBuffer.toString() + "'");
					if (occorrenze == 0) {
						codiceUnivoco = true;
						codice = strBuffer.toString();
					}
					else {
						numeroTentativi++;
					}
				}
				if (!codiceUnivoco) {
					logger.error("numeroTentativi esaurito durante il calcolo per la codifica automatica " + entita);
					throw new Exception("numeroTentativi esaurito durante il calcolo per la codifica automatica " + entita);
				}
			}
		} catch (Exception ex) {
			logger.error("Errore inaspettato durante il calcolo per la codifica automatica " + entita, ex);
			throw new Exception(ex);
		}
		return codice;
	}

	
	
	public SAResult getSAbyCF(String saCf) {
		SAResult result=new SAResult();
		List<SaInfo> saInfo;
		try {
			
			saCf="%"+saCf.toUpperCase()+"%";		
			saInfo = this.sqlMapper.getSAlikeCf(saCf);
			result.setData(saInfo);
			result.setEsito(true);
		}catch(Exception e) {
			result.setEsito(false);
			result.setError("Attenzione si sono verificati alcuni errori");
		}
		return result;
	}

	public SAResult getSAbyName(String saName) {
		SAResult result=new SAResult();
		List<SaInfo> saInfo;
		try {
			
			saName="%"+saName.toUpperCase()+"%";		
			saInfo = this.sqlMapper.getSAlikeName(saName);			
			result.setData(saInfo);
			result.setEsito(true);
		}catch(Exception e) {
			result.setEsito(false);
			result.setError("Attenzione si sono verificati alcuni errori");
		}
		return result;
	}
	
}
