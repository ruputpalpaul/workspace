package com.jivesoftware.ps.addons.jep.clm.dao.jdbi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.jivesoftware.ps.addons.jep.clm.domain.Reviewer;
import com.jivesoftware.ps.addons.jep.clm.domain.Rule;
import com.jivesoftware.ps.addons.jep.clm.domain.Workflow;

public class WorkflowMapper extends BaseMapper implements ResultSetMapper<Workflow> {

    public Workflow map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {

        
       final Workflow workflow = new Workflow(
            resultSet.getLong("workflow_id"),
            resultSet.getString("author"),
            resultSet.getString("name"),
            resultSet.getLong("publish_time"),
            resultSet.getString("type"),
            List<Rule> rules(),
            List<ContentType> contentTypes(),
            new List<Reviewer> reviewers()
            );
        

        // ...and with every line we add one of the 3 tables
        Rule rule = new Rule(resultSet.getInt("rule_id"));
        if (rule.getId() > 0) {
            workflow.getRules().add(Rule);
        }

        return workflow;
    }
}