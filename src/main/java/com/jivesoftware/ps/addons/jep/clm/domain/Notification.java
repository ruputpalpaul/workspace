package com.jivesoftware.ps.addons.jep.clm.domain;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Notification{
    private final long notificationId;
    private final long actionId;
    private final List<Recipient> recipients;
    private final String subject;
    private final String text;
    private final WorkflowStatus status;
}