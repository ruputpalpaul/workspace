package com.jivesoftware.ps.addons.jep.clm.domain;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

@Getter
public enum WorkflowType {
	Place("place"),
	Content("content");
	
	private String value;
	
	WorkflowType(String value){
		this.value = value;
	}
	
	public String toString() {
		return this.value;
	}
    private static final Map<String,WorkflowType> ENUM_MAP;

   
    static {
        Map<String,WorkflowType> map = new ConcurrentHashMap<String, WorkflowType>();
        for (WorkflowType instance : WorkflowType.values()) {
            map.put(instance.toString().toLowerCase(),instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static WorkflowType get (String value) {
        return ENUM_MAP.get(value.toLowerCase());
    }
}
