package com.jivesoftware.ps.addons.jep.clm.domain;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

@Getter
public enum WorkflowStatus {
	Active("active"),
	Inactive("inactive"),
	Deleted("deleted");


    private String value;


    private static final Map<String,WorkflowStatus> ENUM_MAP;

    WorkflowStatus(String value) {
		this.value = value;
	}

    public String toString() {
    	return this.value;
    }

    static {
        Map<String,WorkflowStatus> map = new ConcurrentHashMap<String, WorkflowStatus>();
        for (WorkflowStatus instance : WorkflowStatus.values()) {
            map.put(instance.toString().toLowerCase(),instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static WorkflowStatus get (String value) {
        return ENUM_MAP.get(value.toLowerCase());
    }

    public static WorkflowStatus get(String value) {
        return ENUM_MAP.get(value.toLowerCase())
    }

    public static void main()
    {
        //Set this fucntion up for returning the workflow whenever the class is called
    }
}
