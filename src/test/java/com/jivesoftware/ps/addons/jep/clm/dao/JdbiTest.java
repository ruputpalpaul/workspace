package com.jivesoftware.ps.addons.jep.clm.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.postgresql.ds.PGPoolingDataSource;
import org.skife.jdbi.v2.DBI;
import com.jivesoftware.ps.addons.jep.clm.dao.impl.WorkflowDaoImpl;
import com.jivesoftware.ps.addons.jep.clm.domain.Workflow;
import com.jivesoftware.ps.addons.jep.clm.domain.Action;
import com.jivesoftware.ps.addons.jep.clm.domain.ContentType;
import com.jivesoftware.ps.addons.jep.clm.domain.Recipient;
import com.jivesoftware.ps.addons.jep.clm.domain.Reviewer;
import com.jivesoftware.ps.addons.jep.clm.domain.Rule;
import com.jivesoftware.ps.addons.jep.clm.domain.Trigger;
import com.jivesoftware.ps.addons.jep.clm.domain.Place;
import com.jivesoftware.ps.addons.jep.clm.domain.Notification;

public class JdbiTest {
    private final DBI dbi;
    
    public JdbiTest() {
        PGPoolingDataSource source = new PGPoolingDataSource();
        source.setDataSourceName("jdbc:postgresql");
        source.setServerName("localhost");
        source.setDatabaseName("test");
        source.setUser("postgres");
        source.setPassword("Hiy@1329");
        
        dbi = new DBI(source);
    }

//    @Test
//    public void testGetWorkflow() {
//        WorkflowDaoImpl dao = new WorkflowDaoImpl(dbi);
//        
//        final Workflow wf = dao.getById(1001);
//        System.out.println(wf);
//        
//    }
    
    @Test
    public void testSaveWorkflow() {
    	WorkflowDaoImpl dao = new WorkflowDaoImpl(dbi);
    	
    	Rule rule = new Rule(
    			0,
    			0,
    			Collections.emptyList(),
    			Collections.emptyList(),
    			1234,
    			100100100,
    			"test",
    			100100100,
    			"test"
    		);
    	List <Rule> ruleList = new ArrayList<Rule>();
    	ruleList.add(rule);
    	Workflow workflow = new Workflow(
    		0,
    		ruleList,
    		Collections.emptyList(),
    		Collections.emptyList(),
    		Collections.emptyList(),
    		"test",
    		"test",
    		100100100,
    		"test",
    		100100100,
    		"test",
    		"test"
    	);
    	
    	dao.saveWorkflow(workflow);
    }
}
