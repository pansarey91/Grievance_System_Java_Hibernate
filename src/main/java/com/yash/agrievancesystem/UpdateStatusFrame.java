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

public class UpdateStatusFrame extends JFrame {

	private JTable table;
	private DefaultTableModel model;
	private JButton updateButton;

	public UpdateStatusFrame() {
		setTitle("Update Complaint Status");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		// Table Model
		model = new DefaultTableModel();
		model.setColumnIdentifiers(
				new String[] { "Complaint ID", "Student Name", "Category", "Description", "Status" });
		table = new JTable(model);
		table.setFont(new Font("Arial", Font.PLAIN, 14));
		table.setRowHeight(25);
		table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
		table.getTableHeader().setBackground(new Color(135, 206, 250));
		loadComplaints();

		add(new JScrollPane(table), BorderLayout.CENTER);

		// Update Button
		updateButton = new JButton("Update Status");
		updateButton.setBackground(new Color(255, 165, 0));
		updateButton.setForeground(Color.WHITE);
		updateButton.setFont(new Font("Arial", Font.BOLD, 14));
		updateButton.addActionListener(e -> updateComplaintStatus());

		add(updateButton, BorderLayout.SOUTH);

		setVisible(true);
	}

	private void loadComplaints() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<Object[]> query = session.createQuery(
					"SELECT c.id, c.student.name, c.category, c.description, c.status FROM Complaint c",
					Object[].class);
			List<Object[]> complaints = query.list();

			for (Object[] complaint : complaints) {
				model.addRow(complaint);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error loading complaints!", "Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private void updateComplaintStatus() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Select a complaint first!", "Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int complaintId = (int) table.getValueAt(selectedRow, 0);
		String[] statuses = { "Pending", "In Progress", "Resolved" };
		String newStatus = (String) JOptionPane.showInputDialog(this, "Select new status:", "Update Status",
				JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[0]);

		if (newStatus != null) {
			try (Session session = HibernateUtil.getSessionFactory().openSession()) {
				Transaction transaction = session.beginTransaction();
				Complaint complaint = session.get(Complaint.class, complaintId);
				complaint.setStatus(newStatus);
				session.update(complaint);
				transaction.commit();

				model.setValueAt(newStatus, selectedRow, 4);
				JOptionPane.showMessageDialog(this, "Complaint Status Updated!");

				// Send Email Notification
				String studentEmail = complaint.getStudent().getEmail();
				String subject = "Complaint Status Update";
				String message = "Dear Student,\n\nYour complaint (ID: " + complaintId + ") has been updated to: "
						+ newStatus + "\n\nRegards,\nAdmin Team";

				EmailUtil.sendEmail(studentEmail, subject, message);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "Error updating status!", "Error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new UpdateStatusFrame();
	}
}
