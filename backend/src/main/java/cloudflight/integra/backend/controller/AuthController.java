package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.model.User;
import cloudflight.integra.backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/testAuth")
public class AuthController {
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    @Autowired
    public AuthController(JwtService jwtService, AuthenticationManager authManager) {
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    @GetMapping(value = "/1")
    public ResponseEntity<HttpStatus> testAccessAny() {
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping(value = "/2")
    public ResponseEntity<HttpStatus> testAccessUser() {
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping(value = "/3")
    public ResponseEntity<HttpStatus> testAccessAdmin() {

        return ResponseEntity.ok()
                .build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<HttpStatus> login(@RequestBody User user) {
        try {
            Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user
                    .getEmail(), user.getPassword()));

            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION,
                            jwtService.generateToken(user.getEmail(),
                                                     authentication
                                                             .getAuthorities()))
                    .build();
        } catch (AuthenticationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .build();
        }
    }
}
