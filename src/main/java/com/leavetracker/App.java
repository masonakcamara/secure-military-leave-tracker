package com.leavetracker;

import com.leavetracker.auth.AuthService;
import com.leavetracker.model.LeaveRequest;
import com.leavetracker.model.User;
import com.leavetracker.service.LeaveService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Entry point and CLI for Secure Military Leave Tracker.
 * <p>
 * Supports user sign-up / sign-in and managing leave requests.
 * Admin users can approve or deny any pending request.
 * </p>
 */
public class App {

    private final AuthService authService = new AuthService();
    private final LeaveService leaveService = new LeaveService();
    private User currentUser;
    private final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new App().run();
    }

    /**
     * Main application loop.
     */
    public void run() {
        System.out.println("Welcome to Secure Military Leave Tracker");
        while (true) {
            if (currentUser == null) {
                showAuthMenu();
            } else {
                showUserMenu();
            }
        }
    }

    private void showAuthMenu() {
        System.out.println("\n1) Register\n2) Login\n3) Exit");
        System.out.print("> ");
        switch (scanner.nextLine().trim()) {
            case "1" -> handleRegister();
            case "2" -> handleLogin();
            case "3" -> exit();
            default -> System.out.println("Invalid option");
        }
    }

    private void showUserMenu() {
        System.out.printf("\nHello, %s!%n", currentUser.getUsername());
        System.out.println("1) Request Leave");
        System.out.println("2) My Requests");
        if ("ADMIN".equals(currentUser.getRole())) {
            System.out.println("3) Review Pending");
        }
        System.out.println("0) Logout");
        System.out.print("> ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1" -> handleNewRequest();
            case "2" -> listUserRequests();
            case "3" -> {
                if ("ADMIN".equals(currentUser.getRole())) reviewPending();
                else System.out.println("Invalid option");
            }
            case "0" -> logout();
            default -> System.out.println("Invalid option");
        }
    }

    private void handleRegister() {
        System.out.print("Choose username: ");
        String user = scanner.nextLine().trim();
        System.out.print("Choose password: ");
        String pass = scanner.nextLine().trim();
        System.out.print("Role (USER/ADMIN): ");
        String role = scanner.nextLine().trim().toUpperCase();

        if (authService.register(user, pass, role)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            System.out.println("Username taken. Try again.");
        }
    }

    private void handleLogin() {
        System.out.print("Username: ");
        String user = scanner.nextLine().trim();
        System.out.print("Password: ");
        String pass = scanner.nextLine().trim();

        User u = authService.login(user, pass);
        if (u != null) {
            currentUser = u;
            System.out.println("Logged in successfully.");
        } else {
            System.out.println("Login failed.");
        }
    }

    private void handleNewRequest() {
        try {
            System.out.print("Start date (YYYY-MM-DD): ");
            LocalDate start = LocalDate.parse(scanner.nextLine().trim());
            System.out.print("End date (YYYY-MM-DD): ");
            LocalDate end = LocalDate.parse(scanner.nextLine().trim());
            System.out.print("Type (VACATION/EMERGENCY): ");
            String type = scanner.nextLine().trim().toUpperCase();
            System.out.print("Reason: ");
            String reason = scanner.nextLine().trim();

            LeaveRequest lr = leaveService.createRequest(
                    currentUser.getUsername(), start, end, type, reason
            );
            System.out.printf("Request #%d created.%n", lr.getId());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format, please use YYYY-MM-DD.");
        }
    }

    private void listUserRequests() {
        List<LeaveRequest> list = leaveService.getRequestsForUser(currentUser.getUsername());
        if (list.isEmpty()) {
            System.out.println("No requests found.");
        } else {
            list.forEach(System.out::println);
        }
    }

    private void reviewPending() {
        List<LeaveRequest> all = leaveService.getRequestsForUser(currentUser.getUsername());
        // In a real app, you'd fetch *all* pending, but for demo:
        all.stream()
                .filter(r -> "PENDING".equals(r.getStatus()))
                .forEach(System.out::println);

        System.out.print("Enter request ID to approve/deny (or blank to skip): ");
        String line = scanner.nextLine().trim();
        if (line.isEmpty()) return;
        try {
            long id = Long.parseLong(line);
            Optional<LeaveRequest> opt = leaveService.getRequestById(id);
            if (opt.isPresent()) {
                LeaveRequest r = opt.get();
                System.out.printf("1) Approve  2) Deny for request #%d%n> ", id);
                String choice = scanner.nextLine().trim();
                boolean ok = switch (choice) {
                    case "1" -> leaveService.approveRequest(id);
                    case "2" -> leaveService.denyRequest(id);
                    default -> false;
                };
                System.out.println(ok ? "Done." : "Could not update.");
            } else {
                System.out.println("Request not found.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
        }
    }

    private void logout() {
        currentUser = null;
        System.out.println("Logged out.");
    }

    private void exit() {
        System.out.println("Goodbye!");
        System.exit(0);
    }
}