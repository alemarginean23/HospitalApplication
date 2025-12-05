import java.sql.*;
import java.time.LocalDateTime; // Required for the Appointment time

public class DBConnection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {

            System.out.println("Connected successfully: " + connection);
            String tableNameMedici = "public.medici";
            String tableNamePacienti = "public.pacienti";
            String tableNameProgramari = "public.programari";

            // --- 1. CLEANUP AND SCHEMA CREATION (KEEP THIS AS IS) ---
            statement.execute("DROP TABLE IF EXISTS " + tableNameProgramari);
            statement.execute("DROP TABLE IF EXISTS " + tableNamePacienti);
            statement.execute("TRUNCATE TABLE " + tableNameMedici + " RESTART IDENTITY CASCADE");

            // Create Medici Table
            statement.execute("CREATE TABLE IF NOT EXISTS " + tableNameMedici + " (id SERIAL PRIMARY KEY, name VARCHAR(255), id_medic INT UNIQUE)");
            // Create Pacienti Table
            statement.execute("CREATE TABLE IF NOT EXISTS " + tableNamePacienti + " (id SERIAL PRIMARY KEY, name VARCHAR(255), cnp VARCHAR(13) UNIQUE, phone VARCHAR(15))");
            // Create Programari Table (uses PRIMARY KEY REFERENCES)
            String createProgramariSQL = "CREATE TABLE " + tableNameProgramari + " (" +
                    "id SERIAL PRIMARY KEY, " +
                    "id_medic_fk INT REFERENCES public.medici(id), " +
                    "id_pacient_fk INT REFERENCES public.pacienti(id), " +
                    "appointment_time TIMESTAMP NOT NULL, " +
                    "reason VARCHAR(255)" +
                    ")";
            statement.execute(createProgramariSQL);
            System.out.println("All schema tables created successfully.");

            // --- 2. INSERT DOCTORS (As before) ---
            String insertMediciQuery = "INSERT INTO " + tableNameMedici + " (name, id_medic) VALUES(?, ?)";
            PreparedStatement preparedStmtMedici = connection.prepareStatement(insertMediciQuery);

            Object[][] doctors = {
                    {"Elvira Drăgan", 1097}, {"Robert Negoiță", 9189}, {"Sofia Stăruială", 7862},
                    {"Ionuț Negoiu", 7052}, {"Corina Lămboiu", 5600}
            };

            for (Object[] doctor : doctors) {
                preparedStmtMedici.setString(1, (String) doctor[0]);
                preparedStmtMedici.setInt(2, (int) doctor[1]);
                preparedStmtMedici.executeUpdate();
            }
            System.out.println("Finished inserting " + doctors.length + " doctors.");

            // ---------------------------------------------------------
            // 3. INSERT PATIENTS (New Block)
            // ---------------------------------------------------------
            // Note: We use the existing ID (CNP) from the Demo as the unique identifier.
            String insertPacientiQuery = "INSERT INTO " + tableNamePacienti + " (name, cnp, phone) VALUES(?, ?, ?)";
            PreparedStatement preparedStmtPacienti = connection.prepareStatement(insertPacientiQuery);

            Object[][] patients = {
                    {"Marin Sorescu", "1234", "555-0001"},
                    {"Adrian Dobrescu", "2345", "555-0002"},
                    {"Silvia Eremițiu", "1245", "555-0003"},
                    {"Nadia Birău", "1546", "555-0004"},
                    {"Sabina Chirilă", "7194", "555-0005"},
                    {"Robert Dumitrescu", "3290", "555-0006"},
                    {"Valentin Dragos", "9413", "555-0007"},
                    {"Mădălina Găbureanu", "5251", "555-0008"},
                    {"Emanuel Mihăilescu", "4078", "555-0009"},
                    {"Sergiu Dobre", "6452", "555-0010"}
            };

            for (Object[] patient : patients) {
                preparedStmtPacienti.setString(1, (String) patient[0]);
                preparedStmtPacienti.setString(2, (String) patient[1]); // Using the number from your Demo as CNP
                preparedStmtPacienti.setString(3, (String) patient[2]); // Placeholder phone
                preparedStmtPacienti.executeUpdate();
            }
            System.out.println("Finished inserting " + patients.length + " patients.");

            // ---------------------------------------------------------
            // 4. INSERT APPOINTMENTS (PROGRAMARI)
            // ---------------------------------------------------------
            // We need to link a patient (by their DB 'id') to a doctor (by their DB 'id').
            // Doctor 'Elvira Drăgan' has DB id=1, Patient 'Marin Sorescu' has DB id=1.

            // Since the IDs are SERIAL, the first patient inserted gets ID 1, second gets ID 2, etc.

            String insertProgramariQuery = "INSERT INTO " + tableNameProgramari +
                    " (id_medic_fk, id_pacient_fk, appointment_time, reason) VALUES(?, ?, ?, ?)";
            PreparedStatement preparedStmtProgramari = connection.prepareStatement(insertProgramariQuery);

            // Create some test appointments (using the auto-generated DB IDs):
            // Patient 1 (Marin Sorescu) sees Doctor 1 (Elvira Drăgan) for 'headache'
            preparedStmtProgramari.setInt(1, 1); // Elvira Drăgan (DB ID 1)
            preparedStmtProgramari.setInt(2, 1); // Marin Sorescu (DB ID 1)
            preparedStmtProgramari.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().plusDays(1)));
            preparedStmtProgramari.setString(4, "headache and appendicitis diagnosis");
            preparedStmtProgramari.executeUpdate();

            // Patient 2 (Adrian Dobrescu) sees Doctor 3 (Sofia Stăruială) for 'tonsillitis'
            preparedStmtProgramari.setInt(1, 3); // Sofia Stăruială (DB ID 3)
            preparedStmtProgramari.setInt(2, 2); // Adrian Dobrescu (DB ID 2)
            preparedStmtProgramari.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().plusDays(2)));
            preparedStmtProgramari.setString(4, "myasthenia gravis treatment");
            preparedStmtProgramari.executeUpdate();

            System.out.println("Finished inserting 2 test appointments.");


        } catch (SQLException e) {
            System.out.println("Database Error:");
            e.printStackTrace();
        }
    }
}