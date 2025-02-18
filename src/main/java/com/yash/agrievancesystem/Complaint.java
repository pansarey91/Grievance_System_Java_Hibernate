package com.yash.agrievancesystem;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "complaints")
public class Complaint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@ManyToOne
	@JoinColumn(name = "student_id")
	private Student student;

	private String category;
	private String description;
	private String status;

	@Column(name = "submitted_at")
	private LocalDateTime submittedAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public Complaint() {
		this.submittedAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public Complaint(Student student, String category, String description) {
		this.student = student;
		this.category = category;
		this.description = description;
		this.status = "Pending"; // default status
		this.submittedAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public Complaint(Student student, String category, String description, String status) {
		this.student = student;
		this.category = category;
		this.description = description;
		this.status = status;
		this.submittedAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
		this.updatedAt = LocalDateTime.now();
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
		this.updatedAt = LocalDateTime.now();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		this.updatedAt = LocalDateTime.now();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.updatedAt = LocalDateTime.now();
	}

	public LocalDateTime getSubmittedAt() {
		return submittedAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public String toString() {
		return "Complaint{" + "id=" + id + ", student=" + student.getName() + ", category='" + category + '\''
				+ ", description='" + description + '\'' + ", status='" + status + '\'' + ", submittedAt=" + submittedAt
				+ ", updatedAt=" + updatedAt + '}';
	}
}
