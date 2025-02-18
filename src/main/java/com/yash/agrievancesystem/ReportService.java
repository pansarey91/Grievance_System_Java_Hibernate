package com.yash.agrievancesystem;

import org.hibernate.Session;
import org.hibernate.query.Query;
import java.time.LocalDateTime;
import java.util.List;

public class ReportService {

	public static long getTotalComplaints(LocalDateTime startDate, LocalDateTime endDate) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<Long> query = session.createQuery(
					"SELECT COUNT(c) FROM Complaint c WHERE c.submittedAt BETWEEN :startDate AND :endDate", Long.class);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			return query.uniqueResult();
		}
	}

	public static List<Object[]> getComplaintsByType(LocalDateTime startDate, LocalDateTime endDate) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<Object[]> query = session.createQuery(
					"SELECT c.category, COUNT(c) FROM Complaint c WHERE c.submittedAt BETWEEN :startDate AND :endDate GROUP BY c.category",
					Object[].class);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			return query.list();
		}
	}

	public static List<Object[]> getComplaintsByUser(LocalDateTime startDate, LocalDateTime endDate) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<Object[]> query = session.createQuery(
					"SELECT c.student.name, COUNT(c) FROM Complaint c WHERE c.submittedAt BETWEEN :startDate AND :endDate GROUP BY c.student.name",
					Object[].class);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			return query.list();
		}
	}

	public static List<Object[]> getResolvedComplaintDetails(LocalDateTime startDate, LocalDateTime endDate) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<Object[]> query = session.createQuery(
					"SELECT c.student.name, c.category, c.description, c.status, c.submittedAt, c.updatedAt "
							+ "FROM Complaint c WHERE c.status = 'Resolved' AND c.submittedAt BETWEEN :startDate AND :endDate",
					Object[].class);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			return query.list();
		}
	}

	public static List<Object[]> getPendingComplaintDetails(LocalDateTime startDate, LocalDateTime endDate) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<Object[]> query = session.createQuery(
					"SELECT c.student.name, c.category, c.description, c.status, c.submittedAt, c.updatedAt "
							+ "FROM Complaint c WHERE c.status = 'Pending' AND c.submittedAt BETWEEN :startDate AND :endDate",
					Object[].class);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			return query.list();
		}
	}
	
	public static List<Object[]> getInProgressComplaintDetails(LocalDateTime startDate, LocalDateTime endDate) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			Query<Object[]> query = session.createQuery(
					"SELECT c.student.name, c.category, c.description, c.status, c.submittedAt, c.updatedAt "
							+ "FROM Complaint c WHERE c.status = 'In Progress' AND c.submittedAt BETWEEN :startDate AND :endDate",
					Object[].class);
			query.setParameter("startDate", startDate);
			query.setParameter("endDate", endDate);
			return query.list();
		}
	}
	
}
