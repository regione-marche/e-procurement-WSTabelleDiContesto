package it.maggioli.eldasoft.wstabelledicontesto.dao;


import java.util.List;

import it.maggioli.eldasoft.wstabelledicontesto.vo.DatiGeneraliStazioneAppaltanteEntry;
import it.maggioli.eldasoft.wstabelledicontesto.vo.DatiUsrEinEntry;
import it.maggioli.eldasoft.wstabelledicontesto.vo.StoricoAnagraficaEntry;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * DAO Interface per l'estrazione delle informazioni relative alle stazioni
 * appaltanti mediante MyBatis.
 * 
 * @author Michele Di Napoli
 */
public interface StazioniAppaltantiMapper {

	@Insert("INSERT INTO UFFINT (CODEIN,NOMEIN,VIAEIN,NCIEIN,CITEIN,CODCIT,PROEIN,CAPEIN,CODNAZ,TELEIN,FAXEIN,CFEIN,TIPOIN,EMAIIN,EMAI2IN,ISCUC,CFANAC,CODEIN_UO) VALUES (#{codice},#{denominazione},#{indirizzo},#{civico},#{localita},#{codiceIstat},#{provincia},#{cap},#{codiceNazione},#{telefono},#{fax},#{codiceFiscale},#{tipoAmministrazione},#{email},#{pec},#{iscuc},#{cfAnac},#{codiceUnitaOrganizzativa})")
	public void insertStazioneAppaltante(
			DatiGeneraliStazioneAppaltanteEntry stazioneAppaltante);

	@Update("UPDATE UFFINT SET NOMEIN=#{denominazione}, VIAEIN=#{indirizzo}, NCIEIN=#{civico}, CITEIN=#{localita}, CODCIT=#{codiceIstat}, PROEIN=#{provincia},CAPEIN=#{cap},CODNAZ=#{codiceNazione},TELEIN=#{telefono}, FAXEIN=#{fax},CFEIN=#{codiceFiscale},TIPOIN=#{tipoAmministrazione},EMAIIN=#{email},EMAI2IN=#{pec},ISCUC=#{iscuc},CFANAC=#{cfAnac} where CODEIN=#{codice}")
	public void updateStazioneAppaltante(
			DatiGeneraliStazioneAppaltanteEntry stazioneAppaltante);
	
	@Select("<script>SELECT CODEIN from UFFINT WHERE CFEIN = #{codiceFiscale} and "
	+"<if test='codiceUnitaOrganizzativa != null'> UPPER(CODEIN_UO) = #{codiceUnitaOrganizzativa}</if>"
	+"<if test='codiceUnitaOrganizzativa == null'> CODEIN_UO is null</if></script>")
	public List<String> getCode(
			DatiGeneraliStazioneAppaltanteEntry stazioneAppaltante);
	
	@Insert("INSERT INTO USR_EIN (SYSCON,CODEIN) VALUES (#{syscon},#{codice})")
	public void insertUsrEin(
			DatiUsrEinEntry entry);
	
	@Select("SELECT CODEIN from USR_EIN WHERE SYSCON = #{syscon} and CODEIN = #{codice}" )
	public List<String> getEin(
			DatiUsrEinEntry entry);

	@Insert("INSERT INTO STO_UFFINT (ID,DATA_FINE_VALIDITA,CODEIN,NOMEIN,VIAEIN,NCIEIN,CITEIN,CODCIT,PROEIN,CAPEIN,CODNAZ,TELEIN,FAXEIN,CFEIN,TIPOIN,EMAIIN,EMAI2IN, ISCUC, CFANAC) VALUES (#{id},#{dataFineValidita},#{codice},#{denominazione},#{indirizzo},#{civico},#{localita},#{codiceIstat},#{provincia},#{cap},#{codiceNazione},#{telefono},#{fax},#{codiceFiscale},#{tipoAmministrazione},#{email},#{pec},#{iscuc},#{cfAnac})")
	public void insertStorico(
			StoricoAnagraficaEntry storico);
}
