package com.training.portal.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

	TRAINEE("trainee"),TRAINER("trainer");

	private final String role;
}
