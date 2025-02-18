package com.yash.agrievancesystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.io.FileOutputStream;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class ReportDashboard extends JFrame {
	private JComboBox<String> reportDropdown;
	private JTable table;
	private DefaultTableModel model;
	private JSpinner startDateSpinner, endDateSpinner;
	private JButton printButton;

	public ReportDashboard() {
		setTitle("Complaint Reports");
		setSize(800, 550);
		setLayout(new BorderLayout());

		// Title
		JLabel titleLabel = new JLabel("Complaint Reports", SwingConstants.CENTER);
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		titleLabel.setForeground(new Color(0, 128, 128));
		add(titleLabel, BorderLayout.NORTH);

		// Report Selection Panel
		JPanel selectionPanel = new JPanel();
		String[] reportOptions = { "Total Complaints", "Complaints by Type", "Complaints by User",
				"Resolved Complaints", "Pending Complaints", "In Progress Complaints" };
		reportDropdown = new JComboBox<>(reportOptions);
		reportDropdown.setFont(new Font("Arial", Font.PLAIN, 14));

		// Date Range Selection
		selectionPanel.add(new JLabel("Start Date:"));
		startDateSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
		startDateSpinner.setEditor(startEditor);
		selectionPanel.add(startDateSpinner);

		selectionPanel.add(new JLabel("End Date:"));
		endDateSpinner = new JSpinner(new SpinnerDateModel());
		JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd");
		endDateSpinner.setEditor(endEditor);
		selectionPanel.add(endDateSpinner);

		selectionPanel.add(new JLabel("Select Report:"));
		selectionPanel.add(reportDropdown);
		add(selectionPanel, BorderLayout.NORTH);

		// Report Table
		model = new DefaultTableModel();
		table = new JTable(model);
		table.setFont(new Font("Arial", Font.PLAIN, 14));
		table.setRowHeight(25);
		table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
		table.getTableHeader().setBackground(new Color(135, 206, 235));
		add(new JScrollPane(table), BorderLayout.CENTER);

		// Button Panel
		JPanel buttonPanel = new JPanel();

		// Load Report Button
		JButton loadButton = new JButton("Load Report");
		loadButton.setBackground(new Color(60, 179, 113));
		loadButton.setForeground(Color.WHITE);
		loadButton.setFont(new Font("Arial", Font.BOLD, 14));
		loadButton.addActionListener(this::loadSelectedReport);
		buttonPanel.add(loadButton);

		// Print Report Button
		printButton = new JButton("Print Report");
		printButton.setBackground(new Color(70, 130, 180));
		printButton.setForeground(Color.WHITE);
		printButton.setFont(new Font("Arial", Font.BOLD, 14));
		printButton.addActionListener(this::printReport);
		buttonPanel.add(printButton);

		add(buttonPanel, BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void loadSelectedReport(ActionEvent event) {
		String selectedReport = (String) reportDropdown.getSelectedItem();
		model.setRowCount(0);

		// Convert Date from JSpinner to LocalDateTime
		Date startDate = (Date) startDateSpinner.getValue();
		Date endDate = (Date) endDateSpinner.getValue();
		LocalDateTime startDateTime = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		LocalDateTime endDateTime = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

		switch (selectedReport) {
		case "Total Complaints":
			model.setColumnIdentifiers(new String[] { "Description", "Count" });
			model.addRow(
					new Object[] { "Total Complaints", ReportService.getTotalComplaints(startDateTime, endDateTime) });
			break;

		case "Complaints by Type":
			model.setColumnIdentifiers(new String[] { "Category", "Count" });
			for (Object[] row : ReportService.getComplaintsByType(startDateTime, endDateTime)) {
				model.addRow(row);
			}
			break;

		case "Complaints by User":
			model.setColumnIdentifiers(new String[] { "User Name", "Complaint Count" });
			for (Object[] row : ReportService.getComplaintsByUser(startDateTime, endDateTime)) {
				model.addRow(row);
			}
			break;

		case "Resolved Complaints":
			model.setColumnIdentifiers(
					new String[] { "Student Name", "Category", "Description", "Status", "Submitted At", "Updated At" });
			for (Object[] row : ReportService.getResolvedComplaintDetails(startDateTime, endDateTime)) {
				model.addRow(row);
			}
			break;

		case "Pending Complaints":
			model.setColumnIdentifiers(
					new String[] { "Student Name", "Category", "Description", "Status", "Submitted At", "Updated At" });
			for (Object[] row : ReportService.getPendingComplaintDetails(startDateTime, endDateTime)) {
				model.addRow(row);
			}
			break;
		case "In Progress Complaints":
			model.setColumnIdentifiers(
					new String[] { "Student Name", "Category", "Description", "Status", "Submitted At", "Updated At" });
			for (Object[] row : ReportService.getInProgressComplaintDetails(startDateTime, endDateTime)) {
				model.addRow(row);
			}
			break;
		}
	}

	private void printReport(ActionEvent event) {
		if (model.getRowCount() == 0) {
			JOptionPane.showMessageDialog(this, "Please load a report first.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String selectedReport = (String) reportDropdown.getSelectedItem();
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream(selectedReport.replaceAll(" ", "_") + ".pdf"));
			document.open();

			document.add(
					new Paragraph("Report: " + selectedReport, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
			document.add(new Paragraph("\n"));

			int columnCount = model.getColumnCount();
			PdfPTable pdfTable = new PdfPTable(columnCount);

			// Add table headers
			for (int i = 0; i < columnCount; i++) {
				pdfTable.addCell(model.getColumnName(i));
			}

			// Add table rows
			for (int i = 0; i < model.getRowCount(); i++) {
				for (int j = 0; j < columnCount; j++) {
					pdfTable.addCell(model.getValueAt(i, j).toString());
				}
			}

			document.add(pdfTable);
			document.close();

			JOptionPane.showMessageDialog(this,
					"Report printed successfully as " + selectedReport.replaceAll(" ", "_") + ".pdf");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error printing report: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ReportDashboard();
	}
}
