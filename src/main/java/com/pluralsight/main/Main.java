package com.pluralsight.main;

import com.pluralsight.db.DataManager;
import com.pluralsight.model.Shipper;
import org.apache.commons.dbcp2.BasicDataSource;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            if (args.length != 2) {
                System.out.println("This application needs a Username and Password too run!");
                System.exit(1);
            }

            String username = args[0];
            String password = args[1];

            try (BasicDataSource dataSource = new BasicDataSource()) {
                dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");
                dataSource.setUsername(username);
                dataSource.setPassword(password);

                DataManager dataManager = new DataManager(dataSource);

                System.out.print("\nPlease enter a shipper name to add a shipper: ");
                String name = scanner.nextLine();

                System.out.print("\nPlease enter a shipper phone: ");
                String phone = scanner.nextLine();

                dataManager.insertNameAndPhoneWithKey(name, phone);
                dataManager.displayAllShippers().forEach(System.out::println);

                System.out.println("\nUpdate a shipper with a new phone: ");
                String newPhone = scanner.nextLine();

                System.out.println("\nEnter the ID of the shipper you want to update: ");
                int shipperId = scanner.nextInt();
                scanner.nextLine();

                dataManager.updatePhone(newPhone, shipperId);
                dataManager.displayAllShippers().forEach(System.out::println);

                System.out.println("\nEnter the shipper name to delete: ");
                String shipperToDelete = scanner.nextLine();

                dataManager.deleteShipper(shipperToDelete);
                dataManager.displayAllShippers().forEach(System.out::println);

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
