package com.leavetracker.repository;

import com.leavetracker.model.LeaveRequest;
import com.leavetracker.util.DatabaseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LeaveRequestRepositoryIntegrationTest {
    private LeaveRequestRepository repo;

    @BeforeEach
    void setUp() throws Exception {
        // wipe leave_requests table
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM leave_requests");
        }
        repo = new LeaveRequestRepository();
    }

    @Test
    void saveAndFindById() {
        LeaveRequest lr = new LeaveRequest(100, "dave",
                LocalDate.of(2025,1,1),
                LocalDate.of(2025,1,5),
                "VACATION", "holiday");
        assertTrue(repo.save(lr), "Insert should succeed");

        Optional<LeaveRequest> opt = repo.findById(100);
        assertTrue(opt.isPresent(), "Should find request by ID");
        assertEquals("dave", opt.get().getUsername());
    }

    @Test
    void findByUsernameReturnsCorrectList() {
        LeaveRequest a = new LeaveRequest(101, "ellen",
                LocalDate.now(), LocalDate.now(), "EMERGENCY", "x");
        LeaveRequest b = new LeaveRequest(102, "frank",
                LocalDate.now(), LocalDate.now(), "VACATION", "y");
        repo.save(a);
        repo.save(b);

        List<LeaveRequest> list = repo.findByUsername("ellen");
        assertEquals(1, list.size(), "Should return only Ellen’s request");
        assertEquals(101, list.get(0).getId());
    }

    @Test
    void updateStatusAndDelete() {
        LeaveRequest lr = new LeaveRequest(103, "gina",
                LocalDate.now(), LocalDate.now(), "VACATION", "z");
        repo.save(lr);

        assertTrue(repo.updateStatus(103, "APPROVED"), "Status update should succeed");
        Optional<LeaveRequest> updated = repo.findById(103);
        assertTrue(updated.isPresent());
        assertEquals("APPROVED", updated.get().getStatus());

        assertTrue(repo.delete(103), "Delete should succeed");
        assertTrue(repo.findById(103).isEmpty(), "Request should no longer exist");
    }
}