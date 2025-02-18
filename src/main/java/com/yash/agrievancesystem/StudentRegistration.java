package com.yash.agrievancesystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class StudentRegistration extends JFrame {

	private JTextField nameField, emailField;
	private JPasswordField passwordField;
	private JButton registerButton;

	public StudentRegistration() {
		setTitle("Student Registration");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Title
		JLabel titleLabel = new JLabel("Register as a Student", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		add(titleLabel, gbc);

		// Name Field
		gbc.gridwidth = 1;
		gbc.gridy = 1;
		add(new JLabel("Name:"), gbc);
		gbc.gridx = 1;
		nameField = new JTextField();
		add(nameField, gbc);

		// Email Field
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(new JLabel("Email:"), gbc);
		gbc.gridx = 1;
		emailField = new JTextField();
		add(emailField, gbc);

		// Password Field
		gbc.gridx = 0;
		gbc.gridy = 3;
		add(new JLabel("Password:"), gbc);
		gbc.gridx = 1;
		passwordField = new JPasswordField();
		add(passwordField, gbc);

		// Register Button
		gbc.gridy = 4;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		registerButton = new JButton("Register");
		registerButton.setBackground(new Color(70, 130, 180));
		registerButton.setForeground(Color.WHITE);
		registerButton.setFont(new Font("Arial", Font.BOLD, 16));
		registerButton.addActionListener(e -> registerStudent());
		add(registerButton, gbc);

		setVisible(true);
	}

	private void registerStudent() {
		String name = nameField.getText();
		String email = emailField.getText();
		String password = new String(passwordField.getPassword());

		if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();
			Student student = new Student(name, email, password, false);
			session.save(student);
			transaction.commit();
			JOptionPane.showMessageDialog(this, "Registration successful! Please wait for admin approval.");
			dispose();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error registering student!", "Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new StudentRegistration();
	}
}
