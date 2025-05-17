package com.leavetracker.service;

import com.leavetracker.model.LeaveRequest;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Manages leave requests: create, list, approve, deny, and cancel.
 */
public class LeaveService {
    private final Map<Long, LeaveRequest> requests = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Creates a new leave request with a unique ID and PENDING status.
     *
     * @param username  the user requesting leave
     * @param startDate the first day of leave
     * @param endDate   the last day of leave
     * @param type      the leave category (e.g., "VACATION", "EMERGENCY")
     * @param reason    explanation for the request
     * @return the newly created LeaveRequest
     */
    public LeaveRequest createRequest(String username, LocalDate startDate, LocalDate endDate,
                                      String type, String reason) {
        long id = idGenerator.getAndIncrement();
        LeaveRequest request = new LeaveRequest(id, username, startDate, endDate, type, reason);
        requests.put(id, request);
        return request;
    }

    /**
     * Retrieves all leave requests for the specified user.
     *
     * @param username the user whose requests to fetch
     * @return a list of LeaveRequest objects (empty if none found)
     */
    public List<LeaveRequest> getRequestsForUser(String username) {
        List<LeaveRequest> result = new ArrayList<>();
        for (LeaveRequest lr : requests.values()) {
            if (lr.getUsername().equals(username)) {
                result.add(lr);
            }
        }
        return result;
    }

    /**
     * Approves a pending leave request.
     *
     * @param requestId the ID of the request to approve
     * @return true if the request was found and approved; false otherwise
     */
    public boolean approveRequest(long requestId) {
        LeaveRequest lr = requests.get(requestId);
        if (lr != null && "PENDING".equals(lr.getStatus())) {
            lr.setStatus("APPROVED");
            return true;
        }
        return false;
    }

    /**
     * Denies a pending leave request.
     *
     * @param requestId the ID of the request to deny
     * @return true if the request was found and denied; false otherwise
     */
    public boolean denyRequest(long requestId) {
        LeaveRequest lr = requests.get(requestId);
        if (lr != null && "PENDING".equals(lr.getStatus())) {
            lr.setStatus("DENIED");
            return true;
        }
        return false;
    }

    /**
     * Cancels a pending leave request.
     *
     * @param requestId the ID of the request to cancel
     * @return true if the request was found and cancelled; false otherwise
     */
    public boolean cancelRequest(long requestId) {
        LeaveRequest lr = requests.get(requestId);
        if (lr != null && "PENDING".equals(lr.getStatus())) {
            lr.setStatus("CANCELLED");
            return true;
        }
        return false;
    }

    /**
     * Finds a leave request by its ID.
     *
     * @param requestId the ID to look up
     * @return Optional containing the LeaveRequest if found, or empty if not
     */
    public Optional<LeaveRequest> getRequestById(long requestId) {
        return Optional.ofNullable(requests.get(requestId));
    }
}