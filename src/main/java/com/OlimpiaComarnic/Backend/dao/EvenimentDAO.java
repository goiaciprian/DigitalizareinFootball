package com.OlimpiaComarnic.Backend.dao;

import com.OlimpiaComarnic.Backend.entity.Eveniment;
import com.OlimpiaComarnic.Backend.utils.DBConnection;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class EvenimentDAO {

    /**
     * Get all events
     *
     * @return list of events
     */
    public synchronized static List<Eveniment> findAll() {

        List<Eveniment> all = new ArrayList<>();

        MongoDatabase db = DBConnection.getDatabase();
        MongoCollection<Document> events = db.getCollection("events");

        try (MongoCursor<Document> cursor = events.find(Filters.gt("data", new Date())).sort(Sorts.ascending("data")).iterator()) {
            while (cursor.hasNext()) {
                Document currEvent = cursor.next();
                Eveniment event = new Eveniment(currEvent.get("_id").toString());
                event.setEvent(currEvent.getString("event"));
                event.setDate(currEvent.getDate("data"));
                all.add(event);
            }
        }
        return all;
    }

    /**
     * Finds the next event
     *
     * @return Eveniment if found else null;
     */

    public synchronized static Eveniment getNextEvent() {
        Eveniment found = null;

        MongoDatabase db = DBConnection.getDatabase();
        MongoCollection<Document> events = db.getCollection("events");

        try (MongoCursor<Document> cursor = events.find(Filters.gt("data", new Date())).sort(Sorts.ascending("data")).iterator()) {
            if (cursor.hasNext()) {
                Document curr = cursor.next();
                found = new Eveniment(curr.get("_id").toString());
                found.setEvent(curr.getString("event"));
                found.setDate(curr.getDate("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return found;
    }

    /**
     * Finds event by id
     *
     * @param id the ObjectId from database
     * @return Event if found else null
     */
    public synchronized static Eveniment findOneById(String id) {
        Eveniment found = null;

        MongoDatabase db = DBConnection.getDatabase();
        MongoCollection<Document> events = db.getCollection("events");

        try (MongoCursor<Document> cursor = events.find(Filters.eq("_id", new ObjectId(id))).iterator()) {
            while (cursor.hasNext()) {
                Document curr = cursor.next();
                found = new Eveniment(curr.get("_id").toString());
                found.setEvent(curr.getString("event"));
                found.setDate(curr.getDate("data"));
            }
        }

        return found;
    }

    /**
     * Async method to add a new event in database
     *
     * @param event future event
     * @return Awaitable instance
     */
    public synchronized static CompletableFuture<Void> insertNewEvent(Eveniment event) {
        return CompletableFuture.runAsync(() -> {
            Document newEvent = new Document()
                    .append("event", event.getEvent())
                    .append("data", event.getDate());

            MongoDatabase db = DBConnection.getDatabase();
            MongoCollection<Document> events = db.getCollection("events");

            events.insertOne(newEvent);
        });
    }

    /**
     * Async method to update an event by id
     *
     * @param id      id of the element that has to be updated
     * @param updated the updated event
     * @return Awaitable instance
     */
    public synchronized static CompletableFuture<Void> updateEventById(String id, Eveniment updated) {
        return CompletableFuture.runAsync(() -> {
            Document updatedEvent = new Document()
                    .append("event", updated.getEvent())
                    .append("data", updated.getDate());

            MongoDatabase db = DBConnection.getDatabase();
            MongoCollection<Document> events = db.getCollection("events");

            events.replaceOne(Filters.eq("_id", new ObjectId(id)), updatedEvent);
        });
    }

    /**
     * Async method to delete an event
     *
     * @param id the ObjectId from database
     * @return Awaitable instance
     */
    public synchronized static CompletableFuture<Void> deleteEventById(String id) {
        return CompletableFuture.runAsync(() -> {
            MongoDatabase db = DBConnection.getDatabase();
            MongoCollection<Document> events = db.getCollection("events");
            events.deleteOne(Filters.eq("_id", new ObjectId(id)));
        });
    }

    /**
     * Delete past events
     */
    public static CompletableFuture<Void> checkPastEvents() {
        return CompletableFuture.runAsync(() -> {
            List<Eveniment> allEvents = findAll();
            List<Eveniment> toDelete = allEvents.stream()
                    .filter(event -> new Date().compareTo(event.getDate()) > 0)
                    .collect(Collectors.toList());
            toDelete.forEach(event -> {
                try {
                    deleteEventById(event.get_id()).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
