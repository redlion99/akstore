package me.smartco.akstore.transaction.spring;

import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

/**
 * Created by libin on 15-1-9.
 */
public class DDLExport implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
