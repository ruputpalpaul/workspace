package com.jivesoftware.ps.addons.jep.clm.dao.impl;

import java.util.List;
import java.util.Objects;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import com.jivesoftware.ps.addons.jep.clm.dao.WorkflowDao;
import com.jivesoftware.ps.addons.jep.clm.dao.impl.mapper.WorkflowMapper;
import com.jivesoftware.ps.addons.jep.clm.domain.Workflow;
import lombok.RequiredArgsConstructor;
import com.jivesoftware.ps.addons.jep.clm.domain.Action;
import com.jivesoftware.ps.addons.jep.clm.domain.ContentType;
import com.jivesoftware.ps.addons.jep.clm.domain.Recipient;
import com.jivesoftware.ps.addons.jep.clm.domain.Reviewer;
import com.jivesoftware.ps.addons.jep.clm.domain.Rule;
import com.jivesoftware.ps.addons.jep.clm.domain.Trigger;
import com.jivesoftware.ps.addons.jep.clm.domain.Place;
import com.jivesoftware.ps.addons.jep.clm.domain.Notification;

@RequiredArgsConstructor
public class WorkflowDaoImpl implements WorkflowDao {
    private static final String GET_BY_ID_SQL = "select wf.workflow_id,\r\n"
                    + "       wf.author,\r\n"
                    + "       wf.last_modifier,\r\n"
                    + "       wf.modification_time workflow_modification_time,\r\n"
                    + "       wf.name as workflow_name,\r\n"
                    + "       wf.publish_time workflow_publish_time,\r\n"
                    + "       wf.status as workflo_status,\r\n"
                    + "       wf.type as workflo_type,\r\n"
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
                    + "       r.status as rule_publish_status,\r\n"
                    + "       t.trigger_id,\r\n"
                    + "       t.trigger_type,\r\n"
                    + "       t.trigger_value,\r\n"
                    + "       ra.action_id,\r\n"
                    + "       ra.status as rule_action_status,\r\n"
                    + "       ra.type as rule_action_type,\r\n"
                    + "       n.notification_id,\r\n"
                    + "       n.subject,\r\n"
                    + "       n.text,\r\n"
                    + "       rt.recipient_type_id,\r\n"
                    + "       rt.name as recipient_type_name\r\n"
                    + "  FROM clm_workflow as wf\r\n"
                    + "  LEFT JOIN clm_reviewer as rv\r\n"
                    + "         ON wf.workflow_id = rv.workflow_id\r\n"
                    + "  LEFT JOIN clm_place as p\r\n"
                    + "         ON wf.workflow_id = p.workflow_id\r\n"
                    + "  LEFT JOIN clm_content_type as ct\r\n"
                    + "         ON wf.workflow_id = ct.workflow_id\r\n"
                    + " INNER JOIN clm_rule as r\r\n"
                    + "         ON wf.workflow_id = r.workflow_id\r\n"
                    + " INNER JOIN clm_trigger as t\r\n"
                    + "         ON r.rule_id = t.rule_id\r\n"
                    + " INNER JOIN clm_rule_action as ra\r\n"
                    + "         ON r.rule_id = ra.rule_id\r\n"
                    + "  LEFT JOIN clm_notification as n\r\n"
                    + "         ON ra.action_id = n.action_id\r\n"
                    + "  LEFT JOIN clm_recipient_type as rt\r\n"
                    + "         ON n.notification_id = rt.notification_id\r\n"
                    + " WHERE wf.workflow_id = :workflowId";

    private static final String INSERT_WORKFLOW_SQL = "INSERT into clm_workflow(\r\n"
    		+ "    workflow_id,\r\n"
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
    		+ "    :workflow_id,\r\n"
    		+ "    :author,\r\n"
    		+ "    :last_modifier,\r\n"
    		+ "    :modification_time,\r\n"
    		+ "    :name,\r\n"
    		+ "    :publish_time,\r\n"
    		+ "    :status,\r\n"
    		+ "    :type\r\n"
    		+ ");";

    private static final String UPDATE_WORKFLOW_SQL = "UPDATE clm_workflow SET(\r\n"
    		+ "    workflow_id = :workflow_id,\r\n"
    		+ "    author = :author,\r\n"
    		+ "    last_modifier = :last_modifier,\r\n"
    		+ "    modification_time = :modification_time,\r\n"
    		+ "    name = :name,\r\n"
    		+ "    publish_time = :publish_time,\r\n"
    		+ "    status = :status,\r\n"
    		+ "    type = :type\r\n"
    		+ ");";

