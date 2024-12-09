import java.sql.*;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static final String URL = "jdbc:sqlite:C:\\Users\\YM\\DataGripProjects\\Databas\\SQLIteLabb3.db";

    private static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static void printMenu() {
        System.out.println("\nVälj:\n");
        System.out.println("0  - Stäng av\n" +
                "1  - Visa alla ägare\n" +
                "2  - Lägg till en ny ägare\n" +
                "3  - Uppdatera en ägare\n" +
                "4  - Ta bort en ägare\n" +
                "5  - Visa bilar för en viss ägare\n" +
                "8  - Visa menyn igen.");
    }

    private static void inputOwnerInsert() {
        System.out.println("Ange ägarens namn: ");
        String name = scanner.nextLine();
        System.out.println("Ange ägarens nummer: ");
        String number = scanner.nextLine();
        ownerInsert(name, number);
    }

    private static void inputOwnerUpdate() {
        System.out.println("Ange ID för ägaren som ska uppdateras: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Ange nytt namn: ");
        String name = scanner.nextLine();
        System.out.println("Ange nytt nummer: ");
        String number = scanner.nextLine();
        ownerUpdate(id, name, number);
    }

    private static void deleteOwner() {
        System.out.println("Ange ID för ägaren som ska tas bort: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        ownerDelete(id);
    }

    private static void listAllOwners() {
        String sql = "SELECT * FROM owner";
        try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                System.out.println(rs.getInt("ownerId") + "\t" + rs.getString("ownerName") + "\t" + rs.getString("ownerNumber"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void listCarsByOwner() {
        System.out.println("Ange ID för ägaren vars bilar du vill se: ");
        int ownerId = scanner.nextInt();
        scanner.nextLine();
        String sql = "SELECT carMake, carBrand, carLicensePlate FROM car WHERE ownerId = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ownerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString("carMake") + "\t" + rs.getString("carBrand") + "\t" + rs.getString("carLicensePlate"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void ownerInsert(String name, String number) {
        String sql = "INSERT INTO owner(ownerName, ownerNumber) VALUES(?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, number);
            pstmt.executeUpdate();
            System.out.println("Ägaren har lagts till.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void ownerUpdate(int id, String name, String number) {
        String sql = "UPDATE owner SET ownerName = ?, ownerNumber = ? WHERE ownerId = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, number);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            System.out.println("Ägaren har uppdaterats.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void ownerDelete(int id) {
        String sql = "DELETE FROM owner WHERE ownerId = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Ägaren har tagits bort.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        boolean quit = false;
        printMenu();
        while (!quit) {
            System.out.println("\nVälj (8 för att visa menyn):");
            int action = scanner.nextInt();
            scanner.nextLine();
            switch (action) {
                case 0:
                    System.out.println("\nStänger ner...");
                    quit = true;
                    break;
                case 1:
                    listAllOwners();
                    break;
                case 2:
                    inputOwnerInsert();
                    break;
                case 3:
                    inputOwnerUpdate();
                    break;
                case 4:
                    deleteOwner();
                    break;
                case 5:
                    listCarsByOwner();
                    break;
                case 8:
                    printMenu();
                    break;
                default:
                    System.out.println("Ogiltigt val. Försök igen.");
                    break;
            }
        }
    }
}


// fullständig CRUD på ägare,
// när man trycker på 8 uppdatera menyn så att den kommer upp igen.
// inga hårda värden,
// gör om ownerUpdate som inputOwnerInsert (inputOwnerUpdate)
// Delete, egen metod på case Delete
