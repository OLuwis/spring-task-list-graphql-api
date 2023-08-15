package com.luwis.application.integration.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.HttpGraphQlTester;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureHttpGraphQlTester
public class TestMutationUserSignup {

    @Autowired 
    private HttpGraphQlTester tester;
    
    @Test
    void shouldReturnUserDetails() {
        
        String username = "Luis";
        String email = "oluismrs@gmail.com";
        String password = "12345678";
        
        tester.documentName("userSignup")
        .variable("username", username)
        .variable("email", email)
        .variable("password", password)
        .execute()
        .path("userSignup", path -> path
        .path("username").entity(String.class).isEqualTo(username)
        .path("email").entity(String.class).isEqualTo(email)
        .path("password").entity(String.class).isEqualTo(password));
        
    }
    
}
