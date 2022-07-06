package com.jivesoftware.ps.addons.jep.clm.dao;

import com.jivesoftware.ps.addons.jep.clm.domain.Workflow;

import java.util.List;

public interface WorkflowDao extends Dao {   
    Workflow getById(long workflowId);

    void Update(Workflow workflow);

    void Delete(Workflow workflow);

    void saveWorkflow(Workflow workflow);

	List<Workflow> listWorkflows(String type);

}