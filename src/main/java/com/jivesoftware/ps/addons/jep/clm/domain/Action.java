package com.jivesoftware.ps.addons.jep.clm.domain;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Action{
    private final long actionId;
    private final long ruleId;
    private final Notification notification;
    private final String status;
    private final String type;
}