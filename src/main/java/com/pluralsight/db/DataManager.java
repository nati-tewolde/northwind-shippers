package com.pluralsight.db;

import com.pluralsight.model.Shipper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private DataSource dataSource;

    public DataManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Shipper> displayAllShippers() {
        List<Shipper> shippers = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM shippers");
             ResultSet results = preparedStatement.executeQuery()
        ) {

            while (results.next()) {
                int shipperId = results.getInt(1);
                String companyName = results.getString(2);
                String phone = results.getString(3);

                shippers.add(new Shipper (shipperId, companyName, phone));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return shippers;
    }

    public void insertNameAndPhoneWithKey(String name, String phone) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO shippers (CompanyName, Phone) values (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, phone);

            int rows = preparedStatement.executeUpdate();

            System.out.println("Rows Inserted: " + rows);

            try (ResultSet keys = preparedStatement.getGeneratedKeys()) {
                if (keys.next()) {
                    System.out.println("A new key was added: " + keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePhone (String newPhone, int shipperId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE shippers SET Phone = ? WHERE ShipperID = ?")) {
            preparedStatement.setString(1, newPhone);
            preparedStatement.setInt(2, shipperId);

            int rows = preparedStatement.executeUpdate();

            System.out.println("Rows Updated: " + rows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteShipper(String companyName) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM shippers WHERE CompanyName = ?")) {
            preparedStatement.setString(1, companyName);

            int rows = preparedStatement.executeUpdate();
            System.out.println("Rows Deleted: " + rows);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
