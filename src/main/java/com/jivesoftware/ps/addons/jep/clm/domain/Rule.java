package com.jivesoftware.ps.addons.jep.clm.domain;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.jivesoftware.ps.addons.jep.clm.domain.Action;
import com.jivesoftware.ps.addons.jep.clm.domain.Trigger;

@Getter
@RequiredArgsConstructor
public class Rule{
    private final long ruleId;
    private final long workflowId;
    private final List<Action> actions;
    private final List<Trigger> triggers;
    private final long activationTime;
    private final long endTime;
    private final long executorId;
    private final long modificationTime;
    private final String name;
    private final long publishTime;
    private final String status;
}