package com.leavetracker.service;

import com.leavetracker.model.LeaveRequest;
import com.leavetracker.repository.LeaveRequestRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Handles leave request operations against the database.
 */
public class LeaveService {
    private final LeaveRequestRepository repo = new LeaveRequestRepository();
    private final AtomicLong idGenerator = new AtomicLong(System.currentTimeMillis());

    /**
     * Submit a new leave request.
     *
     * @param username  who is requesting leave
     * @param startDate first day of leave
     * @param endDate   last day of leave
     * @param type      leave category (e.g. VACATION, EMERGENCY)
     * @param reason    explanation for the request
     * @return the saved LeaveRequest
     */
    public LeaveRequest createRequest(String username, LocalDate startDate, LocalDate endDate,
                                      String type, String reason) {
        long id = idGenerator.getAndIncrement();
        LeaveRequest request = new LeaveRequest(id, username, startDate, endDate, type, reason);
        repo.save(request);
        return request;
    }

    /**
     * Get all leave requests for a user.
     *
     * @param username whose requests to fetch
     * @return list of LeaveRequest (or empty if none)
     */
    public List<LeaveRequest> getRequestsForUser(String username) {
        return repo.findByUsername(username);
    }

    /**
     * Approve a pending leave request.
     *
     * @param requestId the request ID
     * @return true if status was updated
     */
    public boolean approveRequest(long requestId) {
        return repo.updateStatus(requestId, "APPROVED");
    }

    /**
     * Deny a pending leave request.
     *
     * @param requestId the request ID
     * @return true if status was updated
     */
    public boolean denyRequest(long requestId) {
        return repo.updateStatus(requestId, "DENIED");
    }

    /**
     * Cancel a pending leave request.
     *
     * @param requestId the request ID
     * @return true if status was updated
     */
    public boolean cancelRequest(long requestId) {
        return repo.updateStatus(requestId, "CANCELLED");
    }

    /**
     * Find a leave request by its ID.
     *
     * @param requestId the request ID
     * @return Optional containing the LeaveRequest if found
     */
    public Optional<LeaveRequest> getRequestById(long requestId) {
        return repo.findById(requestId);
    }
}