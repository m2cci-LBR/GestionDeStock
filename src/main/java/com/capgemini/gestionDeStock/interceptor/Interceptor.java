package com.capgemini.gestionDeStock.interceptor;

import org.hibernate.EmptyInterceptor;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.Locale;

public class Interceptor extends EmptyInterceptor {

    @Override
    public String onPrepareStatement(String sql) {
        if (StringUtils.hasLength(sql) && sql.toLowerCase().startsWith("select")){
            // select utilisateu0_.
            final String entityName = sql.substring(7,sql.indexOf(".")); // renvoie utilisateu0_ par exemple
            final String idEntreprise = MDC.get("idEntreprise");
            if (StringUtils.hasLength(entityName)
                        && !entityName.toLowerCase().contains("entreprise") // Car dans roles et entreprises ont a pas idEntreprise
                        && !entityName.toLowerCase().contains("roles")
                        && StringUtils.hasLength(idEntreprise)) {

                if (sql.contains("where")){
                    sql = sql + " and " + entityName + ".identreprise = " + idEntreprise;
                } else {
                    sql = sql + " where " + entityName + ".identreprise = " + idEntreprise;
                }
            }
        }
        return super.onPrepareStatement(sql);
    }
}
