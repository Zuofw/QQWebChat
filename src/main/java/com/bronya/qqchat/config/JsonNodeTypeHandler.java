package com.bronya.qqchat.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JsonNodeTypeHandler extends BaseTypeHandler<JsonNode> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JsonNode parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.toString());
    }

    @Override
    public JsonNode getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String jsonSource = rs.getString(columnName);
        return parseJson(jsonSource);
    }

    @Override
    public JsonNode getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String jsonSource = rs.getString(columnIndex);
        return parseJson(jsonSource);
    }

    @Override
    public JsonNode getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String jsonSource = cs.getString(columnIndex);
        return parseJson(jsonSource);
    }

    private JsonNode parseJson(String jsonSource) {
        try {
            return objectMapper.readTree(jsonSource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}