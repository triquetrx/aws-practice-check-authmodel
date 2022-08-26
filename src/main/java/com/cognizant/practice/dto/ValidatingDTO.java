package com.cognizant.practice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public @NoArgsConstructor @Data @AllArgsConstructor class ValidatingDTO {

	private boolean tokenValid;
	private String role;
}
