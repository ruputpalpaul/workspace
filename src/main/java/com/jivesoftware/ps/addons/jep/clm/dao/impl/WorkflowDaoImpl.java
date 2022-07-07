package com.jivesoftware.ps.addons.jep.clm.dao.impl;

import java.util.List;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import com.jivesoftware.ps.addons.jep.clm.dao.WorkflowDao;
import com.jivesoftware.ps.addons.jep.clm.dao.impl.mapper.WorkflowMapper;
import com.jivesoftware.ps.addons.jep.clm.domain.Workflow;
import lombok.RequiredArgsConstructor;

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
        // TODO Auto-generated method stub

    }

    @Override
    public List<Workflow> listWorkflows(String type) {
        // TODO Auto-generated method stub
        return null;
    }
}
