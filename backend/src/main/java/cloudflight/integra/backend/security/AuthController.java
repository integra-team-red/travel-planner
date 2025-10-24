package cloudflight.integra.backend.security;

import cloudflight.integra.backend.user.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class AuthController {
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final UserService userService;

    @Autowired
    public AuthController(JwtService jwtService, AuthenticationManager authManager, UserService userService) {
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.userService = userService;
    }

    @GetMapping(value = "/1")
    public ResponseEntity<HttpStatus> testAccessAny() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/2")
    public ResponseEntity<HttpStatus> testAccessUser() {
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/3")
    public ResponseEntity<HttpStatus> testAccessAdmin() {

        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Register into the application by providing an email / password combination",
            responses = {
                @ApiResponse(responseCode = "201", description = "Successfully created"),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid email / password combination",
                        content = @Content)
            })
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        userService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Log into the application by providing an email / password combination",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successful login",
                        headers = {@Header(name = HttpHeaders.AUTHORIZATION, description = "Returns a JWT token")}),
                @ApiResponse(
                        responseCode = "401",
                        description = "Invalid email / password combination",
                        content = @Content)
            })
    @PostMapping(value = "/login")
    public ResponseEntity<HttpStatus> login(@RequestBody User user) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtService.generateToken(user.getEmail(), authentication.getAuthorities()))
                    .build();
        } catch (AuthenticationException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(
            summary = "get the authenticated user",
            description = "returns info about the user based on the jwt token",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "user's info successfully returned",
                        content =
                                @Content(
                                        mediaType = "application/json",
                                        schema = @Schema(implementation = UserDTO.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "invalid or missing token",
                        content = @Content(schema = @Schema(example = "invalid token")))
            })
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDTO user = new UserDTO(
                authentication.getName(),
                authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()));
        return ResponseEntity.ok(user);
    }
}
