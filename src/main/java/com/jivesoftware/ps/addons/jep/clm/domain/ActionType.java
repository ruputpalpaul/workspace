package com.jivesoftware.ps.addons.jep.clm.domain;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ActionType {
	
	Archive("archive"),
	Delete("delete"),
	Notify("notify");
	
	private String value;
		
	ActionType(String value){
		this.value = value;
	}
	
	public String toString() {
		return this.value;
	}
    private static final Map<String,ActionType> ENUM_MAP;

   
    static {
        Map<String,ActionType> map = new ConcurrentHashMap<String, ActionType>();
        for (ActionType instance : ActionType.values()) {
            map.put(instance.toString().toLowerCase(),instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static ActionType get (String value) {
        return ENUM_MAP.get(value.toLowerCase());
    }
}
