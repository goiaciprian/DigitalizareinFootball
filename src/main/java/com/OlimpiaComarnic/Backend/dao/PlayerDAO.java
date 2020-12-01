package com.OlimpiaComarnic.Backend.dao;

import com.OlimpiaComarnic.Backend.entity.Player;
import com.OlimpiaComarnic.Backend.utils.DBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDAO {

    public static Thread worker;

    /**
     * Method that returns all players from database
     * @return list of all players
     */
    public static List<Player> findAll() {
        List<Player> rez = new ArrayList<>();


        MongoDatabase proiect = DBConnection.getDatabase();
        MongoCollection<Document> collection = proiect.getCollection("players");

        try(MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                Document currPlayer = cursor.next();
                Player player = new Player();
                player.setUsername(currPlayer.getString("username"));
                player.setNume(currPlayer.getString("nume"));
                player.setNumarTricou(currPlayer.getInteger("nrTricou"));
                player.setGoluri(currPlayer.getInteger("goluri"));
                player.setPaseGol(currPlayer.getInteger("paseDeGol"));
                player.setCartonaseRosii(currPlayer.getInteger("cartonaseRosii"));
                player.setCartonaseGalbene(currPlayer.getInteger("cartonaseGalbene"));

                List<Document> aparitiiDB = currPlayer.getList("aparitii",Document.class);
                for(Document doc : aparitiiDB) {
                    for(Map.Entry<String, Object> KeyValAparitii : doc.entrySet()) {
                        player.addAparitie((Integer) KeyValAparitii.getValue(), KeyValAparitii.getKey());
                    }
                }
                rez.add(player);
            }
        } catch (Exception ignored) {
            System.err.println("Error in findAll players");
        }

        return rez;
    }

    /**
     * Finds and return player from database by name
     * @param playerName used to search in database
     * @return player if found, null if not
     */
    public static Player findOne(String playerName) {
        Player player = null;
        MongoDatabase proiect = DBConnection.getDatabase();
        MongoCollection<Document> players = proiect.getCollection("players");

        try (MongoCursor<Document> cursor = players.find().iterator()) {
            while (cursor.hasNext()) {
                Document currPlayer = cursor.next();
                String currPlayerName = currPlayer.getString("nume");
                if(currPlayerName.equals(playerName)) {
                    player = new Player(currPlayerName, currPlayer.getString("username"), currPlayer.getInteger("nrTricou"));
                    player.setCartonaseGalbene(currPlayer.getInteger("cartonaseGalbene"));
                    player.setCartonaseRosii(currPlayer.getInteger("cartonaseRosii"));
                    player.setGoluri(currPlayer.getInteger("goluri"));
                    player.setPaseGol(currPlayer.getInteger("paseDeGol"));

                    List<Document> aparitiiDB = currPlayer.getList("aparitii",Document.class);
                    for(Document doc : aparitiiDB) {
                        for(Map.Entry<String, Object> KeyValAparitii : doc.entrySet()) {
                            player.addAparitie((Integer) KeyValAparitii.getValue(), KeyValAparitii.getKey());
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            System.err.println("Error in findOne player");
        }
        return player;
    }

    /**
     * Finds an player by it's associated username
     *
     * @param username associated with user account
     * @return user if found else null
     */
    public static Player findOneByUsername(String username) {
        Player player = null;
        MongoDatabase proiect = DBConnection.getDatabase();
        MongoCollection<Document> players = proiect.getCollection("players");

        try (MongoCursor<Document> cursor = players.find().iterator()) {
            while (cursor.hasNext()) {
                Document currPlayer = cursor.next();
                String currPlayerUsername = currPlayer.getString("username");
                if (currPlayerUsername.equals(username)) {
                    player = new Player(currPlayer.getString("nume"), currPlayerUsername, currPlayer.getInteger("nrTricou"));
                    player.setCartonaseGalbene(currPlayer.getInteger("cartonaseGalbene"));
                    player.setCartonaseRosii(currPlayer.getInteger("cartonaseRosii"));
                    player.setGoluri(currPlayer.getInteger("goluri"));
                    player.setPaseGol(currPlayer.getInteger("paseDeGol"));

                    List<Document> aparitiiDB = currPlayer.getList("aparitii", Document.class);
                    for (Document doc : aparitiiDB) {
                        for (Map.Entry<String, Object> KeyValAparitii : doc.entrySet()) {
                            player.addAparitie((Integer) KeyValAparitii.getValue(), KeyValAparitii.getKey());
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return player;
    }

    /**
     * Add a new player in database
     *
     * @param player new player to add in database
     */
    public static synchronized void insertPlayer(Player player) {

        worker = new Thread(() -> {

            try {
                assert findOne(player.getNume()) == null : "Player already in database";
            } catch (AssertionError err) {
                System.err.println(err.getMessage());
                return;
            }

            MongoDatabase proiect = DBConnection.getDatabase();
            MongoCollection<Document> players = proiect.getCollection("players");

            List<Document> arrDoc = new ArrayList<>();
            for(Map.Entry<String, Integer> map: player.getAparitii().entrySet()) {
                arrDoc.add(
                        new Document()
                        .append(map.getKey(), map.getValue())
                );
            }

            Document playerDB = new Document()
                    .append("nume", player.getNume())
                    .append("username", player.getUsername())
                    .append("nrTricou", player.getNumarTricou())
                    .append("goluri", player.getGoluri())
                    .append("paseDeGol", player.getPaseGol())
                    .append("cartonaseGalbene", player.getCartonaseGalbene())
                    .append("cartonaseRosii", player.getCartonaseRosii())
                    .append("aparitii", arrDoc);

            players.insertOne(playerDB);

        });
        worker.start();
    }

    /**
     * Update method replace in the old player the new values and puts then in db
     //      @param currPlayer current player
     * @param newPlayer updated player
     */
    public static synchronized void updateOne(Player currPlayer, Player newPlayer) {

        worker = new Thread( () -> {

            String currNume = currPlayer.getNume();
            String currUsername = currPlayer.getUsername();
            int currNrTricou = currPlayer.getNumarTricou();
            int currNrPase = currPlayer.getNumarTricou();
            int currGoluri = currPlayer.getGoluri();
            int currGalbene = currPlayer.getCartonaseGalbene();
            int currRosii = currPlayer.getCartonaseRosii();
            HashMap<String, Integer> currAparitii = currPlayer.getAparitii();

            String newNume = newPlayer.getNume();
            String newUsername = newPlayer.getUsername();
            int newNrTricou = newPlayer.getNumarTricou();
            int newNrPase = newPlayer.getNumarTricou();
            int newGoluri = newPlayer.getGoluri();
            int newGalbene = newPlayer.getCartonaseGalbene();
            int newRosii = newPlayer.getCartonaseRosii();
            HashMap<String, Integer> newAparitii = newPlayer.getAparitii();

            MongoDatabase proiect = DBConnection.getDatabase();
            MongoCollection<Document> players = proiect.getCollection("players");

            if (!currNume.equals(newNume)) {
                players.updateOne(Filters.eq("nume", currNume), Updates.set("nume", newNume));
            }
            if (!currUsername.equals(newUsername)) {
                players.updateOne(Filters.eq("username", currUsername), Updates.set("username", newUsername));
            }
            if (currNrTricou != newNrTricou) {
                players.updateOne(Filters.eq("nrTricou", currNrTricou), Updates.set("nrTricou", newNrTricou));
            }
            if (currNrPase != newNrPase) {
                players.updateOne(Filters.eq("paseDeGol", currNrPase), Updates.set("paseDeGol", newNrPase));
            }
            if (currGoluri != newGoluri) {
                players.updateOne(Filters.eq("goluri", currGoluri), Updates.set("goluri", newGoluri));
            }
            if (currGalbene != newGalbene) {
                players.updateOne(Filters.eq("cartonaseGalbene", currGalbene), Updates.set("cartonaseGalbene", newGalbene));
            }
            if (currRosii != newRosii) {
                players.updateOne(Filters.eq("cartonaseRosii", currRosii), Updates.set("cartonaseRosii", newRosii));
            }
            if(!currAparitii.equals(newAparitii)) {
                List<Document> currAparitiiList = new ArrayList<>();
                for(Map.Entry<String, Integer> currApar: currAparitii.entrySet()) {
                    currAparitiiList.add(new Document().append(currApar.getKey(), currApar.getValue()));
                }
                List<Document> newAparitiiList = new ArrayList<>();
                for(Map.Entry<String, Integer> newApar: newAparitii.entrySet()) {
                    newAparitiiList.add( new Document().append(newApar.getKey(), newApar.getValue()));
                }
                players.updateMany(Filters.eq("aparitii", currAparitiiList), Updates.set("aparitii", newAparitiiList));
            }

        });
        worker.start();
    }

    /**
     *  Delete player from database
     * @param player player to delete
     */
    public static synchronized void deleteOne(Player player) {
        worker = new Thread( () -> {

                MongoDatabase proiect = DBConnection.getDatabase();
                MongoCollection<Document> players = proiect.getCollection("players");
                players.deleteOne(
                        Filters.and(
                                Filters.eq("nume", player.getNume()),
                                Filters.eq("nrTricou", player.getNumarTricou())
                        )
                );
        });
        worker.start();
    }
}
