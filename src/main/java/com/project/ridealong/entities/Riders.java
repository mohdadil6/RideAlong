package com.project.ridealong.entities;

import jakarta.persistence.*;

@Entity
@Table(name="riders")
public class Riders {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="email")
	private String email;
	
	@Column(name="password")
	private String password;

public Riders() {
	
}

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public String getPassword() {
	return password;
}

public void setPassword(String password) {
	this.password = password;
}

@Override
public String toString() {
	return "Riders [id=" + id + ", email=" + email + ", password=" + password + "]";
}

public Riders(Long id, String email, String password) {
	super();
	this.id = id;
	this.email = email;
	this.password = password;
}



}