package com.jivesoftware.ps.addons.jep.clm.dao.jdbi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import com.jivesoftware.ps.addons.jep.clm.domain.Action;
import com.jivesoftware.ps.addons.jep.clm.domain.ContentType;
import com.jivesoftware.ps.addons.jep.clm.domain.Recipient;
import com.jivesoftware.ps.addons.jep.clm.domain.Reviewer;
import com.jivesoftware.ps.addons.jep.clm.domain.Rule;
import com.jivesoftware.ps.addons.jep.clm.domain.Trigger;
import com.jivesoftware.ps.addons.jep.clm.domain.Workflow;

public class WorkflowMapper extends BaseMapper implements ResultSetMapper<Workflow> {

    public Workflow map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {
        Workflow workflow = null;
        final Map<Long, Rule> rules = new HashMap<>();
        // final Map<Integer, Place> places = new HashMap<>(); Place is missing
        final Map<Long, ContentType> contentTypes = new HashMap<>();
        final Map<Long, Recipient> recipients = new HashMap<>();
        final Map<Long, Action> actions = new HashMap<>();
        
        while(resultSet.next()) {
            if (Objects.isNull(workflow)) {
                workflow = new Workflow(
            		resultSet.getLong("workflow_id"),
            		new ArrayList<Rule>(),        		
            		new ArrayList<ContentType>(), 
            		new ArrayList<Reviewer>(), 
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
            }
            
            final Long ruleId = resultSet.getLong("rule_id");
            if (!rules.containsKey(ruleId)) {
                final Rule rule = getRule(resultSet);
                rules.put(ruleId, rule);
                workflow.getRules().add(rule);
            }
            
            final Long actionId = resultSet.getLong("action_id");
            if (!actions.containsKey(actionId)) {
                final Action action = getAction(resultSet);
                actions.put(actionId, action);
                final Rule rule = rules.get(ruleId);
                rule.getActions().add(action);
            }
            
            // TODO: Handle places, content types, triggers and notifications and recipient types
        }

        return workflow;
    }
    
    private static Rule getRule(ResultSet resultSet) throws SQLException {
        return new Rule(
                        resultSet.getLong("rule_id"), 
                        resultSet.getLong("workflow_id"), 
                        new ArrayList<Action>(), 
                        new ArrayList<Trigger>(), 
                        resultSet.getLong("activation_time"), 
                        resultSet.getLong("end_time"), 
                        resultSet.getLong("executor_id"), 
                        resultSet.getLong("rule_modification_time"), 
                        resultSet.getString("rule_name"), 
                        resultSet.getLong("rule_publish_time"), 
                        resultSet.getString("rule_publish_status")
                    );
    }
    
    private static Action getAction(ResultSet resultSet) throws SQLException {
        // TODO: return new Action
        return null;
    }
}