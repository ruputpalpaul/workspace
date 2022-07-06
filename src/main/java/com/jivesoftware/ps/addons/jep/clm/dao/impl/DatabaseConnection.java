package com.jivesoftware.ps.addons.jep.clm.dao.impl;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;

public class DatabaseConnection {
	
	private final DBI dbi = new DBI("");
	Handle h = dbi.open();
	
}
