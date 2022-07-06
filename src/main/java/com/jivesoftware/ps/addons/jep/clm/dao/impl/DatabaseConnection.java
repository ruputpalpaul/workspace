package com.jivesoftware.ps.addons.jep.clm.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;


public class DatabaseConnection {
	
	private final DBI dbi = new DBI("link of db goes here");
	List<String> result = null;
	
	List<String> getDatabase() {
		
		Handle h = dbi.open();
		
		List<String> result = h.createQuery("select name from something order by id")
					.map(String.class)
					.list();
        h.close();

        return result;
	}
	
}
