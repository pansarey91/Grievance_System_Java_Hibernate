package com.yash.agrievancesystem;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {

    private Student student;

    public StudentDashboard(Student student) {
        this.student = student;

        setTitle("Student Dashboard");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Welcome Label
        JLabel welcomeLabel = new JLabel("Welcome, " + student.getName(), SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(welcomeLabel, gbc);

        // Submit Complaint Button
        gbc.gridy = 1;
        JButton submitComplaintButton = new JButton("Submit Complaint");
        submitComplaintButton.setBackground(new Color(60, 179, 113));
        submitComplaintButton.setForeground(Color.WHITE);
        submitComplaintButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitComplaintButton.addActionListener(e -> new SubmitComplaint(student));
        add(submitComplaintButton, gbc);

        // View Complaints Button
        gbc.gridy = 2;
        JButton viewComplaintsButton = new JButton("View My Complaints");
        viewComplaintsButton.setBackground(new Color(70, 130, 180));
        viewComplaintsButton.setForeground(Color.WHITE);
        viewComplaintsButton.setFont(new Font("Arial", Font.BOLD, 16));
        viewComplaintsButton.addActionListener(e -> new ViewComplaints(student));
        add(viewComplaintsButton, gbc);

        // Logout Button
        gbc.gridy = 3;
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(220, 20, 60));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginForm();
        });
        add(logoutButton, gbc);

        setVisible(true);
    }
}
