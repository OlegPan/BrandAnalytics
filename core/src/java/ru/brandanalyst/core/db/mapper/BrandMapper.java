package ru.brandanalyst.core.db.mapper;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import ru.brandanalyst.core.model.Brand;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: Dmitry Batkovich
 * Date: 09.10.11
 * Time: 17:17
 * Mapping brand from row of DB
 */
public class BrandMapper implements ParameterizedRowMapper<Brand> {
    public final Brand mapRow(final ResultSet resultSet, final int i) throws SQLException {
        try {
            return new Brand(Long.parseLong(resultSet.getString("Id")),
                    resultSet.getString("Name"),
                    resultSet.getString("Description"),
                    resultSet.getString("Website"),
                    Long.parseLong(resultSet.getString("BranchId")),resultSet.getString("FinamName"));
        } catch (Exception e) {
            return new Brand(Long.parseLong(resultSet.getString("Id")),
                    resultSet.getString("Name"),
                    resultSet.getString("Description"),
                    resultSet.getString("Website"), -1 ,"");
        }
    }
}
