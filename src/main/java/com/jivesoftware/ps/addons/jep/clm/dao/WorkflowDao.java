package com.jivesoftware.ps.addons.jep.clm.dao;

import com.jivesoftware.ps.addons.jep.clm.domain.Workflow;
import com.jivesoftware.ps.addons.jep.clm.domain.WorkflowType;

import java.util.List;

public interface WorkflowDao extends Dao {   
    Workflow getById(long workflowId);

    void Delete(Workflow workflow);

    void saveWorkflow(Workflow workflow);


	List<Workflow> listWorkflows(WorkflowType type, Integer startIndex, Integer count);

}