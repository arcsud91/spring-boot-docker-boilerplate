package com.recro;

import com.mongodb.MongoClient;
import com.recro.config.MongoDBConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class MongoDBService {

    public static final int WRITE = 1;
    public static final int READ = 2;

    private final static Logger LOGGER = LoggerFactory.getLogger(MongoDBService.class);

    private MongoOperations readMongoOperations;
    private MongoOperations writeMongoOperations;

    @Autowired
    private MongoDBConfiguration mongoDBConfiguration;

    @Bean
    public MongoClient mongo() {
        return new MongoClient("mongodb://localhost:27017/test");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), mongoDBConfiguration.getDatabase());
    }

    public MongoOperations getMongoOperations(int operation) {
        if (READ == operation) {
            return this.readMongoOperations;
        } else if (WRITE == operation) {
            return this.writeMongoOperations;
        }
        return null;
    }

    public void insert(Collection<? extends Object> objects, String collection) {
        this.getMongoOperations(this.WRITE).insert(objects, collection);
    }

    public <T> void remove(Query query, Class<T> targetClass, String collection) {
        this.getMongoOperations(this.WRITE).remove(query, targetClass, collection);
    }
}