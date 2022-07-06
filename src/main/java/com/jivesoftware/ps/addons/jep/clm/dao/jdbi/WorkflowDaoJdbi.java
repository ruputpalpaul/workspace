package com.jivesoftware.ps.addons.jep.clm.dao.jdbi;

import java.util.List;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;
import com.jivesoftware.ps.addons.jep.clm.dao.WorkflowDao;
import com.jivesoftware.ps.addons.jep.clm.dao.jdbi.mapper.WorkflowMapper;
import com.jivesoftware.ps.addons.jep.clm.domain.Workflow;

public interface WorkflowDaoJdbi extends WorkflowDao {
    @SqlQuery("select workflow_id, name, author, publish_time, type"
                    + " from clm_workflow where type: type;")
    
    @RegisterMapper(WorkflowMapper.class)
    public List<Workflow> listWorkflows(
        @Bind("type") String type
    );
}