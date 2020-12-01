package com.OlimpiaComarnic.Backend;

import com.OlimpiaComarnic.Backend.dao.PlayerDAO;
import com.OlimpiaComarnic.Backend.dao.UserDAO;
import com.OlimpiaComarnic.Backend.entity.Player;
import com.OlimpiaComarnic.Backend.entity.User;
import com.OlimpiaComarnic.Backend.utils.DBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.AfterClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for backend
 */
public class BackendTest {


    /**
     *  Players test
     *  Tests if findOne and findAll are working.
     */
    @Test
    public void findAllAndOnePlayers() {
        List<Player> all = PlayerDAO.findAll();
        int allSize = all.size()-1;

        Player expected = all.get(allSize);
        Player actual = PlayerDAO.findOne(all.get(allSize).getNume());

        assert actual != null: "findOne returned null";
        assertEquals(expected.toString(), actual.toString());

    }

    /**
     * Players test
     * Tests if insert, update and delete are working
     * @throws Exception if connection with the database fails
     */
    @Test
    public void insertUpdateDeleteOne() throws Exception {
        Player newPl = new Player("junitTest", "junitUsername", 1000),
                updatePl = new Player("junitTest", "jUnitUsername", 2000);

        PlayerDAO.insertPlayer(newPl);
        PlayerDAO.worker.join();
        assertTrue(fastFind(newPl));

        PlayerDAO.updateOne(newPl, updatePl);
        PlayerDAO.worker.join();
        assertTrue(fastFind(updatePl));

        PlayerDAO.deleteOne(updatePl);
        PlayerDAO.worker.join();
        assertFalse(fastFind(updatePl));

    }

    /**
     * Users test
     * Tests if findAll and findOne are working
     */
    @Test
    public void findOneAndAllUsers() {
        List<User> all = UserDAO.findAll();
        int allSize = all.size()-1;

        User expected = all.get(allSize);
        User actual = UserDAO.findUser(all.get(allSize).getUsername());

        assertEquals(expected.toString(), actual.toString());

    }

    /**
     * Users test
     * Tests if insert, update and delete are working
     * @throws Exception if connexion with database fails
     */
    @Test
    public void insertUpdateDeleteUser() throws Exception {
        User user1 = new User("test1", "paroa", false),
             user2 = new User("test2", "parola2", false);

        UserDAO.insertUser(user1);
        UserDAO.worker.join();
        assertTrue(fastFind(user1));

        UserDAO.updateUser(user1, user2);
        UserDAO.worker.join();
        assertTrue(fastFind(user2));

        UserDAO.deleteUser(user2);
        UserDAO.worker.join();
        assertFalse(fastFind(user2));

    }

    @AfterClass
    public static void closeDB() {
        DBConnection.closeConn();
    }

    private boolean fastFind(Object obj) {
        MongoDatabase proiect = DBConnection.getDatabase();
        if(obj instanceof Player) {
            Player pl = (Player) obj;
            assert proiect != null: "Error occurred when creating database connection";
            MongoCollection<Document> players = proiect.getCollection("players");

            try (MongoCursor<Document> cursor = players.find().iterator()) {
                while (cursor.hasNext()) {
                    Document currPlayer = cursor.next();
                    String currPlayerName = currPlayer.getString("nume");
                    int currPlayerNrTricou = currPlayer.getInteger("nrTricou");
                    if (currPlayerName.equals(pl.getNume()) && currPlayerNrTricou == pl.getNumarTricou())
                        return true;
                }
            }
        }
        if(obj instanceof User) {
            User user = (User) obj;
            assert proiect != null: "Error occurred when creating database connection";
            MongoCollection<Document> users = proiect.getCollection("users");

            try (MongoCursor<Document> cursor = users.find().iterator()) {
                while (cursor.hasNext()) {
                    Document currUser = cursor.next();
                    String currUsername = currUser.getString("username");
                    String currPassword = currUser.getString("password");
                    if(user.getUsername().equals(currUsername) && user.getEncPassword().equals(currPassword))
                        return true;
                }
            }
        }

        return false;
    }

}
