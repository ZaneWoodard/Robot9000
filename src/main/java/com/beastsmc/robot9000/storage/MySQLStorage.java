package com.beastsmc.robot9000.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.UUID;

public class MySQLStorage implements PersistentStorage {

    private final HikariDataSource ds;
    public MySQLStorage(String host, short port, String username, String password, String database) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(String.format("jdbc:mysql://%s:%d/%s", host, port, database));
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        this.ds = new HikariDataSource(config);
    }

    @Override
    public boolean contains(String hash) {
        try(Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQLStatements.CONTAINS_HASH.toString())
        ){
            ps.setString(1, hash);
            ResultSet rs = ps.executeQuery();
            return rs.first();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void store(String hash) {
        try(Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQLStatements.STORE_HASH.toString())
        ){
            ps.setString(1, hash);
            ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HashSet<String> loadCache(int N) {
        try(Connection conn = ds.getConnection();
            PreparedStatement ps = conn.prepareStatement(SQLStatements.LOAD_CACHE.toString())
        ){
            ps.setInt(1, N);
            ResultSet rs = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getViolationCount(UUID pid) {
        return 0;
    }

    @Override
    public long getViolationExpiration(UUID pid) {
        return 0;
    }

    @Override
    public int storeViolation(UUID pid) {
        return 0;
    }

    enum SQLStatements {
        CONTAINS_HASH("SELECT * FROM r9k_phrases WHERE hash=? LIMIT 1"),
        STORE_HASH("INSERT INTO r9k_phrases (hash, count) VALUES(?, 0) ON DUPLICATE KEY UPDATE count=count+1"),
        LOAD_CACHE("SELECT * FROM r9k_phrases ORDER BY count DESC LIMIT ?"),
        GET_VIOLATIONS("SELECT * FROM r9k_violations WHERE pid=? LIMIT 1"),
        STORE_VIOLATION("INSERT INTO r9k_violations (pid, 0, violations) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE count=count+1, expiration=?"),
        CREATE_TABLE_PHRASES(""), //TODO
        CREATE_TABLE_VIOLATIONS(""); //TODO

        private final String statement;
        SQLStatements(String s) {
            statement = s;
        }

        @Override
        public String toString() {
            return this.statement;
        }
    }
}
