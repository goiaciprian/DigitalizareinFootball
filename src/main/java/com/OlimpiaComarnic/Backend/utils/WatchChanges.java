package com.OlimpiaComarnic.Backend.utils;

import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class WatchChanges {

    private static final List<String> collectionsWatching = new ArrayList<>();

    public static CompletableFuture<Void> WatchCollectionChanges(String collectionName, Consumer<ChangeStreamDocument<Document>> callback) {

        return CompletableFuture.runAsync(() -> {
            try {
                if(!collectionsWatching.contains(collectionName)) {
                    collectionsWatching.add(collectionName);
                    MongoDatabase db = DBConnection.getDatabase();
                    MongoCollection<Document> collection = db.getCollection(collectionName);

                    ChangeStreamIterable<Document> changeStream = collection.watch();

                    changeStream.forEach((Consumer<ChangeStreamDocument<Document>>) callback::accept);

                }
            } catch (Exception err) {
                System.out.println(err.getMessage());
            }
        });
    }


}
