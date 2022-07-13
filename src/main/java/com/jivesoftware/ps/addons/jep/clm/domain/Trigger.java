package com.jivesoftware.ps.addons.jep.clm.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Trigger{
    private final long triggerId;
    private final long ruleId;
    private final TriggerType triggerType;
    private final long triggerValue;
    private final WorkflowStatus status;
}