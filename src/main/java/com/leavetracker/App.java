package com.leavetracker;

import com.leavetracker.auth.AuthService;
import com.leavetracker.service.LeaveService;
import com.leavetracker.ui.ConsoleUI;

public class App {
    public static void main(String[] args) {
        var auth = new AuthService();
        var leave = new LeaveService();
        new ConsoleUI(auth, leave).start();
    }
}