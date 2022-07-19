
package systemaconcesionario;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionSQL {
    Connection connect = null;
    String server = "";
    String database = "";
    private final String driver = "com.mysql.jdbc.Driver";

    public Connection Connect() {
        try {
            Class.forName(driver);
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/concesionaria","root","");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ConnectionSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return connect;
    }
       
    Connection con = this.Connect();
    
    public ResultSet getData(String sql) {
        ResultSet resultado = null;
        try {           
            PreparedStatement estado = con.prepareStatement(sql);
            resultado = estado.executeQuery();       
            return resultado;
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resultado;
    }
    
    public boolean setData(String sql) {
            boolean ready = false;         
        try {  
            PreparedStatement state = con.prepareStatement(sql);
            state.execute();
            ready = true;
            return ready;
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ready;
    }
}
