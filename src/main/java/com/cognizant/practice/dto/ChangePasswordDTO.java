package com.cognizant.practice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public @Data @AllArgsConstructor @NoArgsConstructor class ChangePasswordDTO {
	
	private String oldPassword;
	private String newPassword;

}
