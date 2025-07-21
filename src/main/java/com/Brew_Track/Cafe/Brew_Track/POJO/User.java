package com.Brew_Track.Cafe.Brew_Track.POJO;

import java.io.Serializable;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NamedQuery;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@NamedQuery(name = "User.findEmailId", query = "select u from User u where u.email =:email")

@NamedQuery(name = "User.getAllUser", query = "select new com.Brew_Track.Cafe.Brew_Track.wrapper.UserWrapper(u.id, u.name, u.email, u.contactNumber, u.status) from User u")

@NamedQuery(name = "User.updateUser", query = "update User u set u.status =:status where u.id =:id")

@NamedQuery(name = "User.getAllAdmin", query = "select u.email from User u where u.role = 'admin'")

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "user")
public class User implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id; 
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "contactNumber")
	private String contactNumber;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "role")
	private String role;
	
	

}
