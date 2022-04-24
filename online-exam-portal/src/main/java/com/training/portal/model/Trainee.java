package com.training.portal.model;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.training.portal.utils.Role;
import org.springframework.stereotype.Component;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainee")
public class Trainee {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "Name can't be Empty")
	private String name;

	@NotBlank(message = "trainer name can't be Empty")
	private String trainerUserName;

	//@NotBlank(message = "technology is mandatory")
	private String technology;

	//@NotBlank(message = "Mention your location")
	private String location;

	//@NotBlank(message = "Contact can't be Empty")
	private String contact;

	private String domain;

	private int passOutYear;

	//@NotBlank(message = "mention your qualification")
	private String education;

	@Enumerated(EnumType.STRING)
	private Role role;


	//@NotBlank(message = "Mention your unique user name")
	private String userName;

	//@NotBlank(message = "password must not be Empty")
	private String password;

	private Boolean isAllowedForExam;
}
