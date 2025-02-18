package com.yash.agrievancesystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class AdminDashboard extends JFrame {

	private JTable table;
	private DefaultTableModel model;
	private JButton approveButton, updateStatusButton, reportButton, logoutButton;

	public AdminDashboard() {
		setTitle("Admin Dashboard - Complaint Management");
		setSize(700, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Title
		JLabel titleLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(new Color(0, 128, 128));
		add(titleLabel, BorderLayout.NORTH);

		// Table Model
		model = new DefaultTableModel();
		model.setColumnIdentifiers(new String[] { "ID", "Student Name", "Email", "Approved" });
		table = new JTable(model);
		table.setFont(new Font("Arial", Font.PLAIN, 14));
		table.setRowHeight(25);
		table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
		table.getTableHeader().setBackground(new Color(135, 206, 250));
		loadStudentRegistrations();
		add(new JScrollPane(table), BorderLayout.CENTER);

		// Buttons Panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		approveButton = new JButton("Approve Student");
		approveButton.setBackground(new Color(60, 179, 113));
		approveButton.setForeground(Color.WHITE);
		approveButton.setFont(new Font("Arial", Font.BOLD, 14));
		approveButton.addActionListener(e -> approveStudent());
		buttonPanel.add(approveButton);

		updateStatusButton = new JButton("Update Complaint Status");
		updateStatusButton.setBackground(new Color(255, 165, 0));
		updateStatusButton.setForeground(Color.WHITE);
		updateStatusButton.setFont(new Font("Arial", Font.BOLD, 14));
		updateStatusButton.addActionListener(e -> new UpdateStatusFrame());
		buttonPanel.add(updateStatusButton);

		reportButton = new JButton("View Reports");
		reportButton.setBackground(new Color(70, 130, 180));
		reportButton.setForeground(Color.WHITE);
		reportButton.setFont(new Font("Arial", Font.BOLD, 14));
		reportButton.addActionListener(e -> new ReportDashboard());
		buttonPanel.add(reportButton);

		logoutButton = new JButton("Logout");
		logoutButton.setBackground(new Color(220, 20, 60));
		logoutButton.setForeground(Color.WHITE);
		logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new LoginForm();
			}
		});
		buttonPanel.add(logoutButton);

		add(buttonPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private void loadStudentRegistrations() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<Object[]> query = session.createQuery("SELECT s.id, s.name, s.email, s.approved FROM Student s",
					Object[].class);
			List<Object[]> students = query.list();

			for (Object[] student : students) {
				model.addRow(new Object[] { student[0], student[1], student[2], student[3] });
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error loading student registrations!", "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void approveStudent() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Select a student to approve!", "Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int studentId = (int) table.getValueAt(selectedRow, 0);
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Transaction transaction = session.beginTransaction();

			// Fetch student by ID
			Student student = session.get(Student.class, studentId);
			student.setApproved(true);
			session.update(student);
			transaction.commit();

			// Update UI table
			model.setValueAt(true, selectedRow, 3);
			JOptionPane.showMessageDialog(this, "Student approved successfully!");

			// Send Email Notification
			String studentEmail = student.getEmail();
			String subject = "Account Approved - Grievance System";
			String message = "Dear " + student.getName() + ",\n\n" + "Your account has been approved by the Admin.\n"
					+ "You can now log in and submit your complaints.\n\n" + "Regards,\nAdmin Team";

			EmailUtil.sendEmail(studentEmail, subject, message);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error approving student!", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new AdminDashboard();
	}
}
