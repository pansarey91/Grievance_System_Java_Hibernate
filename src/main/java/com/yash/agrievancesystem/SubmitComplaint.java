package com.yash.agrievancesystem;

import javax.swing.*;
import java.awt.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SubmitComplaint extends JFrame {

    private JComboBox<String> categoryBox;
    private JTextArea descriptionArea;
    private JButton submitButton;
    private Student student;

    public SubmitComplaint(Student student) {
        this.student = student;

        setTitle("Submit Complaint");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Submit Your Complaint", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // Category Field
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        categoryBox = new JComboBox<>(new String[] { "Hostel", "Food", "Library" });
        categoryBox.setFont(new Font("Arial", Font.PLAIN, 14));
        add(categoryBox, gbc);

        // Description Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        descriptionArea = new JTextArea(5, 20);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        add(scrollPane, gbc);

        // Submit Button
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        submitButton = new JButton("Submit Complaint");
        submitButton.setBackground(new Color(60, 179, 113));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.addActionListener(e -> submitComplaint());
        add(submitButton, gbc);

        setVisible(true);
    }

    private void submitComplaint() {
        String category = (String) categoryBox.getSelectedItem();
        String description = descriptionArea.getText().trim();

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Description cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Complaint complaint = new Complaint(student, category, description);

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.save(complaint);
            transaction.commit();
            JOptionPane.showMessageDialog(this, "Complaint Submitted Successfully!");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error submitting complaint!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
