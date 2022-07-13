package com.jivesoftware.ps.addons.jep.clm.domain;

import java.util.Date;
import java.util.List;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Workflow {
	private final long workflowId;
    private final List<Rule> rules;
    private final List<ContentType> contentTypes;
    private final List<Reviewer> reviewers;
    private final List<Place> places;
    private final Integer author;
    private final Integer lastModifier;
    private final Date modificationTime;
    private final String name;
    private final Date publishTime;
    private final WorkflowStatus status;
    private final WorkflowType type;
}
