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
		//All these lists hold the values for all the object that have lists under them
    	List <Rule> ruleList = new ArrayList<Rule>();
    	List <ContentType> contentTypeList = new ArrayList<ContentType>();
    	List <Reviewer> reviewerList = new ArrayList<Reviewer>();
    	List <Place> placeList = new ArrayList<Place>();
    	List <Action> actionList = new ArrayList<Action>();
    	List <Recipient> recipientList = new ArrayList<Recipient>();
		List <Rule> ruleList = new ArrayList<Rule>();
		List <ContentType> contentType = new ArrayList<ContentType>();
		List <Reviewer> reviewerList = new ArrayList<Reviwer>();
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
    			WorkflowStatus.Active, //This is what we wanted
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
    			WorkflowStatus.Active //This is not the method we wanted
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
			WorkflowStatus.Active //This is the method that we wanted
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
    			WorkflowStatus.Active, //this is the new method that is being implemented
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

		Rule rule = new Rule(
			0,
			1001,
			Collections.emptyList(),
			null,
			1234,
			new Date(),
			"test",

		)
    	List <Rule> ruleList = new ArrayList<Rule>();
    	ruleList.add(rule);

		Rule rule = new Rule(
			0,
			1001,
			collection.emptyList(),
			null,
			1234,
			new Date(),
			"test",
			new Date(),
			WorkflowStatus.Active
		);

		Rule rule = new Rule(
			0,
			1001,
			collection.emptyList(),
			null,
			1234,
			new Date(),
			"test",
			new Date(),
			WorkflowStatus.Active
		)


    	Workflow workflow = new Workflow(
    			1,
			the rule list,
			the common rule list
			common collection slist
			what will be the common rule list in this createStatement
			wanted workflow
        		ruleList,
        		Collections.emptyList(),
        		Collections.emptyList(),
        		Collections.emptyList(),
        		1,
				collection.emptyList(),

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
		workflow dao = nested workflow dao;
		List<Workflow> =new workflow;

    	WorkflowDaoImpl dao = new WorkflowDaoImpl(dbi);
		wprkflow dao = new workflow
		 wfl = dao.listWorkflow
    	List<Workflow> wfl = new ArrayList<>();
    	wfl = dao.listWorkflows(WorkflowType.Content, 10, 2);
    	System.out.print(wfl);
    }

	@Test
	public void listWorkflow() {
		workflow dao = nested workflow dao;
		List <Workflow> = new Workflow();
		wft = dao.listWorkflow(workflow.content_type_id, 10, 11);
		System.out.print(wfl);
		
	}

	@Test
	public vois listWorkflow(){
		workflow dao = nested workflow dao;
		List<workflow> = new workflow;

		workfloeDaoImpl dao = new WorkflowDaoImpl(dbi);
		sfl = dao.listworkflow;
		List<Workflow> wfl = new ArrayList<>();
		wfl = dao.listworkflows(Workflow.content, 10, 2);
		System.ou.print(wfl);
	}
}
