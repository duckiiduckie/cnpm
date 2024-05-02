/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package DAO;

import java.util.*;
import java.sql.*;
import Model.Flight;
import Model.Schedule;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author duckii
 */
public class DAO {
    private static String connectionString = "jdbc:sqlserver://localhost:1433;databaseName=test;user=sa;password=Duckie@01;encrypt=false";

    private Connection _connect;

    public DAO() {

        _connect = initializeDBConnection();
    }

    private Connection initializeDBConnection() {
        try {
            String password = "";
            Connection connection = DriverManager.getConnection(connectionString);
            return connection;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public List<Flight> searchFlights(String departure, String arrival) throws SQLException {
        List<Flight> flights = new ArrayList<>();
        String query = "SELECT * FROM Flight WHERE departure = ? AND arrival = ?";
        try {
            PreparedStatement statement = _connect.prepareStatement(query);
            statement.setString(1, departure);
            statement.setString(2, arrival);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Flight flight = new Flight();
                flight.setId(result.getInt("id"));
                flight.setName(result.getString("name"));
                flight.setArrival(result.getString("arrival"));
                flight.setDeparture(result.getString("departure"));
                flight.setDurationMinutes(result.getInt("durationMinutes"));
                flights.add(flight);
            }
        } catch (SQLException ex) {
            throw ex;
        }
        return flights;
    }
    
    public List<Flight> searchAllFlights() throws SQLException {
        List<Flight> flights = new ArrayList<>();
        String query = "SELECT * FROM Flight";
        try {
            PreparedStatement statement = _connect.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Flight flight = new Flight();
                flight.setId(result.getInt("id"));
                flight.setName(result.getString("name"));
                flight.setArrival(result.getString("arrival"));
                flight.setDeparture(result.getString("departure"));
                flight.setDurationMinutes(result.getInt("durationMinutes"));
                flights.add(flight);
            }
        } catch (SQLException ex) {
            throw ex;
        }
        return flights;
    }
    
    public List<Schedule> searchSchedules(Timestamp departureTime, int flightId) throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT * FROM Schedule WHERE departureTime >= ? AND departureTime < ? AND flightId = ?";
        try {
            PreparedStatement statement = _connect.prepareStatement(query);
            // Đặt khoảng thời gian từ departureTime đến departureTime + 1 tiếng
            statement.setTimestamp(1, departureTime);
            statement.setTimestamp(2, new Timestamp(departureTime.getTime() + TimeUnit.HOURS.toMillis(1)));
            statement.setInt(3, flightId);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(result.getInt("id"));
                schedule.setAircraft(result.getString("aircraft"));
                schedule.setGate(result.getString("gate"));
                schedule.setStatus(result.getString("status"));
                schedule.setDepartureTime(result.getTimestamp("departureTime")); // Sử dụng cột chứa thời gian khởi hành
                schedule.setFlightId(result.getInt("flightId"));
                schedules.add(schedule);
            }
        } catch (SQLException ex) {
            throw ex;
        }
        return schedules;
    }

    
    public List<Schedule> searchScheduleByFlight(int flightId) throws SQLException {
    List<Schedule> schedules = new ArrayList<>();
    String query = "SELECT * FROM Schedule WHERE flightId = ?";
    try {
        PreparedStatement statement = _connect.prepareStatement(query);
        statement.setInt(1, flightId);
        ResultSet result = statement.executeQuery();
        while (result.next()) {
            Schedule schedule = new Schedule();
            schedule.setId(result.getInt("id"));
            schedule.setAircraft(result.getString("aircraft"));
            schedule.setGate(result.getString("gate"));
            schedule.setStatus(result.getString("status"));
            schedule.setDepartureTime(result.getTimestamp("departureTime")); // Thay đổi tên cột
            schedule.setFlightId(result.getInt("flightId"));
            schedules.add(schedule);
        }
    } catch (SQLException ex) {
        // Xử lý ngoại lệ hoặc ghi nhật ký ở đây
        ex.printStackTrace(); // Hoặc thay vì in ra, bạn có thể xử lý ngoại lệ một cách phù hợp với ứng dụng của bạn.
        throw ex; // Ném ra ngoại lệ để phía gọi sử lý.
    }
    return schedules;
}

    
    public List<Schedule> searchAllSchedules() throws SQLException {
        List<Schedule> schedules = new ArrayList<>();
        String query = "SELECT * FROM Schedule";
        try {
            
            PreparedStatement statement = _connect.prepareStatement(query);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Schedule schedule = new Schedule();
                schedule.setId(result.getInt("id"));
                schedule.setAircraft(result.getString("aircraft"));
                schedule.setGate(result.getString("gate"));
                schedule.setStatus(result.getString("status"));
                schedule.setDepartureTime(result.getTimestamp("departureTime"));
                schedule.setFlightId(result.getInt("flightId"));
                schedules.add(schedule);
            }
        } catch (SQLException ex) {
            throw ex;
        }
        return schedules;
    }
    
    public void addSchedule(Schedule schedule) throws SQLException {
        String insertQuery = "INSERT INTO Schedule (gate, departureTime, status, aircraft, flightId) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement insertStatement = _connect.prepareStatement(insertQuery);
            insertStatement.setString(1, schedule.getGate());
            insertStatement.setTimestamp(2, schedule.getDepartureTime());
            insertStatement.setString(3, schedule.getStatus());
            insertStatement.setString(4, schedule.getAircraft());
            insertStatement.setInt(5, schedule.getFlightId());
            insertStatement.executeUpdate();
        } catch (SQLException ex) {
            throw ex;
        }
    }
    
    public void updateSchedule(Schedule schedule) throws SQLException {
        String updateQuery = "UPDATE Schedule SET gate = ?, departureTime = ?, status = ?, aircraft = ?, flightId = ? WHERE id = ?";
        try {
            PreparedStatement updateStatement = _connect.prepareStatement(updateQuery);
            updateStatement.setString(1, schedule.getGate());
            updateStatement.setTimestamp(2, schedule.getDepartureTime());
            updateStatement.setString(3, schedule.getStatus());
            updateStatement.setString(4, schedule.getAircraft());
            updateStatement.setInt(5, schedule.getFlightId());
            updateStatement.setInt(6, schedule.getId());
            System.out.println("oke");
            updateStatement.executeUpdate();
        } catch (SQLException ex) {
            throw ex;
        }
    }
}
