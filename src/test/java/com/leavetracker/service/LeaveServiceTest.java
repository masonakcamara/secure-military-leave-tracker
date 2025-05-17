package com.leavetracker.service;

import com.leavetracker.model.LeaveRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LeaveServiceTest {

    private LeaveService svc;

    @BeforeEach
    void setUp() {
        svc = new LeaveService();
    }

    @Test
    void createRequestSetsPendingStatus() {
        LeaveRequest lr = svc.createRequest("alice",
                LocalDate.of(2025, 6, 1),
                LocalDate.of(2025, 6, 5),
                "VACATION",
                "Time off");
        assertNotNull(lr);
        assertEquals("PENDING", lr.getStatus());
        assertEquals("alice", lr.getUsername());
    }

    @Test
    void getRequestsForUserReturnsCorrectList() {
        svc.createRequest("bob", LocalDate.now(), LocalDate.now(), "EMERGENCY", "test");
        svc.createRequest("alice", LocalDate.now(), LocalDate.now(), "VACATION", "test2");
        List<LeaveRequest> bobList = svc.getRequestsForUser("bob");
        assertEquals(1, bobList.size());
        assertEquals("bob", bobList.get(0).getUsername());
    }

    @Test
    void approveAndDenyChangeStatus() {
        LeaveRequest lr = svc.createRequest("carol",
                LocalDate.now(), LocalDate.now(), "VACATION", "x");
        long id = lr.getId();
        assertTrue(svc.approveRequest(id));
        Optional<LeaveRequest> approved = svc.getRequestById(id);
        assertTrue(approved.isPresent());
        assertEquals("APPROVED", approved.get().getStatus());

        // Attempt to deny after approval should still return true but status moves on
        assertTrue(svc.denyRequest(id));
        Optional<LeaveRequest> denied = svc.getRequestById(id);
        assertTrue(denied.isPresent());
        assertEquals("DENIED", denied.get().getStatus());
    }

    @Test
    void cancelRequestWorks() {
        LeaveRequest lr = svc.createRequest("dave",
                LocalDate.now(), LocalDate.now(), "EMERGENCY", "y");
        long id = lr.getId();
        assertTrue(svc.cancelRequest(id));
        Optional<LeaveRequest> cancelled = svc.getRequestById(id);
        assertTrue(cancelled.isPresent());
        assertEquals("CANCELLED", cancelled.get().getStatus());
    }
}
