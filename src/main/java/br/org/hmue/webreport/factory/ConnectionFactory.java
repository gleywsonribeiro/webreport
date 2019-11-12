/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.org.hmue.webreport.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionFactory {

    private static final String USERNAME = "dbamv";
    private static final String PASSWORD = "prosaude1973";
    private static final String SERVER = "10.79.100.15";
    private static final String PORT = "1521";
    private static final String DATABASE = "prd";
    //Dados de caminho, porta e nome da base de dados que irá ser feita a conexão
    private static final String DATABASE_URL = "jdbc:oracle:thin:@" + SERVER + ":" + PORT + ":" + DATABASE;

    public static Connection createConnectionToOracle() {
        Connection connection = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return connection;
    }

    public static void main(String[] args) throws Exception {

        //Recupera uma conexão com o banco de dados
        Connection con = createConnectionToOracle();

        //Testa se a conexão é nula
        if (con != null) {
            System.out.println("Conexão obtida com sucesso!" + con);
            con.close();
        }

    }
}
