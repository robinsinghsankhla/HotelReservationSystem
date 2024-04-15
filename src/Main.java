import java.sql.*;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String user = "root";
    private static final String password = "root";
    public static void main(String[] args) {
        try {//load the driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{//set connection
            Connection connection = DriverManager.getConnection(url,user,password);
            Scanner sc = new Scanner(System.in);
            while (true){
                System.out.println();
                System.out.println("RESERVATION SYSTEM");
                System.out.println("1. -> New Reservation");
                System.out.println("2. -> View Reservation");
                System.out.println("3. -> Get Room Number");
                System.out.println("4. -> Update Reservation");
                System.out.println("5. -> Delete Reservation");
                System.out.println("0. -> Exit");
                System.out.print("Enter the Choose-> ");
                int choice = sc.nextInt();
                switch (choice){
                    case 1:
                        newReservation(connection,sc);
                        break;
                    case 2:
                        viewReservation(connection);
                        break;
                    case 3:
                        getRoomNumber(connection,sc);
                        break;
                    case 4:
                        updateReservation(connection,sc);
                        break;
                    case 5:
                        deleteReservation(connection,sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        connection.close();
                        return;
                    default:
                        System.out.println("-------opps-------");
                        System.out.println("-------Wrong Entry-------");
                }
            }

        }catch (SQLException e){
            System.out.println();
            System.out.println(e.getMessage());
        }
        catch (InterruptedException e){
            throw new RuntimeException();
        }

    }
    //for the new reservation
    private static void newReservation(Connection connection,Scanner sc) throws InterruptedException{
        try {
            // get the information of the gust
            sc.nextLine();
            System.out.print("Enter the gust name -> ");
            String gustName = sc.nextLine().toUpperCase();
            System.out.print("Enter the room number -> ");
            int roomNumber = sc.nextInt();
            System.out.print("Enter the gust contact number -> ");
            String contactNumber = sc.next();
            // set the query
            String query = "INSERT INTO reservation(gust_name,room_no,contact_no) VALUES(?,?,?)";
            // insert data
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setString(1,gustName);
                preparedStatement.setInt(2,roomNumber);
                preparedStatement.setString(3,contactNumber);
                int affectedRow = preparedStatement.executeUpdate();
                int count = 5;
                if (affectedRow > 0) {
                    System.out.print("Reservation successfully");
                    while (count > 0) {
                        System.out.print(".");
                        Thread.sleep(450);
                        count--;
                    }
                    System.out.println();
                } else {
                    System.out.print("Reservation unsuccessfully");
                    while (count > 0) {
                        System.out.print(".");
                        Thread.sleep(450);
                        count--;
                    }
                    System.out.println();
                }
            }catch (SQLException e){
                System.out.println(e.getMessage());
            }
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
        }
    }
    // for View the reservation
    private static void viewReservation(Connection connection) throws SQLException{
        System.out.println();
        System.out.println("Current Reservation");
        System.out.println();
        String query = "SELECT * FROM reservation;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()){
                System.out.println("id-> "+resultSet.getInt("id")+" Name-> "+resultSet.getString("gust_name")+" RoomNo-> "+
                        resultSet.getInt("room_no")+" contactNo-> "+resultSet.getString("contact_no")+" date-> "+
                        resultSet.getTimestamp("reservation_date"));
            }
            System.out.println();
        }
    }
    // for find the room number
    private static void getRoomNumber(Connection connection,Scanner scanner){
        System.out.print("Enter the Reservation ID: ");
        int reservationID = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the gust Name: ");
        String gustName = scanner.nextLine().toUpperCase();
        String query = "SELECT room_no FROM reservation WHERE id = ? AND gust_name = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)){
            preparedStatement.setInt(1,reservationID);
            preparedStatement.setString(2,gustName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                int roomNo = resultSet.getInt("room_no");
                System.out.println("Room No. -> "+roomNo);
            }else {
                System.out.println("Reservation not find");
            }
            resultSet.close();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
    // update the reservation
    private static void updateReservation(Connection connection,Scanner scanner){
        try {
            System.out.print("Enter the reservation id: ");
            int reservationID = scanner.nextInt();
            scanner.nextLine();//Consume the newLine character
            //check data
            if (!reservationExists(connection, reservationID)) {
                System.out.println("Reservation not Exists");
                return;
            }
            // detail for the updation
            System.out.print("Enter the gust name -> ");
            String gustName = scanner.nextLine().toUpperCase();
            System.out.print("Enter the room number -> ");
            int roomNumber = scanner.nextInt();
            System.out.print("Enter the gust contact number -> ");
            String contactNumber = scanner.next();
            // set the query
            String query = "UPDATE reservation SET gust_name = ?,room_no = ?,contact_no=? WHERE id=?;";
            // insert data
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)){
                preparedStatement.setString(1,gustName);
                preparedStatement.setInt(2,roomNumber);
                preparedStatement.setString(3,contactNumber);
                preparedStatement.setInt(4,reservationID);
                int affectedRow = preparedStatement.executeUpdate();
                int count = 5;
                if (affectedRow > 0) {
                    System.out.print("Updation successfully");
                    while (count > 0) {
                        System.out.print(".");
                        Thread.sleep(450);
                        count--;
                    }
                    System.out.println();
                } else {
                    System.out.print("Updation unsuccessfully");
                    while (count > 0) {
                        System.out.print(".");
                        Thread.sleep(450);
                        count--;
                    }
                    System.out.println();
                }
            }catch (InterruptedException e){
                throw new RuntimeException();
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    // remove the reservation
    private static void deleteReservation(Connection connection,Scanner scanner){
        try {
            System.out.print("Enter the reservation id: ");
            int reservationID = scanner.nextInt();
            scanner.nextLine();//Consume the newLine character
            //check data
            if (!reservationExists(connection, reservationID)) {
                System.out.println("Reservation not Exists");
                return;
            }
            String query = "DELETE FROM reservation WHERE id = ?;";
            try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setInt(1,reservationID);
                int rowAffected = preparedStatement.executeUpdate();
                if (rowAffected>0){
                    System.out.println("Reservation Delete Successfully");
                }else {
                    System.out.println("Reservation Delete Unsuccessfully");
                }
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
    // exit function to exit the main menu
    private static void exit() throws InterruptedException{
        System.out.print("Exiting System");
        int i = 5;
        while (i!=0){
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println("\nThanks for using ther Rerservation Stystem!!");

    }
    //to check reservation exist or not
    private static boolean reservationExists(Connection connection , int reservationID) throws SQLException{
        String query = "SELECT id FROM reservation WHERE id = ?;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1,reservationID);
            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next())//if data exists
                    return true;
            }
        }
        return false;// if data not exists
    }

}