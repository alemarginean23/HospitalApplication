import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaApiServer {

    // Database connection details (Same as your DBConnection.java)
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // 1. Define the specific API Endpoints
        server.createContext("/api/doctors", new DoctorsHandler());
        server.createContext("/api/patient-status", new PatientStatusHandler());
        server.createContext("/api/appointments", new AppointmentHandler());
        server.createContext("/api/patients", new PatientsHandler());

        // 2. Global CORS Handler/Filter (This is mostly redundant now but necessary for general safety)
        server.createContext("/", (exchange) -> {
            Headers headers = exchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "Content-Type");

            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            // --- Routing Logic ---
            if (exchange.getRequestURI().getPath().startsWith("/api/")) {
                return;
            }

            String response = "404 Not Found";
            exchange.sendResponseHeaders(404, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });

        server.setExecutor(null);
        server.start();
        System.out.println("Pure Java API Server started on port " + port);
        System.out.println("Ready to handle API requests on port " + port);
    }

    // --- 1. Doctors Handler (CORS restored) ---
    static class DoctorsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            Headers headers = exchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET");
            headers.add("Content-Type", "application/json");

            String response;
            int statusCode = 200;

            if ("GET".equals(exchange.getRequestMethod())) {
                List<Map<String, Object>> doctors = retrieveDoctorsFromDB();
                response = convertListMapToJson(doctors);
            } else {
                statusCode = 405;
                response = "{\"error\": \"Method Not Allowed\"}";
                headers.add("Access-Control-Allow-Headers", "Content-Type");
            }

            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    // --- JDBC RETRIEVAL LOGIC for Doctors ---
    private static List<Map<String, Object>> retrieveDoctorsFromDB() {
        List<Map<String, Object>> doctors = new ArrayList<>();
        String SQL = "SELECT id, name, id_medic FROM public.medici ORDER BY name";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(SQL)) {

            while (rs.next()) {
                Map<String, Object> doctor = new HashMap<>();
                doctor.put("id", rs.getInt("id"));
                doctor.put("name", rs.getString("name"));
                doctor.put("idMedic", rs.getInt("id_medic"));
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            System.err.println("Database retrieval error: " + e.getMessage());
        }
        return doctors;
    }

    // --- MANUAL JSON CONVERTER ---
    private static String convertListMapToJson(List<Map<String, Object>> data) {
        StringBuilder json = new StringBuilder();
        json.append("[");

        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> map = data.get(i);
            json.append("{");
            int j = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                json.append("\"").append(entry.getKey()).append("\":");

                if (entry.getValue() instanceof String) {
                    json.append("\"").append(entry.getValue()).append("\"");
                } else {
                    json.append(entry.getValue());
                }

                if (j < map.size() - 1) {
                    json.append(",");
                }
                j++;
            }
            json.append("}");
            if (i < data.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    // --- 2. Patient Status Handler (CORS restored) ---
    static class PatientStatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Headers headers = exchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET");
            headers.add("Content-Type", "application/json");

            String response = "{\"error\": \"Method Not Allowed\"}";
            int statusCode = 405;

            if ("GET".equals(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery();
                String patientName = getQueryParam(query, "name");

                if (patientName != null && !patientName.trim().isEmpty()) {
                    List<Map<String, Object>> statusData = retrievePatientStatus(patientName);
                    response = convertListMapToJson(statusData);
                    statusCode = 200;
                } else {
                    response = "{\"error\": \"Missing patient name parameter.\"}";
                    statusCode = 400;
                }
            }

            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    // --- HELPER METHODS ---

    private static String getQueryParam(String query, String paramName) {
        if (query == null) return null;
        for (String param : query.split("&")) {
            if (param.startsWith(paramName + "=")) {
                try {
                    return java.net.URLDecoder.decode(param.substring(paramName.length() + 1), "UTF-8");
                } catch (java.io.UnsupportedEncodingException e) {
                    return param.substring(paramName.length() + 1);
                }
            }
        }
        return null;
    }

    private static List<Map<String, Object>> retrievePatientStatus(String patientName) {
        List<Map<String, Object>> statusList = new ArrayList<>();

        String SQL = "SELECT r.appointment_time, r.reason, m.name AS doctor_name " +
                "FROM public.pacienti p " +
                "JOIN public.programari r ON p.id = r.id_pacient_fk " +
                "JOIN public.medici m ON r.id_medic_fk = m.id " +
                "WHERE p.name ILIKE ?";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(SQL)) {

            ps.setString(1, "%" + patientName + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> appointment = new HashMap<>();
                    appointment.put("doctorName", rs.getString("doctor_name"));
                    appointment.put("reason", rs.getString("reason"));
                    appointment.put("time", rs.getTimestamp("appointment_time").toLocalDateTime().toString());
                    statusList.add(appointment);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database status retrieval error: " + e.getMessage());
        }
        return statusList;
    }

    // --- 3. Appointment Handler (CORS restored) ---
    static class AppointmentHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Headers headers = exchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET, POST");
            headers.add("Content-Type", "application/json");

            String response;
            int statusCode = 200;

            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                String requestBody = new String(exchange.getRequestBody().readAllBytes());

                Map<String, String> data = parseJsonToMap(requestBody);
                String patientName = data.get("name");
                String reason = data.get("reason");

                if (patientName == null || reason == null || patientName.trim().isEmpty()) {
                    statusCode = 400;
                    response = "{\"message\": \"Error: Patient name and reason are required.\"}";
                } else {
                    try {
                        long patientId = findOrCreatePatient(patientName);
                        int doctorId = 1;

                        createAppointment(patientId, doctorId, reason);

                        response = "{\"message\": \"Appointment scheduled successfully for " + patientName + ".\"}";
                    } catch (SQLException e) {
                        statusCode = 500;
                        response = "{\"message\": \"Database error during scheduling: " + e.getMessage() + "\"}";
                        System.err.println("Appointment creation error: " + e.getMessage());
                    }
                }
            } else {
                statusCode = 405;
                response = "{\"message\": \"Method Not Allowed\"}";
            }

            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

// --- APPOINTMENT DATABASE LOGIC ---

    private static long findOrCreatePatient(String name) throws SQLException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String findSQL = "SELECT id FROM public.pacienti WHERE name ILIKE ?";
            try (PreparedStatement ps = connection.prepareStatement(findSQL)) {
                ps.setString(1, name.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getLong("id");
                    }
                }
            }

            String insertSQL = "INSERT INTO public.pacienti (name) VALUES (?) RETURNING id";
            try (PreparedStatement ps = connection.prepareStatement(insertSQL)) {
                ps.setString(1, name.trim());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getLong("id");
                    }
                }
            }
        }
        throw new SQLException("Failed to find or create patient.");
    }

    private static void createAppointment(long patientId, int doctorId, String reason) throws SQLException {
        String insertSQL = "INSERT INTO public.programari (id_medic_fk, id_pacient_fk, appointment_time, reason) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = connection.prepareStatement(insertSQL)) {

            ps.setInt(1, doctorId);
            ps.setLong(2, patientId);
            ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now().plusDays(5)));
            ps.setString(4, reason);
            ps.executeUpdate();
        }
    }

    private static Map<String, String> parseJsonToMap(String jsonString) {
        Map<String, String> map = new HashMap<>();
        jsonString = jsonString.trim().substring(1, jsonString.length() - 1);
        String[] parts = jsonString.split(",");

        for (String part : parts) {
            String[] pair = part.trim().split(":", 2);
            if (pair.length == 2) {
                String key = pair[0].replaceAll("\"", "").trim();
                String value = pair[1].replaceAll("\"", "").trim();
                map.put(key, value);
            }
        }
        return map;
    }

    // --- 4. Patients List Handler (CORS restored) ---
    static class PatientsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Headers headers = exchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET");
            headers.add("Content-Type", "application/json");

            String response;
            int statusCode = 200;

            if ("GET".equals(exchange.getRequestMethod())) {
                List<Map<String, Object>> patients = retrieveAllPatients();
                response = convertListMapToJson(patients);
            } else {
                statusCode = 405;
                response = "{\"error\": \"Method Not Allowed\"}";
            }

            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }

    // --- JDBC RETRIEVAL LOGIC for Patients ---
    private static List<Map<String, Object>> retrieveAllPatients() {
        List<Map<String, Object>> patientList = new ArrayList<>();
        String SQL = "SELECT id, name, cnp, phone FROM public.pacienti ORDER BY name";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(SQL)) {

            while (rs.next()) {
                Map<String, Object> patient = new HashMap<>();
                patient.put("id", rs.getInt("id"));
                patient.put("name", rs.getString("name"));
                patient.put("cnp", rs.getString("cnp"));
                patient.put("phone", rs.getString("phone"));
                patientList.add(patient);
            }
        } catch (SQLException e) {
            System.err.println("Database patient retrieval error: " + e.getMessage());
        }
        return patientList;
    }
}