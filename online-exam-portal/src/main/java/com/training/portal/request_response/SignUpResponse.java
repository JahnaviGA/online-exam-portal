package com.training.portal.request_response;

import com.training.portal.utils.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {

    private String email;

    private String name;

    private String message;

    private Role role;
}
