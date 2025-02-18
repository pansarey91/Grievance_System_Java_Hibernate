package com.yash.agrievancesystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class AdminLogin extends JFrame {
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;

	public AdminLogin() {
		setTitle("Admin Login");
		setSize(350, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new GridLayout(3, 2));

		JLabel usernameLabel = new JLabel("Username:");
		usernameField = new JTextField();
		JLabel passwordLabel = new JLabel("Password:");
		passwordField = new JPasswordField();
		loginButton = new JButton("Login");

		add(usernameLabel);
		add(usernameField);
		add(passwordLabel);
		add(passwordField);
		add(loginButton);

		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				authenticateAdmin();
			}
		});

		setVisible(true);
	}

	private void authenticateAdmin() {
		String username = usernameField.getText();
		String password = new String(passwordField.getPassword());

		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<Admin> query = session.createQuery("FROM Admin WHERE username = :username AND password = :password",
					Admin.class);
			query.setParameter("username", username);
			query.setParameter("password", password);
			Admin admin = query.uniqueResult();

			if (admin != null) {
				JOptionPane.showMessageDialog(this, "Login Successful!");
				new AdminDashboard();
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Invalid Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Login Failed!", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}
}
