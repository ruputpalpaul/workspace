package com.jivesoftware.ps.addons.jep.clm.domain;

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
    private final String author;
    private final String lastModifier;
    private final long modificationTime;
    private final String name;
    private final long publishTime;
    private final String status;
    private final String type;
}
