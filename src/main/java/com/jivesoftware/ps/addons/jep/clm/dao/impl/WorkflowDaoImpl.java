package com.jivesoftware.ps.addons.jep.clm.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

import com.jivesoftware.ps.addons.jep.clm.dao.WorkflowDao;
import com.jivesoftware.ps.addons.jep.clm.dao.impl.mapper.WorkflowMapper;
import com.jivesoftware.ps.addons.jep.clm.domain.Action;
import com.jivesoftware.ps.addons.jep.clm.domain.ContentType;
import com.jivesoftware.ps.addons.jep.clm.domain.Notification;
import com.jivesoftware.ps.addons.jep.clm.domain.Place;
import com.jivesoftware.ps.addons.jep.clm.domain.Recipient;
import com.jivesoftware.ps.addons.jep.clm.domain.Reviewer;
import com.jivesoftware.ps.addons.jep.clm.domain.Rule;
import com.jivesoftware.ps.addons.jep.clm.domain.Trigger;
import com.jivesoftware.ps.addons.jep.clm.domain.Workflow;
import com.jivesoftware.ps.addons.jep.clm.domain.WorkflowType;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WorkflowDaoImpl implements WorkflowDao {


	final Map<Long, Workflow> workflows = new HashMap<>();

    private static final String GET_BY_ID_SQL = "SELECT wf.workflow_id,\r\n"
    		+ "       wf.author,\r\n"
    		+ "       wf.last_modifier,\r\n"
    		+ "       wf.modification_time as workflow_modification_time,\r\n"
    		+ "       wf.name as workflow_name,\r\n"
    		+ "       wf.publish_time as workflow_publish_time,\r\n"
    		+ "       wf.status as workflow_status,\r\n"
    		+ "       wf.type as workflow_type,\r\n"
    		+ "       rv.reviewer_id,\r\n"
    		+ "       rv.status as reviewer_status,\r\n"
    		+ "       rv.reviewer_user_id,\r\n"
    		+ "       p.place_id,\r\n"
    		+ "       p.url,\r\n"
    		+ "       p.jive_id,\r\n"
    		+ "       p.jive_place_id,\r\n"
    		+ "       p.name as place_name,\r\n"
    		+ "       p.status as place_status,\r\n"
    		+ "       p.type as place_type,\r\n"
    		+ "       ct.content_type_id,\r\n"
    		+ "       ct.name as content_type_name,\r\n"
    		+ "       ct.status as content_type_status,\r\n"
    		+ "       r.rule_id,\r\n"
    		+ "       r.executor_id,\r\n"
    		+ "       r.modification_time as rule_modification_time,\r\n"
    		+ "       r.name as rule_name,\r\n"
    		+ "       r.publish_time as rule_publish_time,\r\n"
    		+ "       r.status as rule_status,\r\n"
    		+ "       t.trigger_id,\r\n"
    		+ "       t.trigger_type,\r\n"
    		+ "       t.trigger_value,\r\n"
    		+ "       t.status as trigger_status,\r\n"
    		+ "       ra.action_id,\r\n"
    		+ "       ra.status as rule_action_status,\r\n"
    		+ "       ra.type as rule_action_type,\r\n"
    		+ "       n.notification_id,\r\n"
    		+ "       n.subject,\r\n"
    		+ "       n.text,\r\n"
    		+ "       n.status as notification_status,\r\n"
    		+ "       rt.recipient_type_id,\r\n"
    		+ "       rt.name as recipient_type_name,\r\n"
    		+ "       rt.status as recipient_type_status\r\n"
    		+ "\r\n"
    		+ "  FROM clm_workflow as wf\r\n"
    		+ "  LEFT JOIN clm_reviewer as rv\r\n"
    		+ "         ON wf.workflow_id = rv.workflow_id\r\n"
    		+ "  LEFT JOIN clm_place as p\r\n"
    		+ "         ON wf.workflow_id = p.workflow_id\r\n"
    		+ "  LEFT JOIN clm_content_type as ct\r\n"
    		+ "         ON wf.workflow_id = ct.workflow_id\r\n"
    		+ "  INNER JOIN clm_rule as r\r\n"
    		+ "         ON wf.workflow_id = r.workflow_id\r\n"
    		+ "  INNER JOIN clm_trigger as t\r\n"
    		+ "         ON r.rule_id = t.rule_id\r\n"
    		+ "  INNER JOIN clm_rule_action as ra\r\n"
    		+ "         ON r.rule_id = ra.rule_id\r\n"
    		+ "  LEFT JOIN clm_notification as n\r\n"
    		+ "         ON ra.action_id = n.action_id\r\n"
    		+ "  LEFT JOIN clm_recipient_type as rt\r\n"
    		+ "         ON n.notification_id = rt.notification_id\r\n"
    		+ "\r\n"
    		+ " WHERE wf.workflow_id = :workflow_id;";

    private static final String LIST_WORKFLOWS_SQL = "SELECT wf.workflow_id,\r\n"
    		+ "       wf.author,\r\n"
    		+ "       wf.last_modifier,\r\n"
    		+ "       wf.modification_time as workflow_modification_time,\r\n"
    		+ "       wf.name as workflow_name,\r\n"
    		+ "       wf.publish_time as workflow_publish_time,\r\n"
    		+ "       wf.status as workflow_status,\r\n"
    		+ "       wf.type as workflow_type,\r\n"
    		+ "       rv.reviewer_id,\r\n"
    		+ "       rv.status as reviewer_status,\r\n"
    		+ "       rv.reviewer_user_id,\r\n"
    		+ "       p.place_id,\r\n"
    		+ "       p.url,\r\n"
    		+ "       p.jive_id,\r\n"
    		+ "       p.jive_place_id,\r\n"
    		+ "       p.name as place_name,\r\n"
    		+ "       p.status as place_status,\r\n"
    		+ "       p.type as place_type,\r\n"
    		+ "       ct.content_type_id,\r\n"
    		+ "       ct.name as content_type_name,\r\n"
    		+ "       ct.status as content_type_status,\r\n"
    		+ "       r.rule_id,\r\n"
    		+ "       r.executor_id,\r\n"
    		+ "       r.modification_time as rule_modification_time,\r\n"
    		+ "       r.name as rule_name,\r\n"
    		+ "       r.publish_time as rule_publish_time,\r\n"
    		+ "       r.status as rule_status,\r\n"
    		+ "       t.trigger_id,\r\n"
    		+ "       t.trigger_type,\r\n"
    		+ "       t.trigger_value,\r\n"
    		+ "       t.status as trigger_status,\r\n"
    		+ "       ra.action_id,\r\n"
    		+ "       ra.status as rule_action_status,\r\n"
    		+ "       ra.type as rule_action_type,\r\n"
    		+ "       n.notification_id,\r\n"
    		+ "       n.subject,\r\n"
    		+ "       n.text,\r\n"
    		+ "       n.status as notification_status,\r\n"
    		+ "       rt.recipient_type_id,\r\n"
    		+ "       rt.name as recipient_type_name,\r\n"
    		+ "       rt.status as recipient_type_status\r\n"
    		+ "\r\n"
    		+ "  FROM clm_workflow as wf\r\n"
    		+ "  LEFT JOIN clm_reviewer as rv\r\n"
    		+ "         ON wf.workflow_id = rv.workflow_id\r\n"
    		+ "  LEFT JOIN clm_place as p\r\n"
    		+ "         ON wf.workflow_id = p.workflow_id\r\n"
    		+ "  LEFT JOIN clm_content_type as ct\r\n"
    		+ "         ON wf.workflow_id = ct.workflow_id\r\n"
    		+ "  INNER JOIN clm_rule as r\r\n"
    		+ "         ON wf.workflow_id = r.workflow_id\r\n"
    		+ "  INNER JOIN clm_trigger as t\r\n"
    		+ "         ON r.rule_id = t.rule_id\r\n"
    		+ "  INNER JOIN clm_rule_action as ra\r\n"
    		+ "         ON r.rule_id = ra.rule_id\r\n"
    		+ "  LEFT JOIN clm_notification as n\r\n"
    		+ "         ON ra.action_id = n.action_id\r\n"
    		+ "  LEFT JOIN clm_recipient_type as rt\r\n"
    		+ "         ON n.notification_id = rt.notification_id\r\n"
    		+ "\r\n"
    		+ "WHERE wf.workflow_type = :workflow_type \r\n"
    		+ "LIMIT :count offset :offset \r\n"
    		+ "ORDER BY workflow_id";

    private static final String INSERT_WORKFLOW_SQL = "INSERT into clm_workflow(\r\n"
    		+ "    author,\r\n"
    		+ "    last_modifier,\r\n"
    		+ "    modification_time,\r\n"
    		+ "    name,\r\n"
    		+ "    publish_time,\r\n"
    		+ "    status,\r\n"
    		+ "    type\r\n"
    		+ ")\r\n"
    		+ "values\r\n"
    		+ "(\r\n"
    		+ "    :author,\r\n"
    		+ "    :last_modifier,\r\n"
    		+ "    :modification_time,\r\n"
    		+ "    :name,\r\n"
    		+ "    :publish_time,\r\n"
    		+ "    :status,\r\n"
    		+ "    :type\r\n"
    		+ ")";

    private static final String UPDATE_WORKFLOW_SQL = "UPDATE clm_workflow SET\r\n"
    		+ "    author = :author,\r\n"
    		+ "    last_modifier = :last_modifier,\r\n"
    		+ "    modification_time = :modification_time,\r\n"
    		+ "    name = :name,\r\n"
    		+ "    publish_time = :publish_time,\r\n"
    		+ "    status = :status,\r\n"
    		+ "    type = :type \r\n"
			+ "where \r\n"
    		+ "    workflow_id = :workflow_id";

    private static final String INSERT_RULE_SQL = "insert into clm_rule (\r\n"
    		+ "    executor_id,\r\n"
    		+ "    modification_time,\r\n"
    		+ "    name,\r\n"
    		+ "    publish_time,\r\n"
    		+ "    status,\r\n"
    		+ "    workflow_id\r\n"
    		+ ")\r\n"
    		+ "values(\r\n"
    		+ "    :executor_id,\r\n"
    		+ "    :modification_time,\r\n"
    		+ "    :name,\r\n"
    		+ "    :publish_time,\r\n"
    		+ "    :status,\r\n"
    		+ "    :workflow_id\r\n"
    		+ ")";

	private static final String UPDATE_RULE_SQL = "UPDATE clm_rule SET \r\n"
    		+ "    rule_id = :rule_id,\r\n"
    		+ "    executor_id = :executor_id,\r\n"
    		+ "    modification_time = :modification_time,\r\n"
    		+ "    name = :name,\r\n"
    		+ "    publish_time = :publish_time,\r\n"
    		+ "    status = :status \r\n"
			+ "where \r\n"
    		+ "    workflow_id = :workflow_id";

	private static final String INSERT_CONTENT_TYPE_SQL = "insert into clm_content_type (\r\n"
			+ "    name,\r\n"
			+ "    status,\r\n"
			+ "    workflow_id\r\n"
			+ ")\r\n"
			+ "values(\r\n"
			+ "    :name,\r\n"
			+ "    :status,\r\n"
			+ "    :workflow_id\r\n"
			+ ")";
	private static final String UPDATE_CONTENT_TYPE_SQL = "UPDATE clm_content_type SET \r\n"
			+ "    content_type_id = :content_type_id,\r\n"
			+ "    name = :name,\r\n"
			+ "    status = :status \r\n"
			+ "where \r\n"
			+ "    workflow_id = :workflow_id\r\n";

	private static final String INSERT_REVIEWER_SQL = "insert into clm_reviewer (\r\n"
			+ "    status,\r\n"
			+ "    reviewer_user_id,\r\n"
			+ "    workflow_id\r\n"
			+ ")\r\n"
			+ "values(\r\n"
			+ "    :status,\r\n"
			+ "    :reviewer_user_id,\r\n"
			+ "    :workflow_id\r\n"
			+ ")";
	private static final String UPDATE_REVIEWER_SQL = "UPDATE clm_reviewer SET \r\n"
			+ "    reviewer_id = :reviewer_id,\r\n"
			+ "    status = :status,\r\n"
			+ "    reviewer_user_id = :reviewer_user_id \r\n"
			+ "where \r\n"
			+ "    workflow_id = :workflow_id\r\n";

	private static final String INSERT_PLACE_SQL = "insert into clm_place (\r\n"
			+ "    url,\r\n"
			+ "    jive_id,\r\n"
			+ "    jive_place_id,\r\n"
			+ "    name,\r\n"
			+ "    status,\r\n"
			+ "    type,\r\n"
			+ "    workflow_id\r\n"
			+ ")\r\n"
			+ "values(\r\n"
			+ "    :url,\r\n"
			+ "    :jive_id,\r\n"
			+ "    :jive_place_id,\r\n"
			+ "    :name,\r\n"
			+ "    :status,\r\n"
			+ "    :type,\r\n"
			+ "    :workflow_id\r\n"
			+ ")";
	private static final String UPDATE_PLACE_SQL = "UPDATE clm_place SET\r\n"
			+ "		place_id = :place_id,\r\n"
			+ "		url = :url,\r\n"
			+ "		jive_id = :jive_id,\r\n"
			+ "		jive_place_id = :jive_place_id,\r\n"
			+ "		name = :name,\r\n"
			+ "		status = :status,\r\n"
			+ "		type = :type\r\n"
			+ "	where \r\n"
			+ "		workflow_id = :workflow_id";

	private static final String INSERT_ACTION_SQL = "INSERT into clm_rule_action(\r\n"
			+ "    status,\r\n"
			+ "    type,\r\n"
			+ "    rule_id\r\n"
			+ ")\r\n"
			+ "values(\r\n"
			+ "    :status,\r\n"
			+ "    :type,\r\n"
			+ "    :rule_id\r\n"
			+ ")";

	private static final String UPDATE_ACTION_SQL = "UPDATE  clm_rule_action SET\r\n"
			+ "    action_id = :action_id,\r\n"
			+ "    status = :status,\r\n"
			+ "    type = :type\r\n"
			+ "	where \r\n"
			+ "    rule_id = :rule_id";

	private static final String INSERT_NOTIFICATION_SQL = "insert into clm_notification (\r\n"
			+ "    subject,\r\n"
			+ "    text,\r\n"
			+ "    action_id, \r\n"
			+ "    status \r\n"
			+ ")\r\n"
			+ "values(\r\n"
			+ "    :subject,\r\n"
			+ "    :text,\r\n"
			+ "    :action_id, \r\n"
			+ "    :status \r\n"
			+ ")";

	private static final String UPDATE_NOTIFICATION_SQL = "UPDATE  clm_notification SET\r\n"
			+ "    text = :text,\r\n"
			+ "    subject = :subject,\r\n"
			+ "    status = :status\r\n"
			+ "	where \r\n"
			+ "    action_id = :action_id";

	private static final String INSERT_RECIPIENT_SQL = "insert into clm_recipient_type (\r\n"
			+ "    name,\r\n"
			+ "    status,\r\n"
			+ "    notification_id\r\n"
			+ ")\r\n"
			+ "values(\r\n"
			+ "    :name,\r\n"
			+ "    :status,\r\n"
			+ "    :notification_id \r\n"
			+ ")";

	private static final String UPDATE_RECIPIENT_SQL = "UPDATE  clm_recipient_type SET\r\n"
			+ "    notification_id = :notification_id,\r\n"
			+ "    recipient_type_id = :recipient_type_id,\r\n"
			+ "    name = :name,\r\n"
			+ "    status = :status\r\n"
			+ "	where \r\n"
			+ "    action_id = :action_id";

	private static final String INSERT_TRIGGER_SQL = "insert into clm_trigger(\r\n"
			+ "    trigger_type,\r\n"
			+ "    trigger_value,\r\n"
			+ "    status,\r\n"
			+ "    rule_id\r\n"
			+ ")\r\n"
			+ "values(\r\n"
			+ "    :trigger_type,\r\n"
			+ "    :trigger_value,\r\n"
			+ "    :status,\r\n"
			+ "    :rule_id\r\n"
			+ ")";

	private static final String UPDATE_TRIGGER_SQL = "UPDATE clm_trigger SET\r\n"
			+ "    trigger_id = :trigger_id,\r\n"
			+ "    trigger_type = :trigger_type,\r\n"
			+ "    trigger_value = :trigger_value,\r\n"
			+ "    status = :status\r\n"
			+ "	where \r\n"
			+ "    rule_id = :rule_id";




    private final DBI dbi;

    @Override
    public Workflow getById(long workflowId) {
        final Handle handle = dbi.open();

        try {
            return handle.createQuery(GET_BY_ID_SQL)
                         .bind("workflow_id", workflowId)
                         .fold((Workflow)null,WorkflowMapper::mapDetails);
        } finally {
            handle.close();
        }
    }

    @Override
    public void Delete(Workflow workflow) {
    	final Handle handle = dbi.open();

    	if (workflow.getWorkflowId()!=0) {
    		handle.createStatement("UPDATE clm_workflow SET status = 'Deleted' WHERE workflow_id = :workflow_id")
    			.bind("workflow_id", workflow.getWorkflowId())
    			.execute();

    		for(Rule rule: workflow.getRules()) {

		    	if (rule.getRuleId()!=0)
		    		handle.createStatement("UPDATE clm_rule SET status = 'Deleted' WHERE rule_id = :rule_id")
	    				.bind("rule_id", rule.getRuleId())
	    				.execute();
		    	}
    		}
    		for(ContentType contentType: workflow.getContentTypes())
			{
				if (contentType.getContentTypeId()!=0)
					handle.createStatement("UPDATE clm_content_type SET status = 'Deleted' WHERE content_type_id = :content_type_id")
    				.bind("content_type_id", contentType.getContentTypeId())
    				.execute();
				}
			}

    		for(Reviewer reviewer: workflow.getReviewers())
			{
				if (reviewer.getReviewerId()!=0)
					handle.createStatement("UPDATE clm_reviewer SET status = 'Deleted' WHERE reviewer_id = :reviewer_id")
    				.bind("reviewer_id", reviewer.getReviewerId())
    				.execute();
				}
			}
    		for(Place place: workflow.getPlaces())
			{
				if (Objects.isNull(place.getPlaceId()) && place.getStatus().getValue() != "Deleted")
				{
					System.out.print("Place doesn't exist");
				}
				else {
					handle.createStatement("UPDATE clm_place SET status = 'Deleted' WHERE place_id = :place_id")
    				.bind("place_id", place.getPlaceId())
    				.execute();
				}
			}
			for(place: place in the system please get all the updated system)

    	handle.close();

    }

    @Override
    public void saveWorkflow(Workflow workflow) {
    	final Handle handle = dbi.open();
    	try {
			//first we check if the workflow is existing on the db or not and whether it is a deleted workflow
	    	if (workflow.getWorkflowId()==0 && workflow.getStatus().getValue() != "Deleted") {

	            handle.createStatement(INSERT_WORKFLOW_SQL)
                     .bind("author", workflow.getAuthor())
                     .bind("last_modifier", workflow.getLastModifier())
                     .bind("modification_time", workflow.getModificationTime())
                     .bind("name", workflow.getName())
                     .bind("publish_time", workflow.getPublishTime())
                     .bind("status", workflow.getStatus().getValue())
                     .bind("type", workflow.getType().getValue())
                     .execute();
		    }
	    	else {
				//here we do an update if the workflow exists already by assuming that if it has an id it must be existent on the db as well
	            handle.createStatement(UPDATE_WORKFLOW_SQL)
                     .bind("workflow_id", workflow.getWorkflowId())
                     .bind("author", workflow.getAuthor())
                     .bind("last_modifier", workflow.getLastModifier())
                     .bind("modification_time", workflow.getModificationTime())
                     .bind("name", workflow.getName())
                     .bind("publish_time", workflow.getPublishTime())
                     .bind("status", workflow.getStatus().getValue())
                     .bind("type", workflow.getType().getValue())
                     .execute();
		    }


		    for(Rule rule: workflow.getRules()) {
				// similar strategy for rule as well that we have a workflow rule in the db because the object has an id associated with it.
		    	if (rule.getRuleId()==0 && rule.getStatus().getValue() != "Deleted") {

		            handle.createStatement(INSERT_RULE_SQL)
						.bind("executor_id", rule.getExecutorId())
						.bind("modification_time", rule.getModificationTime())
						.bind("name", rule.getName())
						.bind("publish_time", rule.getPublishTime())
						.bind("status", rule.getStatus().getValue())
						.bind("workflow_id", rule.getWorkflowId())
						.execute();
			    }

				else {

					handle.createStatement(UPDATE_RULE_SQL)
						.bind("rule_id", rule.getRuleId())
						.bind("executor_id", rule.getExecutorId())
						.bind("modification_time", rule.getModificationTime())
						.bind("name", rule.getName())
						.bind("publish_time", rule.getPublishTime())
						.bind("status", rule.getStatus().getValue())
						.bind("workflow_id", rule.getWorkflowId())
						.execute();
			    }
				// for the nested lists in various object we do an initial check of whether the object exists on the db or not
				// if it does exist we update the object and if it does not then we insert a new object in to the db with appropriate relationships.
		    	for (Action action: rule.getActions())
				{
		    		if (action.getActionId()==0 && action.getStatus().getValue()!= "Deleted") {

			            handle.createStatement(INSERT_ACTION_SQL)
							.bind("status", action.getStatus().getValue())
							.bind("type", action.getType())
							.bind("rule_id", action.getRuleId())
							.execute();
				    }

					else {

						handle.createStatement(UPDATE_ACTION_SQL)
							.bind("action_id", action.getActionId())
							.bind("status", action.getStatus().getValue())
							.bind("type", action.getType())
							.bind("rule_id", action.getRuleId())
							.execute();
				    }

		    		Notification notification = action.getNotification();

		    		if (notification.getNotificationId()==0) {

			            handle.createStatement(INSERT_NOTIFICATION_SQL)
							.bind("subject", notification.getSubject())
							.bind("text", notification.getText())
							.bind("status", notification.getStatus().getValue())
							.bind("action_id", notification.getActionId())
							.execute();
				    }

					else {

						handle.createStatement(UPDATE_NOTIFICATION_SQL)
							.bind("notification_id", notification.getNotificationId())
							.bind("subject", notification.getSubject())
							.bind("text", notification.getText())
							.bind("status", notification.getStatus().getValue())
							.bind("action_id", notification.getActionId())
							.execute();
					}
		    		for (Recipient recipient: notification.getRecipients())
					{
			    		if (recipient.getRecipientTypeId()==0) {

				            handle.createStatement(INSERT_RECIPIENT_SQL)
								.bind("notification_id", recipient.getNotificationId())
								.bind("name", recipient.getName())
								.bind("status", recipient.getStatus().getValue())
								.execute();
					    }

						else {

							handle.createStatement(UPDATE_RECIPIENT_SQL)
								.bind("notification_id", recipient.getNotificationId())
								.bind("recipient_type_id", recipient.getRecipientTypeId())
								.bind("status", recipient.getStatus().getValue())
								.bind("name", recipient.getName())
								.execute();
					    }
					}


			    	Trigger trigger = rule.getTrigger();

		    		if (trigger.getTriggerId()==0) {

			            handle.createStatement(INSERT_TRIGGER_SQL)
							.bind("trigger_type", trigger.getTriggerType().getValue())
							.bind("trigger_value", trigger.getTriggerValue())
							.bind("status", trigger.getStatus().getValue())
							.bind("rule_id", trigger.getRuleId())
							.execute();
				    }

					else {

						handle.createStatement(UPDATE_TRIGGER_SQL)
							.bind("trigger_id", trigger.getTriggerId())
							.bind("trigger_type", trigger.getTriggerType().getValue())
							.bind("trigger_value", trigger.getTriggerValue())
							.bind("status", trigger.getStatus().getValue())
							.bind("rule_id", trigger.getRuleId())
							.execute();
				    }
				}
		    }
			//content type describes if the content that the workflow is managing is a video photo or blog. There are various options associated with it.
			for(ContentType contentType: workflow.getContentTypes())
			{
				if (contentType.getContentTypeId()==0 && contentType.getStatus().getValue() != "Deleted")
				{

					handle.createStatement(INSERT_CONTENT_TYPE_SQL)
						.bind("name", contentType.getName())
						.bind("status", contentType.getStatus().getValue())
						.bind("workflow_id", contentType.getWorkflowId())
						.execute();

				}

				else{

					handle.createStatement(UPDATE_CONTENT_TYPE_SQL)
						.bind("content_type_id", contentType.getContentTypeId())
						.bind("name", contentType.getName())
						.bind("status", contentType.getStatus().getValue())
						.bind("workflow_id", contentType.getWorkflowId())
						.execute();

				}
			}

			for(Reviewer reviewer: workflow.getReviewers())
			{
				if (reviewer.getReviewerId()==0 && reviewer.getStatus().getValue() != "Deleted")
				{

					handle.createStatement(INSERT_REVIEWER_SQL)
						.bind("reviewer_user_id", reviewer.getReviewerUserId())
						.bind("status", reviewer.getStatus().getValue())
						.bind("workflow_id", reviewer.getWorkflowId())
						.execute();

				}
				else{

					handle.createStatement(UPDATE_REVIEWER_SQL)
						.bind("reviewer_id", reviewer.getReviewerId())
						.bind("reviewer_user_id", reviewer.getReviewerUserId())
						.bind("status", reviewer.getStatus().getValue())
						.bind("workflow_id", reviewer.getWorkflowId())
						.execute();

				}

			}

			for(Place place: workflow.getPlaces())
			{
				//What if we have to get all the eystem in place and not worry about them ever, think about it?
				//It is our goal to get all the systems in place by 27 th of this month.
				//Let us get all hands on deck working on this.
				if (Objects.isNull(place.getPlaceId()))
				{

					handle.createStatement(INSERT_PLACE_SQL)
						.bind("url", place.getUrl())
						.bind("jive_id", place.getJiveId())
						.bind("jive_place_id", place.getJivePlaceId())
						.bind("name", place.getName())
						.bind("status", place.getStatus().getValue())
						.bind("type", place.getType())
						.bind("workflow_id", place.getWorkflowId())
						.execute();

				}
				else{

					handle.createStatement(UPDATE_PLACE_SQL)
						.bind("place_id", place.getPlaceId())
						.bind("url", place.getUrl())
						.bind("jive_id", place.getJiveId())
						.bind("jive_place_id", place.getJivePlaceId())
						.bind("name", place.getName())
						.bind("status", place.getStatus().getValue())
						.bind("type", place.getType())
						.bind("workflow_id", place.getWorkflowId())
						.execute();

					Let us get all the SOPs in place
				}
			}

			else{
				handle.createStatement(UPDATE_ACTION_SQL)
				.bind("place_id", place.getPlaceId())
				.bind("url", place.getUrl())
				.bind("jive_id", place.getJiveId())
				.bind("jive_place_id", place.getJivePlaceId())
				.bind("name", place.getName())
				.bind("status", place.getStatus().getValue())
				.bind("type", place.getType())
				.bind("workflow_id", place.getWorkflowId())
				.execute();
			}
        }

		finally {
            handle.close();
        }

		else{
			handle.createQuery("Select * from clm_workflow where action_id == 0
			left join clm_action as as on
			workflow_id = workflow_id
			left join clm_rule as r
			on r.workflow_id = workflow_id
			left join clm_ninja as n
			where n,workflow_id - worjflow_id");
			.bind("worjflow_id", workflow_id);
			.bind("workflow_status"), WorkflowStatus.Deleted.
			On gettting the above information we must get all the data and collect them into the database to later format as needed.
			This data will be used to later create the appropriate tables and then we can proceed futher from there
		}
    }


    @Override
	public List<Workflow> listWorkflows(WorkflowType type, Integer startIndex, Integer count) {

	    	List<Workflow> workflowList= new ArrayList<>();
	    	int i = 0;
	    	while(i < count)
	    	{
	    		workflowList.add(getByIdAndType(startIndex, type.getValue(), count));
	    		i++;
	    	}

	        return workflowList;
    }
}
