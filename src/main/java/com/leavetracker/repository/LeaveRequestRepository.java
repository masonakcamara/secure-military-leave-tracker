package com.leavetracker.repository;

import com.leavetracker.model.LeaveRequest;
import com.leavetracker.util.DatabaseUtil;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Persists and retrieves leave requests from the database.
 */
public class LeaveRequestRepository {

    private static final String INSERT_SQL =
            "INSERT INTO leave_requests(id, username, start_date, end_date, type, reason, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BY_USER_SQL =
            "SELECT * FROM leave_requests WHERE username = ?";
    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM leave_requests WHERE id = ?";
    private static final String UPDATE_STATUS_SQL =
            "UPDATE leave_requests SET status = ? WHERE id = ?";
    private static final String DELETE_SQL =
            "DELETE FROM leave_requests WHERE id = ?";

    /**
     * Save a new leave request.
     *
     * @param req the leave request to store
     * @return true if inserted; false otherwise
     */
    public boolean save(LeaveRequest req) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setLong(1, req.getId());
            ps.setString(2, req.getUsername());
            ps.setDate(3, Date.valueOf(req.getStartDate()));
            ps.setDate(4, Date.valueOf(req.getEndDate()));
            ps.setString(5, req.getType());
            ps.setString(6, req.getReason());
            ps.setString(7, req.getStatus());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting leave request", e);
        }
    }

    /**
     * List all leave requests for a given user.
     *
     * @param username the user to look up
     * @return list of matching requests (empty if none)
     */
    public List<LeaveRequest> findByUsername(String username) {
        List<LeaveRequest> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_USER_SQL)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching leave requests", e);
        }
        return list;
    }

    /**
     * Find a request by its ID.
     *
     * @param id the request ID
     * @return Optional containing the request if found
     */
    public Optional<LeaveRequest> findById(long id) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching leave request", e);
        }
        return Optional.empty();
    }

    /**
     * Update only the status of a leave request.
     *
     * @param id     the request ID
     * @param status new status value
     * @return true if updated; false otherwise
     */
    public boolean updateStatus(long id, String status) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS_SQL)) {

            ps.setString(1, status);
            ps.setLong(2, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating leave status", e);
        }
    }

    /**
     * Remove a leave request (e.g. when cancelled).
     *
     * @param id the request ID to delete
     * @return true if deleted; false otherwise
     */
    public boolean delete(long id) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_SQL)) {

            ps.setLong(1, id);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting leave request", e);
        }
    }

    private LeaveRequest mapRow(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String user = rs.getString("username");
        LocalDate start = rs.getDate("start_date").toLocalDate();
        LocalDate end = rs.getDate("end_date").toLocalDate();
        String type = rs.getString("type");
        String reason = rs.getString("reason");
        String status = rs.getString("status");
        LeaveRequest req = new LeaveRequest(id, user, start, end, type, reason);
        req.setStatus(status);
        return req;
    }

    public List<LeaveRequest> findAll() {
        List<LeaveRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM leave_requests";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all leave requests", e);
        }
        return list;
    }

}