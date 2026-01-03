// Controller -> talks to service -> returns DTOs to client
package nl.carsforyou.garage.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import nl.carsforyou.garage.dtos.User.UserRequestDto;
import nl.carsforyou.garage.dtos.User.UserResponseDto;
import nl.carsforyou.garage.helpers.UrlHelper;
import nl.carsforyou.garage.services.UserService;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Users")
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UrlHelper urlHelper;

    public UserController(UserService userService, UrlHelper urlHelper) {
        this.userService = userService;
        this.urlHelper = urlHelper;
    }

    //these annotations are needed for Swagger
    @Operation(summary = "Get all users")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "List of users returned")})
    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }


    @Operation(summary = "Get a user by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User not found"),
            @ApiResponse(responseCode = "404", description = "User not found, check the {id}", content = @Content)
    })
    @GetMapping("/{id}")
    public UserResponseDto getUserById(@Parameter(description = "User id", example = "1") @PathVariable Long id) {
        return userService.getUserById(id);
    }


    @Operation(summary = "Create a user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created"),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content(schema = @Schema(implementation = org.springframework.http.ProblemDetail.class))
            )
    })
    @PostMapping
    public ResponseEntity<@NonNull UserResponseDto> createUser(@Valid @RequestBody UserRequestDto dto) {
        UserResponseDto created = userService.createUser(dto);

        //this will return 201 Created and a location header with the new Id
        return ResponseEntity
                .created(urlHelper.getCurrentUrlWithId(created.getUserId()))
                .body(created);
    }


    @Operation(summary = "Update a user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated"),
            @ApiResponse(responseCode = "404", description = "User not found, check the {id}", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<@NonNull UserResponseDto> updateCustomer(@Parameter(description = "User id", example = "1") @PathVariable Long id, @Valid @RequestBody UserRequestDto dto) {
        UserResponseDto updated = userService.updateUser(id, dto);

        return ResponseEntity.ok(updated);

    }


    @Operation(summary = "Delete a user")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found, check the {id}", content = @Content)}
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@Parameter(description = "User id", example = "1") @PathVariable Long id) {
        userService.deleteUser(id);
    }
}
