package com.sof.plugins.core.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DatabaseManager {

    private final MongoDatabase database;
    private final MongoClient connection;
    public DatabaseManager(String uri, String databaseName) {
        connection = MongoClients.create(uri);
        database = connection.getDatabase(databaseName);
    }

    public MongoClient getConnection() {
        return connection;
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    public void disable() {
        connection.close();
    }
}