package com.espressoprogrammer.references;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.sql.Time;
import java.util.List;

@Repository
public class ReferencesCountDAO {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initialize() {
        logger.info("Creating references_count table");

        jdbcTemplate.execute("DROP TABLE references_count IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE references_count (created TIME, soft_references_count INTEGER, weak_references_count INTEGER, phantom_references_count INTEGER)");
    }

    public void deleteAll() {
        logger.info("Delete all from references_count table");

        jdbcTemplate.execute("DELETE FROM references_count");
    }

    public void insert(ReferencesCount referencesCount) {
        jdbcTemplate.update("INSERT INTO references_count(created, soft_references_count, weak_references_count, phantom_references_count) " +
                "VALUES (?,?,?,?)", new Time(System.currentTimeMillis()),
                referencesCount.getSoftReferencesCount(),
                referencesCount.getWeakReferencesCount(),
                referencesCount.getPhantomReferencesCount());

    }

    public List<ReferencesCount> retrieveAll() {
        logger.info("Retrieve all from references_count table");

        return jdbcTemplate.query(
                "SELECT created, soft_references_count, weak_references_count, phantom_references_count FROM references_count ORDER BY created",
                (rs, rowNum) -> new ReferencesCount(rs.getTime("created"),
                        rs.getInt("soft_references_count"),
                        rs.getInt("weak_references_count"),
                        rs.getInt("phantom_references_count")));
    }
}