    private static final String INSERT_RULE_SQL = "insert into clm_rule (\r\n"
    		+ "    rule_id,\r\n"
    		+ "    end_time,\r\n"
    		+ "    executor_id,\r\n"
    		+ "    modification_time,\r\n"
    		+ "    name,\r\n"
    		+ "    publish_time,\r\n"
    		+ "    status,\r\n"
    		+ "    workflow_id\r\n"
    		+ ")\r\n"
    		+ "values(\r\n"
    		+ "    :rule_id,\r\n"
    		+ "    :end_time,\r\n"
    		+ "    :executor_id,\r\n"
    		+ "    :modification_time,\r\n"
    		+ "    :name,\r\n"
    		+ "    :publish_time,\r\n"
    		+ "    :status,\r\n"
    		+ "    :workflow_id\r\n"
    		+ ");";

	private static final String UPDATE_RULE_SQL = "UPDATE clm_rule SET";

	private static final String INSERT_CONTENT_TYPE_SQL = "insert into clm_content_type (\r\n"
			+ "    content_type_id,\r\n"
			+ "    name,\r\n"
			+ "    status,\r\n"
			+ "    workflow_id\r\n"
			+ ")\r\n"
			+ "values(\r\n"
			+ "    :content_type_id,\r\n"
			+ "    :name,\r\n"
			+ "    :status,\r\n"
			+ "    :workflow_id\r\n"
			+ ");";
	private static final String UPDATE_CONTENT_TYPE_SQL = "UPDATE clm_content_type SET";

	private static final String INSERT_REVIEWER_SQL = "insert into clm_reviewer (\r\n"
			+ "    reviewer_id,\r\n"
			+ "    status,\r\n"
			+ "    reviewer_user_id,\r\n"
			+ "    workflow_id\r\n"
			+ ")\r\n"
			+ "values(\r\n"
			+ "    :reviewer_id,\r\n"
			+ "    :status,\r\n"
			+ "    :reviewer_user_id,\r\n"
			+ "    :workflow_id\r\n"
			+ ");";
	private static final String UPDATE_REVIEWER_SQL = "UPDATE clm_reviewer SET";

	private static final String INSERT_PLACE_SQL = "insert into clm_place (\r\n"
			+ "    place_id,\r\n"
			+ "    url,\r\n"
			+ "    jive_id,\r\n"
			+ "    jive_place_id,\r\n"
			+ "    name,\r\n"
			+ "    status,\r\n"
			+ "    type,\r\n"
			+ "    workflow_id\r\n"
			+ ")\r\n"
			+ "values(\r\n"
			+ "    :place_id,\r\n"
			+ "    :url,\r\n"
			+ "    :jive_id,\r\n"
			+ "    :jive_place_id,\r\n"
			+ "    :name,\r\n"
			+ "    :status,\r\n"
			+ "    :type,\r\n"
			+ "    :workflow_id\r\n"
			+ ");";
	private static final String UPDATE_PLACE_SQL = "UPDATE clm_place SET";


    private final DBI dbi;

    @Override
    public Workflow getById(long workflowId) {
        final Handle handle = dbi.open();
        try {
            return handle.createQuery(GET_BY_ID_SQL)
                         .bind("workflowId", workflowId)
                         .fold((Workflow)null, WorkflowMapper::mapDetails);
        } finally {
            handle.close();
        }
    }

    @Override
    public void Update(Workflow workflow) {
        // TODO Auto-generated method stub

    }

    @Override
    public void Delete(Workflow workflow) {
        // TODO Auto-generated method stub

    }

