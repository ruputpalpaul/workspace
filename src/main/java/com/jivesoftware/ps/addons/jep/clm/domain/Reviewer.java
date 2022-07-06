package com.jivesoftware.ps.addons.jep.clm.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Reviewer {
    private final long reviewerId;   
    private final long workflowId;
    private final String reviewerUserId;                                                  
    private final String status;                                 
}
