package com.foro.app;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;

@SpringBootTest
class AppApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() {
        System.out.println("--- DIAGNOSTICO DE BASE DE DATOS ---");
        try {
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(
                "SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE, COLUMN_TYPE " +
                "FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA = 'foro_db'"
            );
            for (Map<String, Object> col : columns) {
                System.out.println("COL_INFO: " + col.get("TABLE_NAME") + "." + col.get("COLUMN_NAME") + " -> " + col.get("COLUMN_TYPE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
