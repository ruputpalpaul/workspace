package com.jivesoftware.ps.addons.jep.clm.dao.jdbi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.jivesoftware.ps.addons.jep.clm.domain.ContentType;
import com.jivesoftware.ps.addons.jep.clm.domain.Reviewer;
import com.jivesoftware.ps.addons.jep.clm.domain.Rule;
import com.jivesoftware.ps.addons.jep.clm.domain.Workflow;

public class WorkflowMapper extends BaseMapper implements ResultSetMapper<Workflow> {

    public Workflow map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {

        
        Workflow workflow = new Workflow(
    		resultSet.getLong("workflow_id"),
    		null,        		
    		null, 
    		null, 
    		resultSet.getLong("activation_time"), 
    		resultSet.getString("author"), 
    		resultSet.getString("category"), 
    		resultSet.getLong("end_time"), 
    		resultSet.getString("last_modifier"), 
    		resultSet.getLong("modification_time"), 
    		resultSet.getString("name"), 
    		resultSet.getLong("publish_time"), 
    		resultSet.getString("status"), 
    		resultSet.getString("type")
    	);
        
        

        // ...and with every line we add one of the 3 tables
        Rule rule = new Rule(
        		resultSet.getLong("rule_id"), 
        		resultSet.getLong("workflow_id"), 
        		null, 
        		null, 
        		resultSet.getLong("activation_time"), 
        		resultSet.getLong("end_time"), 
        		resultSet.getLong("executor_id"), 
        		resultSet.getLong("rule_modification_time"), 
        		resultSet.getString("rule_name"), 
        		resultSet.getLong("rule_publish_time"), 
        		resultSet.getString("rule_publish_status")
        	);
        
        if (rule.getRuleId() > 0) {
            workflow.getRules().add(rule);
        }

        return workflow;
    }
}