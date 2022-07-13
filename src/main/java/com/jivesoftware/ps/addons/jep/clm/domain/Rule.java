package com.jivesoftware.ps.addons.jep.clm.domain;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Rule{
    private final long ruleId;
    private final long workflowId;
    private final List<Action> actions;
    private final Trigger trigger;
    private final long executorId;
    private final Date modificationTime;
    private final String name;
    private final Date publishTime;
    private final WorkflowStatus status;
}