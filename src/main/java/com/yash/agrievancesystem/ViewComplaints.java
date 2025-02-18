package com.yash.agrievancesystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class ViewComplaints extends JFrame {

    private JTable table;
    private Student student;

    public ViewComplaints(Student student) {
        this.student = student;

        setTitle("My Complaints");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("My Complaints", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(70, 130, 180));
        add(titleLabel, BorderLayout.NORTH);

        // Table Model
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[] { "ID", "Category", "Description", "Status", "Submitted At","Updated At" });
        table = new JTable(model);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        table.getTableHeader().setBackground(new Color(135, 206, 235));
        loadComplaints(model);

        // Adding table to frame
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Footer with Close Button
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(245, 245, 245));
        JLabel footerLabel = new JLabel("End of Complaints List", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerPanel.add(footerLabel);

        JButton closeButton = new JButton("Close");
        closeButton.setBackground(new Color(220, 20, 60));
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        footerPanel.add(closeButton);

        add(footerPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadComplaints(DefaultTableModel model) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Complaint> query = session.createQuery("FROM Complaint WHERE student.id = :studentId", Complaint.class);
            query.setParameter("studentId", student.getId());
            List<Complaint> complaints = query.list();

            for (Complaint complaint : complaints) {
                model.addRow(new Object[] {
                        complaint.getId(),
                        complaint.getCategory(),
                        complaint.getDescription(),
                        complaint.getStatus(),
                        complaint.getSubmittedAt(),
                        complaint.getUpdatedAt()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading complaints!", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
