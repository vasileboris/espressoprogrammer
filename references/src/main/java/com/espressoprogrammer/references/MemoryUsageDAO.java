package com.espressoprogrammer.references;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@Repository
public class MemoryUsageDAO {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initialize() {
        logger.info("Creating memory_usage table");

        jdbcTemplate.execute("DROP TABLE memory_usage IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE memory_usage (created TIME, total_memory BIGINT, free_memory BIGINT)");
    }

    public void deleteAll() {
        logger.info("Delete all from memory_usage table");

        jdbcTemplate.execute("DELETE FROM memory_usage");
    }

    public void insert(MemoryUsage memoryUsage) {
        jdbcTemplate.update("INSERT INTO memory_usage(created, total_memory, free_memory) " +
                "VALUES (?,?,?)", memoryUsage.getCreated(),
                memoryUsage.getTotalMemory(),
                memoryUsage.getFreeMemory());

    }

    public List<MemoryUsage> retrieveAll() {
        logger.info("Retrieve all from memory_usage table");

        return jdbcTemplate.query(
                "SELECT created, total_memory, free_memory FROM memory_usage ORDER BY created",
                (rs, rowNum) -> new MemoryUsage(rs.getTime("created"),
                        rs.getLong("total_memory"),
                        rs.getLong("free_memory")));
    }
}
