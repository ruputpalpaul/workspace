package com.jivesoftware.ps.addons.jep.clm.domain;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

@Getter
public enum TriggerType {
	Archived("archived"),
	LastModified("lastModified"),
	NoPlaceAdmin("noPlaceAdmin"),
	LastActivity("lastActivity"),
	Created("created");


	private String value;
	
	TriggerType(String value){
		this.value = value;
	}
	
	public String toString() {
		return this.value;
	}
    private static final Map<String,TriggerType> ENUM_MAP;

   
    static {
        Map<String,TriggerType> map = new ConcurrentHashMap<String, TriggerType>();
        for (TriggerType instance : TriggerType.values()) {
            map.put(instance.toString().toLowerCase(),instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static TriggerType get (String value) {
        return ENUM_MAP.get(value.toLowerCase());
    }
}