package com.jivesoftware.ps.addons.jep.clm.dao.jdbi.mapper;

import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import com.jivesoftware.ps.addons.jep.clm.domain.Workflow;
import com.jivesoftware.ps.addons.jep.clm.domain.Rule;


public class WorkflowMapper extends BaseMapper implements ResultSetMapper<Workflow> {

    public Workflow map(int index, ResultSet resultSet, StatementContext ctx) throws SQLException {

        if (index == 0) {
            final Workflow workflow = new Workflow(
            getLongValue(resultSet, "workflow_id"),
            resultSet.getString("author"),
            resultSet.getString("name"),
            getLongValue(resultSet, "publish_time"),
            resultSet.getString("type"),
            new List<Rule> rules(),
            new List<ContentType> contentTypes(),
            new List<Reviewer> reviewers(),
        );
        }

        // ...and with every line we add one of the 3 tables
        Rule rule = new Rule(resultSet.getInt("rule_id"));
        if (rule.getId() > 0) {
            workflow.getRules().add(Rule);
        }

        return workflow;
    }
}