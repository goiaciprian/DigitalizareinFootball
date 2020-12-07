package com.OlimpiaComarnic.Backend.dao;

import com.OlimpiaComarnic.Backend.entity.Player;
import com.OlimpiaComarnic.Backend.entity.User;
import com.OlimpiaComarnic.Backend.utils.DBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PlayerDAO {

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
            e.printStackTrace();
        }
        return player;
    }

    /**
     * Add a new player in database
     *
     * @param player new player to add in database
     */
    public static synchronized CompletableFuture<Void> insertPlayer(Player player) {

        return CompletableFuture.runAsync(() -> {

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
    }

    /**
     * Update method replace in the old player the new values and puts then in db
     * //      @param currPlayer current player
     *
     * @param newPlayer updated player
     */
    public static synchronized CompletableFuture<Void> updateOne(String username, Player newPlayer) {

        return CompletableFuture.runAsync(() -> {

            List<Document> arrDoc = new ArrayList<>();
            for (Map.Entry<String, Integer> map : newPlayer.getAparitii().entrySet()) {
                arrDoc.add(
                        new Document()
                                .append(map.getKey(), map.getValue())
                );
            }

            if (!username.equals(newPlayer.getUsername())) {
                User toChangeUsername = UserDAO.findUser(username);
                if (toChangeUsername != null) {
                    toChangeUsername.setUsername(newPlayer.getUsername());
                    UserDAO.updateUserByUsername(username, toChangeUsername);
                }
            }

            Document toReplace = new Document()
                    .append("nume", newPlayer.getNume())
                    .append("username", newPlayer.getUsername())
                    .append("nrTricou", newPlayer.getNumarTricou())
                    .append("goluri", newPlayer.getGoluri())
                    .append("paseDeGol", newPlayer.getPaseGol())
                    .append("cartonaseGalbene", newPlayer.getCartonaseGalbene())
                    .append("cartonaseRosii", newPlayer.getCartonaseRosii())
                    .append("aparitii", arrDoc);


            MongoDatabase proiect = DBConnection.getDatabase();
            MongoCollection<Document> players = proiect.getCollection("players");
            players.replaceOne(Filters.eq("username", username), toReplace);

        });
    }

    /**
     * Delete player from database
     *
     * @param username the username associated with the player to delete
     */
    public static synchronized CompletableFuture<Void> deleteOne(String username) {
        return CompletableFuture.runAsync(() -> {

            MongoDatabase proiect = DBConnection.getDatabase();
            MongoCollection<Document> players = proiect.getCollection("players");
            players.deleteOne(Filters.eq("username", username));
            try {
                UserDAO.deleteUser(username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
