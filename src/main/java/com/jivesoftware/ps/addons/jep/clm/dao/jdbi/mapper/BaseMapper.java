package com.jivesoftware.ps.addons.jep.clm.dao.jdbi.mapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

abstract class BaseMapper {
    protected Long getLongValue(ResultSet resultSet, String column) throws SQLException {
        BigDecimal value = resultSet.getBigDecimal(column);
        return value == null ? null : value.longValue();
    }
}
