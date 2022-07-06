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

        
        Workflow workflow = new Workflow(index, null, null, null, index, null, null, index, null, index, null, index, null, null);
        
          
        

        // ...and with every line we add one of the 3 tables
        Rule rule = new Rule(index, index, null, null, index, index, index, index, null, index, null);
        if (rule.getRuleId() > 0) {
            workflow.getRules().add(rule);
        }

        return workflow;
    }
}