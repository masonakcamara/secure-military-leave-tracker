package com.leavetracker.model;

import java.time.LocalDate;

/**
 * Represents a leave request submitted by a user,
 * tracking its details and approval status.
 */
public class LeaveRequest {
    private final long id;
    private final String username;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String type;    // e.g. "VACATION", "EMERGENCY"
    private final String reason;
    private String status;        // "PENDING", "APPROVED", "DENIED"

    /**
     * Create a new leave request. Defaults status to PENDING.
     *
     * @param id        unique identifier
     * @param username  who is taking leave
     * @param startDate first day of leave
     * @param endDate   last day of leave
     * @param type      category of leave
     * @param reason    explanation for the request
     */
    public LeaveRequest(long id, String username, LocalDate startDate, LocalDate endDate,
                        String type, String reason) {
        this.id = id;
        this.username = username;
        this.startDate = startDate;
        this.endDate = endDate;
        this.type = type;
        this.reason = reason;
        this.status = "PENDING";
    }

    /** @return the unique request ID */
    public long getId() {
        return id;
    }

    /** @return who requested the leave */
    public String getUsername() {
        return username;
    }

    /** @return the first day of the leave period */
    public LocalDate getStartDate() {
        return startDate;
    }

    /** @return the last day of the leave period */
    public LocalDate getEndDate() {
        return endDate;
    }

    /** @return the leave category */
    public String getType() {
        return type;
    }

    /** @return the user’s reason for the leave */
    public String getReason() {
        return reason;
    }

    /** @return current approval status */
    public String getStatus() {
        return status;
    }

    /**
     * Update the approval status of this request.
     *
     * @param status new status, e.g. "APPROVED" or "DENIED"
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "LeaveRequest{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", type='" + type + '\'' +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}