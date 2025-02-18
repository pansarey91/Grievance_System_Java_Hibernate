package com.yash.agrievancesystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class LoginForm extends JFrame {

	private JTextField emailField;
	private JPasswordField passwordField;
	private JButton loginButton, registerButton;
	private JRadioButton studentRadio, adminRadio;

	public LoginForm() {
		setTitle("Login");
		setSize(400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Title
		JLabel titleLabel = new JLabel("Welcome to Grievance System", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		add(titleLabel, gbc);

		// Email Field
		gbc.gridwidth = 1;
		gbc.gridy = 1;
		add(new JLabel("Email:"), gbc);
		gbc.gridx = 1;
		emailField = new JTextField();
		add(emailField, gbc);

		// Password Field
		gbc.gridx = 0;
		gbc.gridy = 2;
		add(new JLabel("Password:"), gbc);
		gbc.gridx = 1;
		passwordField = new JPasswordField();
		add(passwordField, gbc);

		// Role Selection
		gbc.gridx = 0;
		gbc.gridy = 3;
		studentRadio = new JRadioButton("Student");
		adminRadio = new JRadioButton("Admin");
		ButtonGroup roleGroup = new ButtonGroup();
		roleGroup.add(studentRadio);
		roleGroup.add(adminRadio);
		JPanel rolePanel = new JPanel();
		rolePanel.add(studentRadio);
		rolePanel.add(adminRadio);
		gbc.gridwidth = 2;
		add(rolePanel, gbc);

		// Login Button
		gbc.gridy = 4;
		loginButton = new JButton("Login");
		loginButton.setBackground(new Color(34, 139, 34));
		loginButton.setForeground(Color.WHITE);
		loginButton.addActionListener(e -> login());
		add(loginButton, gbc);

		// Registration Button
		gbc.gridy = 5;
		registerButton = new JButton("Student Registration");
		registerButton.setBackground(new Color(70, 130, 180));
		registerButton.setForeground(Color.WHITE);
		registerButton.addActionListener(e -> new StudentRegistration());
		add(registerButton, gbc);

		setVisible(true);
	}

	private void login() {
		String email = emailField.getText();
		String password = new String(passwordField.getPassword());

		if (email.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Email and Password are required!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (studentRadio.isSelected()) {
			Student student = authenticateStudent(email, password);
			if (student != null) {
				if (student.isApproved()) {
					JOptionPane.showMessageDialog(this, "Student Login Successful!");
					new StudentDashboard(student);
					dispose();
				} else {
					JOptionPane.showMessageDialog(this, "Your account is pending approval by the admin.",
							"Access Denied", JOptionPane.WARNING_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(this, "Invalid Student Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (adminRadio.isSelected()) {
			if (authenticateAdmin(email, password)) {
				JOptionPane.showMessageDialog(this, "Admin Login Successful!");
				new AdminDashboard();
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Invalid Admin Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Select a Role (Student/Admin)!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private Student authenticateStudent(String email, String password) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<Student> query = session.createQuery("FROM Student WHERE email = :email AND password = :password",
					Student.class);
			query.setParameter("email", email);
			query.setParameter("password", password);
			return query.uniqueResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private boolean authenticateAdmin(String email, String password) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<Admin> query = session.createQuery("FROM Admin WHERE email = :email AND password = :password",
					Admin.class);
			query.setParameter("email", email);
			query.setParameter("password", password);
			return query.uniqueResult() != null;
		}
	}

	public static void main(String[] args) {
		new LoginForm();
	}
}
