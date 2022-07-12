package com.jivesoftware.ps.addons.jep.clm.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGPoolingDataSource;
import org.skife.jdbi.v2.DBI;

import com.jivesoftware.ps.addons.jep.clm.dao.impl.WorkflowDaoImpl;
import com.jivesoftware.ps.addons.jep.clm.domain.Rule;
import com.jivesoftware.ps.addons.jep.clm.domain.Workflow;

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

    @Test
    public void testGetWorkflow() {
        WorkflowDaoImpl dao = new WorkflowDaoImpl(dbi);
        
        final Workflow wf = dao.getById(2);
        System.out.println(wf);
        
    }
    
    @Test
    public void testSaveWorkflow() {
    	WorkflowDaoImpl dao = new WorkflowDaoImpl(dbi);
    	
    	Rule rule = new Rule(
    			0,
    			3,
    			Collections.emptyList(),
    			null,
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
    
    @Test
    public void deleteWorkflow() {
    	WorkflowDaoImpl dao = new WorkflowDaoImpl(dbi);
    	
    	Rule rule = new Rule(
    			0,
    			1001,
    			Collections.emptyList(),
    			null,
    			1234,
    			100100100,
    			"test",
    			100100100,
    			"test"
    		);
    	List <Rule> ruleList = new ArrayList<Rule>();
    	ruleList.add(rule);
    	Workflow workflow = new Workflow(
    		1001,
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
    	
    	dao.Delete(workflow);
    	    	
    }

    @Test
    public void listWorkflow() {
    	WorkflowDaoImpl dao = new WorkflowDaoImpl(dbi);
    	
    	List<Workflow> wfl = new ArrayList<>();
    	wfl = dao.listWorkflows("open", 10, 2);
    	System.out.print(wfl);
    }
}
