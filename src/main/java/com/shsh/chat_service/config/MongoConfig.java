package com.shsh.chat_service.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Override
    protected String getDatabaseName() {
        return "chatdb";
    }

    @Override
    public MongoClient mongoClient() {
        System.out.println("Connecting to MongoDB...");
        MongoClient client = MongoClients.create("mongodb://chat-service-db:27017");
        System.out.println("Connected to MongoDB.");
        return client;
    }

    @Override
    protected boolean autoIndexCreation() {
        return true; // Создание индексов по умолчанию
    }
}
