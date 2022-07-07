package com.jivesoftware.ps.addons.jep.clm.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ContentType{
    private final long contentTypeId;
    private final long workflowId;
    private final String status;
    private final String name;
}