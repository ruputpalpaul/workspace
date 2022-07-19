package com.jivesoftware.ps.addons.jep.clm.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Recipient{
    private final long recipientTypeId;
    private final long notificationId;
    private final String name;
    private final WorkflowStatus status;
    private final WorkflowStatus status;
    private final long notificationId;
    private final long recipientId;
    private final String name;
}