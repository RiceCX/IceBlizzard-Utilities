package dev.blizzardutils.hikari;

import com.zaxxer.hikari.HikariDataSource;
import dev.blizzardutils.example.ExamplePlugin;
import dev.blizzardutils.string.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HikariSetup {

    private static HikariSetup instance = null;
    private HikariDataSource dataSource;

    // Hikari Settings
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;

    public static synchronized HikariSetup getInstance() {
        if (instance == null) instance = new HikariSetup();
        return instance;
    }

    public void connectToDatabase(String host, int port, String database, String username, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        dataSource = new HikariDataSource();
        setDataSourceSettings();
        registerDefaultTables();
        Bukkit.getConsoleSender().sendMessage(StringUtils.format("&bBlizzard Utilities &7made a &aConnection&7!"));
    }

    /**
     * Register tables in the database
     */
    private void registerDefaultTables() {
        // Register tables

    }

    /**
     * Run a query asynchronously
     *
     * @param statement
     */
    public void runAsyncQuery(String statement) {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeQuery();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a table
     *
     * @param tableName Prepared Statement
     */
    private void createTable(String tableName) {
        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(tableName)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets default settings for hikari
     */
    private void setDataSourceSettings() {
        dataSource.setMaximumPoolSize(3);
        dataSource.setDataSourceClassName("com.mysql.jdbc.jdbc2.optional.MysqlDataSource");
        dataSource.addDataSourceProperty("serverName", host);
        dataSource.addDataSourceProperty("port", port);
        dataSource.addDataSourceProperty("databaseName", database);
        dataSource.addDataSourceProperty("user", username);
        dataSource.addDataSourceProperty("password", password);
    }
}
