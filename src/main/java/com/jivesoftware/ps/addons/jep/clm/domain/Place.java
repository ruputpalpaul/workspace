package com.jivesoftware.ps.addons.jep.clm.domain;


import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Place {
	private final long placeId; 
	private final long workflowId;
	private final String url;
	private final long jiveId;
	private final long jivePlaceId;
    private final String name;                                                 
    private final WorkflowStatus status;                  
    private final String type;                      
}
