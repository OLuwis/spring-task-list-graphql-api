package com.luwis.application.unit;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import com.luwis.application.controllers.AuthController;
import com.luwis.application.graphql.inputs.LoginInput;
import com.luwis.application.graphql.responses.LoginRes;
import com.luwis.application.graphql.types.User;
import com.luwis.application.services.AuthService;
import com.luwis.application.unit.mocks.JwtMock;
import com.luwis.application.utils.InputValidator;

@Import(JwtMock.class)
@GraphQlTest(AuthController.class)
@TestMethodOrder(OrderAnnotation.class)
public class Login {
    
    @Autowired
    private GraphQlTester tester;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private JwtDecoder jwtDecoder;

    @MockBean
    private AuthService authService;

    @MockBean
    private InputValidator inputValidator;

    private final JwtMock jwtMock = new JwtMock();

    @Test
    @Order(1)
    void shouldReturnUserAndToken() {
        String name = "MyName";
        String email = "myemail@gmail.com";
        String password = "12345Ab!";

        var input = new LoginInput(email, password);
        var user = new User((long) 1, name, email);
        var token = jwtEncoder.encode(jwtMock.getParameters()).getTokenValue();
        var response = new LoginRes(user, token);

        Mockito.doReturn(response).when(authService).login(input);

        tester.documentName("LoginUser")
            .variable("name", name)
            .variable("email", email)
            .variable("password", password)
            .execute()
            .path("$['data']['LoginUser']", path -> {
                path.path("['user']").entity(User.class).get().equals(user);
                jwtDecoder.decode(
                    path.path("['token']").entity(String.class).get()
                ).getSubject().equals("1");
            });
    }

    @Test
    @Order(2)
    void shouldThrowInvalidEmail() {
        String email = "invalid@email";
        String password = "12345Ab!";
        
        var input = new LoginInput(email, password);
        var exception = new BadCredentialsException("Error: Invalid Email");

        Mockito.doThrow(exception).when(inputValidator).validate(input);
        
        tester.documentName("LoginUser")
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Invalid Email"))
            .expect(error -> error.getErrorType().equals(ErrorType.BAD_REQUEST))
            .expect(error -> error.getPath().equals("LoginUser"));
    }

    @Test
    @Order(3)
    void shouldThrowInvalidPassword() {
        String email = "myemail@gmail.com";
        String password = "Invalid";
        
        var input = new LoginInput(email, password);
        var exception = new BadCredentialsException("Error: Invalid Password");
        
        Mockito.doThrow(exception).when(inputValidator).validate(input);
        
        tester.documentName("LoginUser")
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: Invalid Password"))
            .expect(error -> error.getErrorType().equals(ErrorType.BAD_REQUEST))
            .expect(error -> error.getPath().equals("LoginUser"));
    }

    @Test
    @Order(4)
    void shouldThrowUserIsNotRegistered() {
        String email = "unknown@gmail.com";
        String password = "12345Ab!";
        
        var input = new LoginInput(email, password);
        var exception = new UsernameNotFoundException("Error: User Is Not Registered");
        
        Mockito.doThrow(exception).when(inputValidator).validate(input);
        
        tester.documentName("LoginUser")
            .variable("email", email)
            .variable("password", password)
            .execute()
            .errors()
            .expect(error -> error.getMessage().equals("Error: User Is Not Registered"))
            .expect(error -> error.getErrorType().equals(ErrorType.NOT_FOUND))
            .expect(error -> error.getPath().equals("LoginUser"));
    }
    
}