package dao;

import domain.Landlord;
import domain.Property;
import domain.Services;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author jake
 */
public class ServicesJdbcDAO {

    private String databaseURI = DbConnection.getDefaultConnectionUri();

    // default construcot
    public ServicesJdbcDAO() {
    }

    // constructor that intialises the URI
    public ServicesJdbcDAO(String databaseURI) {
        this.databaseURI = databaseURI;
    }

    // method to add service
    public void saveService(Services s) {
        String sql = "insert into Services (serviceType, servicePassword, username, servicePhone, serviceEmail) values (?,?,?,?,?)";

        try (
                // get connection to database
                Connection dbCon = DbConnection.getConnection(databaseURI);
                // create the statement
                PreparedStatement stmt = dbCon.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
            // copy the data from the service domain object into the SQL parameters

            stmt.setString(1, s.getServiceType());
            stmt.setString(2, s.getServicePassword());
            stmt.setString(3, s.getUsername());
            stmt.setString(4, s.getServicePhone());
            stmt.setString(5, s.getServiceEmail());

            stmt.executeUpdate(); // execute the statement

            //getting generated keys and adding it to domain
            Integer Id = null;
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                Id = rs.getInt(1);
            } else {
                throw new DAOException("Problem getting generated services ID");
            }
            s.setServiceId(Id);

        } catch (SQLException ex) {  // we are forced to catch SQLException
            // don't let the SQLException leak from our DAO encapsulation
            throw new DAOException(ex.getMessage(), ex);
        }
    }

    // method to get service by username
    // support method only - used by validateCredentials() below
    public Services getServices(String username) {
        String sql = "select * from Services where username = ?";

        try (
                // get a connection to the database
                Connection dbCon = DbConnection.getConnection(databaseURI);
                // create the statement
                PreparedStatement stmt = dbCon.prepareStatement(sql);) {
            // copy the data from the customer domain object into the SQL parameters
            stmt.setString(1, username);
            // execute the query
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // get the data out of the query
                Integer serviceId1 = rs.getInt("serviceId");
                String serviceType = rs.getString("serviceType");
                String servicePassword = rs.getString("servicePassword");
                String servicePhone = rs.getString("servicePhone");
                String serviceEmail = rs.getString("serviceEmail");

                // use the data to create a service object
                Services s = new Services(serviceId1, serviceType, servicePassword, username, servicePhone, serviceEmail);

                return s;
            } else {
                return null;
            }
        } catch (SQLException ex) {  // we are forced to catch SQLException
            // don't let the SQLException leak from our DAO encapsulation
            throw new DAOException(ex.getMessage(), ex);
        }
    }

    //method to return services filtered by the type of service
    public Collection<Services> filterByType(String serviceType) {

        String sql = "select * from Services where serviceType = ? order by serviceId";

        try (
                // get a connection to the database
                Connection dbCon = DbConnection.getConnection(databaseURI);
                // create the statement
                PreparedStatement stmt = dbCon.prepareStatement(sql);) {
            // copy the data from the product domain object into the SQL parameters
            stmt.setString(1, serviceType);
            // execute the query
            ResultSet rs = stmt.executeQuery();

            // Using a List to preserve the order in which the data was returned from the query.
            List<Services> services = new ArrayList<>();

            // iterate through the query results
            while (rs.next()) {

                // get the data out of the query
                Integer serviceId = rs.getInt("serviceId");
                String serviceType1 = rs.getString("serviceType");
                String servicePassword = rs.getString("servicePassword");
                String username = rs.getString("username");
                String servicePhone = rs.getString("servicePhone");
                String serviceEmail = rs.getString("serviceEmail");

                // use the data to create a service object
                Services s = new Services();
                s.setServiceId(serviceId);
                s.setServiceType(serviceType1);
                s.setServicePassword(servicePassword);
                s.setUsername(username);
                s.setServicePhone(servicePhone);
                s.setServiceEmail(serviceEmail);

                // and put it in the collection
                services.add(s);
            }

            //return collection of properties that has been filtered by number of bedrooms
            return services;

        } catch (SQLException ex) {  // we are forced to catch SQLException
            // don't let the SQLException leak from our DAO encapsulation
            throw new DAOException(ex.getMessage(), ex);
        }
    }

    // method to sign users in
    // accesses getService() above
    public Boolean validateCredentials(String username, String password) {
        Services s = getServices(username);
        if ((s != null) && (s.getServicePassword().equals(password))) {
            return true;
        } else {
            return false;
        }
    }

    public void removeServices(Services s) {
        String sql = "delete Services where username = ?";

        try (
                // get connection to database
                Connection dbCon = DbConnection.getConnection(databaseURI);
                // create the statement
                PreparedStatement stmt = dbCon.prepareStatement(sql);) {
            // copy the data from the property domain object into the SQL parameters
            stmt.setString(1, s.getUsername());

            stmt.executeUpdate(); // execute the statement

        } catch (SQLException ex) {  // we are forced to catch SQLException
            // don't let the SQLException leak from our DAO encapsulation
            throw new DAOException(ex.getMessage(), ex);
        }
    }

    public Collection<Services> getAllServices() {

        String sql = "select * from Services";

        try (
                // get a connection to the database
                Connection dbCon = DbConnection.getConnection(databaseURI);
                // create the statement
                PreparedStatement stmt = dbCon.prepareStatement(sql);) {

            // execute the query
            ResultSet rs = stmt.executeQuery();

            // Using a List to preserve the order in which the data was returned from the query.
            List<Services> services = new ArrayList<>();

            // iterate through the query results
            while (rs.next()) {

                // get the data out of the query
                Integer serviceId = rs.getInt("serviceId");
                String serviceType1 = rs.getString("serviceType");
                String servicePassword = rs.getString("servicePassword");
                String username = rs.getString("username");
                String servicePhone = rs.getString("servicePhone");
                String serviceEmail = rs.getString("serviceEmail");

                // use the data to create a service object
                Services s = new Services();
                s.setServiceId(serviceId);
                s.setServiceType(serviceType1);
                s.setServicePassword(servicePassword);
                s.setUsername(username);
                s.setServicePhone(servicePhone);
                s.setServiceEmail(serviceEmail);

                // and put it in the collection
                services.add(s);
            }

            //return collection of properties that has been filtered by number of bedrooms
            return services;

        } catch (SQLException ex) {  // we are forced to catch SQLException
            // don't let the SQLException leak from our DAO encapsulation
            throw new DAOException(ex.getMessage(), ex);
        }
    }

}
