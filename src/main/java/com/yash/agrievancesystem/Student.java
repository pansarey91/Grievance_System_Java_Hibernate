package com.yash.agrievancesystem;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "students")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	private String email;
	private String password;
	private boolean approved;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public Student() {
		this.updatedAt = LocalDateTime.now();
	}

	public Student(String name, String email, String password, boolean approved) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.approved = approved;
		this.updatedAt = LocalDateTime.now();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.updatedAt = LocalDateTime.now();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
		this.updatedAt = LocalDateTime.now();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		this.updatedAt = LocalDateTime.now();
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
		this.updatedAt = LocalDateTime.now();
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public String toString() {
		return "Student{" + "id=" + id + ", name='" + name + '\'' + ", email='" + email + '\'' + ", approved="
				+ approved + ", updatedAt=" + updatedAt + '}';
	}
}
