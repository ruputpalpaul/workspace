package com.jivesoftware.ps.addons.jep.clm.dao;

import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGPoolingDataSource;
import org.skife.jdbi.v2.DBI;
import com.jivesoftware.ps.addons.jep.clm.dao.impl.WorkflowDaoImpl;
import com.jivesoftware.ps.addons.jep.clm.domain.Workflow;

public class JdbiTest {
    private final DBI dbi;
    
    public JdbiTest() {
        PGPoolingDataSource source = new PGPoolingDataSource();
        source.setDataSourceName("jdbc:postgresql");
        source.setServerName("localhost");
        source.setDatabaseName("clm-addon");
        source.setUser("postgres");
        source.setPassword("postgres");
        
        dbi = new DBI(source);
    }

    @Test
    public void test() {
        WorkflowDaoImpl dao = new WorkflowDaoImpl(dbi);
        
        final Workflow wf = dao.getById(1215);
        System.out.println(wf);
        
    }

}
