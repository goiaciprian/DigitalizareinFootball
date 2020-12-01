package com.OlimpiaComarnic.Backend;

import com.OlimpiaComarnic.Backend.dao.PlayerDAO;
import com.OlimpiaComarnic.Backend.entity.Player;
import com.OlimpiaComarnic.Backend.utils.DBConnection;


/**
 * Clasa main pentru teste in backend
 */
public class Main {
    public static void main(String[] args) {

        Player pl = PlayerDAO.findOneByUsername("user");
        Player pl2 = PlayerDAO.findOneByUsername("user");

//        pl.addAparitie(44, "meci4");
//        pl.addAparitie(99, "meci5");
//        pl.addAparitie(64, "meci6");
//        pl.addAparitie(91, "meci7");
//        pl.addAparitie(88, "meci8");
//        pl.addAparitie(90, "meci9");
//        pl.addAparitie(55, "meci10");
//
//        PlayerDAO.updateOne(pl2, pl);

        DBConnection.closeConn();
    }
}
