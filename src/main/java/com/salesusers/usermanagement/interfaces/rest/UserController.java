package com.salesusers.usermanagement.interfaces.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping
    public ProblemDetail listUsers() {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_IMPLEMENTED,
            "User listing is not implemented yet."
        );
        problem.setTitle("Not implemented");
        return problem;
    }
}

