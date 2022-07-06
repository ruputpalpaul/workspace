package com.jivesoftware.ps.addons.jep.clm.domain;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.jivesoftware.ps.addons.jep.clm.domain.ContentType;
import com.jivesoftware.ps.addons.jep.clm.domain.Place;
import com.jivesoftware.ps.addons.jep.clm.domain.Rule;
import com.jivesoftware.ps.addons.jep.clm.domain.Reviewer;

@Getter
@RequiredArgsConstructor
public class Workflow {
	private final long workflowId; 
    private final List<Rule> rules;
    private final List<ContentType> contentTypes;
    private final List<Reviewer> reviewers;
    private final List<Place> places;
    private final long activationTime;
    private final String author;               
    private final String category;                     
    private final long endTime;                                         
    private final String lastModifier;                
    private final long modificationTime;                                 
    private final String name;              
    private final long publishTime;                                     
    private final String status;                  
    private final String type;                      
}
