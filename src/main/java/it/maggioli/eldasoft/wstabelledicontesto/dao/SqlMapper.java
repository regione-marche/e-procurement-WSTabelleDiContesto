package it.maggioli.eldasoft.wstabelledicontesto.dao;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import it.maggioli.eldasoft.wstabelledicontesto.vo.SaInfo;

public interface SqlMapper {
	
	
    class PureSqlProvider {
        public String sql(String sql) {
            return sql;
        }

        public String sqlObject(String sql) {
            return sql;
        }
        
        public String count(String from) {
            return "SELECT count(*) FROM " + from;
        }
    }

    
    @SelectProvider(type = PureSqlProvider.class, method = "sql")
    public List<Map<String,Object>> select(String sql);

    @SelectProvider(type = PureSqlProvider.class, method = "count")
    public Integer count(String from);

    @SelectProvider(type = PureSqlProvider.class, method = "sql")
    public Integer execute(String query);

    @SelectProvider(type = PureSqlProvider.class, method = "sqlObject")
    public String executeReturnString(String query);
    
	@Select("select valore from w_config where UPPER(codapp) = #{codapp} AND chiave = #{chiave}")
	public String getCipherKey(@Param("codapp") String codapp, @Param("chiave") String chiave);
	
	@Select("select cfein as saCFein,nomein as saNomein from uffint where UPPER(cfein) like #{saCf}")
	public List<SaInfo> getSAlikeCf(@Param("saCf") String saCf);
	
	@Select("select cfein as saCFein,nomein as saNomein from uffint where UPPER(nomein) like #{saNomein}")
	public List<SaInfo> getSAlikeName(@Param("saNomein") String saNomein);

}
