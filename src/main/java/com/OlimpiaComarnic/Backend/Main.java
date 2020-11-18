package com.OlimpiaComarnic.Backend;

import com.OlimpiaComarnic.Backend.dao.UserDAO;
import com.OlimpiaComarnic.Backend.entity.User;


/**
 * Clasa main pentru teste in backend
 */
public class Main {
    public static void main(String[] args) {

    User normalUser = new User("normalUser", "normalUser", false);
    UserDAO.insertUser(normalUser);


    }
}
