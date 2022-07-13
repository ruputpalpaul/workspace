package com.jivesoftware.ps.addons.jep.clm.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.postgresql.ds.PGPoolingDataSource;
import org.skife.jdbi.v2.DBI;

import com.jivesoftware.ps.addons.jep.clm.dao.impl.WorkflowDaoImpl;
import com.jivesoftware.ps.addons.jep.clm.domain.Action;
import com.jivesoftware.ps.addons.jep.clm.domain.ActionType;
import com.jivesoftware.ps.addons.jep.clm.domain.ContentType;
import com.jivesoftware.ps.addons.jep.clm.domain.Notification;
import com.jivesoftware.ps.addons.jep.clm.domain.Place;
import com.jivesoftware.ps.addons.jep.clm.domain.Recipient;
import com.jivesoftware.ps.addons.jep.clm.domain.Reviewer;
import com.jivesoftware.ps.addons.jep.clm.domain.Rule;
import com.jivesoftware.ps.addons.jep.clm.domain.Trigger;
import com.jivesoftware.ps.addons.jep.clm.domain.TriggerType;
import com.jivesoftware.ps.addons.jep.clm.domain.Workflow;
import com.jivesoftware.ps.addons.jep.clm.domain.WorkflowStatus;
import com.jivesoftware.ps.addons.jep.clm.domain.WorkflowType;

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
    	List <Rule> ruleList = new ArrayList<Rule>();
    	List <ContentType> contentTypeList = new ArrayList<ContentType>();
    	List <Reviewer> reviewerList = new ArrayList<Reviewer>();
    	List <Place> placeList = new ArrayList<Place>();
    	List <Action> actionList = new ArrayList<Action>();
    	List <Recipient> recipientList = new ArrayList<Recipient>();
    	
    	Recipient recipient = new Recipient(
    			0,
    			2,
    			"test",
    			WorkflowStatus.Active
    			);
    	
    	recipientList.add(recipient);
    	
    	Notification notification = new Notification(
    			3,
    			2,
    			recipientList,
    			"test subject",
    			"test text",
    			WorkflowStatus.Active
    			);
    	
    	Action action = new Action(
    			1,
    			2,
    			notification,
    			WorkflowStatus.Active,
    			ActionType.Notify
    			);
    	actionList.add(action);
    	
    	Trigger trigger = new Trigger(
    			0,
    			1,
    			TriggerType.Created,
    			1,
    			WorkflowStatus.Active
    			);
    			
    	
    	Rule rule = new Rule(
    			0,
    			1,
    			actionList,
    			trigger,
    			1234,
    			new Date(),
    			"test",
    			new Date(),
    			WorkflowStatus.Active
    		);
    	
    	ruleList.add(rule);
    	
    	ContentType contentType = new ContentType(
    			0,
    			1,
    			WorkflowStatus.Active,
    			"video"
    			);
    	
    	contentTypeList.add(contentType);
    	
    	Reviewer reviewer = new Reviewer(
    			0,
    			1,
    			1,
    			WorkflowStatus.Active
    			);
    	
    	reviewerList.add(reviewer);
    	
    	Place place = new Place(
    			0,
    			2,
    			"url",
    			1,
    			1,
    			"test",
    			WorkflowStatus.Active,
    			"common"
    			);
    	
    	placeList.add(place);
    	    	
    	Workflow workflow = new Workflow(
    		0,
    		ruleList,
    		contentTypeList,
    		reviewerList,
    		placeList,
    		1,
    		1,
    		new Date(),
    		"test",
    		new Date(),
    		WorkflowStatus.Active,
    		WorkflowType.Content
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
    			new Date(),
    			"test",
    			new Date(),
    			WorkflowStatus.Active
    		);
    	List <Rule> ruleList = new ArrayList<Rule>();
    	ruleList.add(rule);
    	
    	Workflow workflow = new Workflow(
    			1,
        		ruleList,
        		Collections.emptyList(),
        		Collections.emptyList(),
        		Collections.emptyList(),
        		1,
        		1,
        		new Date(),
        		"test",
        		new Date(),
        		WorkflowStatus.Active,
        		WorkflowType.Content
    	);
    	
    	dao.Delete(workflow);
    	    	
    }

    @Test
    public void listWorkflow() {
    	WorkflowDaoImpl dao = new WorkflowDaoImpl(dbi);
    	
    	List<Workflow> wfl = new ArrayList<>();
    	wfl = dao.listWorkflows(WorkflowType.Content, 10, 2);
    	System.out.print(wfl);
    }
}
