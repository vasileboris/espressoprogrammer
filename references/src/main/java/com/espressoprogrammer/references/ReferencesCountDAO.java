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
        jdbcTemplate.execute("CREATE TABLE references_count (created TIME, hard_references_count INTEGER, soft_referents_count INTEGER, weak_referents_count INTEGER, phantom_referents_count INTEGER)");
    }

    public void deleteAll() {
        logger.info("Delete all from references_count table");

        jdbcTemplate.execute("DELETE FROM references_count");
    }

    public void insert(ReferencesCount referencesCount) {
        jdbcTemplate.update("INSERT INTO references_count(created, hard_references_count, soft_referents_count, weak_referents_count, phantom_referents_count) " +
                "VALUES (?,?,?,?,?)", new Time(System.currentTimeMillis()),
                referencesCount.getHardReferencesCount(),
                referencesCount.getSoftReferentsCount(),
                referencesCount.getWeakReferentsCount(),
                referencesCount.getPhantomReferentsCount());

    }

    public List<ReferencesCount> retrieveAll() {
        logger.info("Retrieve all from references_count table");

        return jdbcTemplate.query(
                "SELECT created, hard_references_count, soft_referents_count, weak_referents_count, phantom_referents_count FROM references_count ORDER BY created",
                (rs, rowNum) -> new ReferencesCount(rs.getTime("created"),
                        rs.getInt("hard_references_count"),
                        rs.getInt("soft_referents_count"),
                        rs.getInt("weak_referents_count"),
                        rs.getInt("phantom_referents_count")));
    }
}
