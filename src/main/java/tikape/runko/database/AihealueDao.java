/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tikape.runko.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import tikape.runko.domain.Aihealue;

public class AihealueDao implements Dao<Aihealue, Integer> {

    private Database database;

    public AihealueDao(Database database) {
        this.database = database;
    }

    @Override
    public Aihealue findOne(Integer key) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihealue WHERE id = ?;");
        stmt.setObject(1, key);

        ResultSet rs = stmt.executeQuery();
        boolean hasOne = rs.next();
        if (!hasOne) {
            return null;
        }

        Integer id = rs.getInt("id");
        String nimi = rs.getString("otsikko");

        Aihealue aihealue = new Aihealue(id, nimi);

        rs.close();
        stmt.close();
        connection.close();

        return aihealue;
    }

    @Override
    public List<Aihealue> findAll() throws SQLException {

        Connection connection = database.getConnection();
        PreparedStatement stmt = connection.prepareStatement("SELECT * FROM Aihealue;");

        ResultSet rs = stmt.executeQuery();
        List<Aihealue> aihealueet = new ArrayList<>();
        while (rs.next()) {
            Integer id = rs.getInt("id");
            String nimi = rs.getString("otsikko");

            aihealueet.add(new Aihealue(id, nimi));
        }

        rs.close();
        stmt.close();
        connection.close();

        return aihealueet;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM Aihealue WHERE id = ?;");
        stmt.setObject(1, key);
        stmt.execute();
        
        stmt.close();
        
        //poistetaan myös viestiketjut, ks viestiketjuDao 
        ViestiketjuDao vkd = new ViestiketjuDao(this.database);
        stmt = conn.prepareStatement("SELECT * FROM Viestiketju WHERE aihealue_id = ?;");
        stmt.setObject(1, key);
        ResultSet rs = stmt.executeQuery();
        
        stmt.close();
        
        while (rs.next()) {
            Integer id = rs.getInt("id");
            vkd.delete(id);
        }
        stmt.close();
        conn.close();

    }

    public void create(String nimi) throws SQLException {
        Connection conn = database.getConnection();
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Aihealue(otsikko) VALUES(?)");
        stmt.setObject(1, nimi);

        stmt.execute();
        stmt.close();
        conn.close();

    }

}