    @Override
    public void saveWorkflow(Workflow workflow) {
    	final Handle handle = dbi.open();

    	if (Objects.isNull(workflow.getWorkflowId())) {
	        try {
	            handle.createQuery(INSERT_WORKFLOW_SQL)
	                         .bind("workflow_id", workflow.getWorkflowId())
	                         .bind("author", workflow.getAuthor())
	                         .bind("last_modifier", workflow.getLastModifier())
	                         .bind("modification_time", workflow.getModificationTime())
	                         .bind("name", workflow.getName())
	                         .bind("publish_time", workflow.getPublishTime())
	                         .bind("status", workflow.getStatus())
	                         .bind("type", workflow.getType());
	        } finally {
	            handle.close();
	        }

		    for(Rule rule: workflow.getRules()) {
		    	if (Objects.isNull(rule.getRuleId()))
		    	{
		    		try {
			            handle.createQuery(INSERT_RULE_SQL)
			                         .bind("rule_id", rule.getRuleId())
			                         .bind("executor_id", rule.getExecutorId())
			                         .bind("modification_time", rule.getModificationTime())
			                         .bind("name", rule.getName())
			                         .bind("publish_time", rule.getPublishTime())
			                         .bind("status", rule.getStatus())
			                         .bind("workflow_id", rule.getWorkflowId());
			        } finally {
			            handle.close();
			        }
		    	}

				else{
					try {
						//TODO change sql
			            handle.createQuery(UPDATE_RULE_SQL)
			                         .bind("rule_id", rule.getRuleId())
			                         .bind("executor_id", rule.getExecutorId())
			                         .bind("modification_time", rule.getModificationTime())
			                         .bind("name", rule.getName())
			                         .bind("publish_time", rule.getPublishTime())
			                         .bind("status", rule.getStatus())
			                         .bind("workflow_id", rule.getWorkflowId());
			        } finally {
			            handle.close();
			        }
				}

		    for(ContentType contentType: workflow.getContentTypes())
			{
				if (Objects.isNull(contentType.getContentTypeId()))
		    	{
		    		try {
			            handle.createQuery(INSERT_CONTENT_TYPE_SQL)
			                         .bind("content_type_id", contentType.getContentTypeId())
			                         .bind("name", contentType.getName())
			                         .bind("status", contentType.getStatus())
			                         .bind("workflow_id", contentType.getWorkflowId());
			        } finally {
			            handle.close();
			        }
		    	}

				else{
					try {
						//TODO change sql
			            handle.createQuery(UPDATE_CONTENT_TYPE_SQL)
			                         .bind("content_type_id", contentType.getContentTypeId())
			                         .bind("name", contentType.getName())
			                         .bind("status", contentType.getStatus())
			                         .bind("workflow_id", contentType.getWorkflowId());
			        } finally {
			            handle.close();
			        }
				}
			}

			for(Reviewer reviewer: workflow.getReviewers())
			{
				if (Objects.isNull(reviewer.getReviewerId()))
		    	{
		    		try {
			            handle.createQuery(INSERT_REVIEWER_SQL)
			                         .bind("reviewer_id", reviewer.getReviewerId())
			                         .bind("reviewer_user_id", reviewer.getReviewerUserId())
			                         .bind("status", reviewer.getStatus())
			                         .bind("workflow_id", reviewer.getWorkflowId());
			        } finally {
			            handle.close();
			        }
		    	}
				else{
					try {
			            handle.createQuery(UPDATE_REVIEWER_SQL)
			                         .bind("reviewer_id", reviewer.getReviewerId())
			                         .bind("reviewer_user_id", reviewer.getReviewerUserId())
			                         .bind("status", reviewer.getStatus())
			                         .bind("workflow_id", reviewer.getWorkflowId());
			        } finally {
			            handle.close();
			        }
				}
			}

			for(Place place: workflow.getPlaces())
			{
				if (Objects.isNull(place.getPlaceId()))
		    	{
		    		try {
			            handle.createQuery(INSERT_PLACE_SQL)
			                         .bind("place_id", place.getPlaceId())
			                         .bind("url", place.getUrl())
			                         .bind("jive_id", place.getJiveId())
			                         .bind("jive_place_id", place.getJivePlaceId())
									 .bind("name", place.getName())
									 .bind("status", place.getStatus())
									 .bind("type", place.getType());
			        } finally {
			            handle.close();
			        }
		    	}
				else{
					try {
			            handle.createQuery(UPDATE_PLACE_SQL)
			                         .bind("place_id", place.getPlaceId())
			                         .bind("url", place.getUrl())
			                         .bind("jive_id", place.getJiveId())
			                         .bind("jive_place_id", place.getJivePlaceId())
									 .bind("name", place.getName())
									 .bind("status", place.getStatus())
									 .bind("type", place.getType());
			        } finally {
			            handle.close();
			        }
				}
			}

	    }

	        //TODO check for the id and insert for the Trigger, Recipient, Notification and Action classes
        }

    	else {
    		try {
				//TODO change sql
	            handle.createQuery(UPDATE_WORKFLOW_SQL)
	                         .bind("workflow_id", workflow.getWorkflowId())
	                         .bind("author", workflow.getAuthor())
	                         .bind("last_modifier", workflow.getLastModifier())
	                         .bind("modification_time", workflow.getModificationTime())
	                         .bind("name", workflow.getName())
	                         .bind("publish_time", workflow.getPublishTime())
	                         .bind("status", workflow.getStatus())
	                         .bind("type", workflow.getType());
	        } finally {
	            handle.close();
	        }
    	}


    }

    @Override
    public List<Workflow> listWorkflows(String type) {
        // TODO Auto-generated method stub
        return null;
    }
}