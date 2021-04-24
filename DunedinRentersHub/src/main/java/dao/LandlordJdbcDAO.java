package dao;

import domain.Landlord;
import domain.Property;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
 
/**
 *
 * @author jake
 */
public class LandlordJdbcDAO {
 
    private String databaseURI = DbConnection.getDefaultConnectionUri();
 
    // default construcot
    public LandlordJdbcDAO() {
    }
 
    // constructor that intialises the URI
    public LandlordJdbcDAO(String databaseURI) {
        this.databaseURI = databaseURI;
    }
 
    // method to add landlord
    public void saveLandlord(Landlord l) {
        String sql = "insert into Landlord (landlordId, landlordPassword, userName, landlordPhone, landlordEmail) values (?,?,?,?,?)";
 
        try (
            // get connection to database
            Connection dbCon = DbConnection.getConnection(databaseURI);
            // create the statement
            PreparedStatement stmt = dbCon.prepareStatement(sql);) {
                // copy the data from the landlord domain object into the SQL parameters
                stmt.setInt(1, l.getLandlordId());
                stmt.setString(2, l.getLandlordPassword());
                stmt.setString(3, l.getUserName());
                stmt.setString(4, l.getLandlordPhone());
                stmt.setString(5, l.getLandlordEmail());

                stmt.executeUpdate(); // execute the statement
 
            } catch (SQLException ex) {  // we are forced to catch SQLException
                // don't let the SQLException leak from our DAO encapsulation
                throw new DAOException(ex.getMessage(), ex);
            }
    }
 
    // method to get landlord by username
    // support method only - used by validateCredentials() below
    public Landlord getLandlord(String landlordId) {
        String sql = "select * from Landlord where landlordId = ?";
        
        try (
            // get a connection to the database
            Connection dbCon = DbConnection.getConnection(databaseURI);
            // create the statement
            PreparedStatement stmt = dbCon.prepareStatement(sql);) {
                // copy the data from the customer domain object into the SQL parameters
                stmt.setString(1, landlordId);
                // execute the query
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()){
                    // get the data out of the query
                    int landlordId1 = rs.getInt("landlordId");
                    String landlordPassword  = rs.getString("landlordPassword");
                    String userName = rs.getString("userName");
                    String landlordPhone = rs.getString("landlordPhone");
                    String landlordEmail = rs.getString("landlordEmail");

                    // use the data to create a landlord object
                    Landlord l = new Landlord();
                    l.setLandlordId(landlordId1);
                    l.setLandlordPassword(landlordPassword);
                    l.setUserName(userName);
                    l.setLandlordPhone(landlordPhone);
                    l.setLandlordEmail(landlordEmail);
                   // landlordId, landlordPassword, userName, landlordPhone, landlordEmail, properties);

                    return l;
                }
                return null;

            } catch (SQLException ex) {  // we are forced to catch SQLException
                // don't let the SQLException leak from our DAO encapsulation
                throw new DAOException(ex.getMessage(), ex);
            }
    }

    // method to sign users in
    // accesses getLandlord() above
    public Boolean validateCredentials(String username, String password) {
        Landlord l = getLandlord(username);
        if ((l != null) && (l.getLandlordPassword().equals(password))) return true;
        else return false;
    }
}