package cat.institutmarianao.sailing.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cat.institutmarianao.sailing.ws.model.Admin;
import cat.institutmarianao.sailing.ws.model.Client;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.User.Role;
import cat.institutmarianao.sailing.ws.service.UserService;
import cat.institutmarianao.sailing.ws.validation.groups.OnUserUpdate;
import cat.institutmarianao.sailing.ws.validation.groups.OnUserCreate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.websocket.server.PathParam;

@Tag(name = "User", description = "User API")

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Operation(summary = "Check if user exists", description = "Check if a user exists in the database. The response is true if exists, false otherwise")
	@ApiResponse(responseCode = "200", content = { @Content() }, description = "Check goes ok")
	@ApiResponse(responseCode = "500", content = {
			@Content() }, description = "Error checking the user. See response body for more details")
	@GetMapping(value = "/check/{username}")
	public @ResponseBody boolean check(@PathParam("username") @NotBlank String username) throws Exception {
		return userService.existsById(username);
	}

	/* Swagger */
	@Operation(summary = "Get user by id")
	@ApiResponse(responseCode = "200", description = "User retrieved ok")
	@ApiResponse(responseCode = "404", description = "Resource not found")
	/**/
	@GetMapping("/get/by/username/{username}")
	public User findByUsername(@PathVariable("username") @NotBlank String username) {
		return userService.getByUsername(username);
	}

	@Operation(summary = "Retrieve all users", description = "Retrieve all users from the database.")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class))) }, description = "Users retrieved ok")
	@GetMapping(value = "/find/all")
	public @ResponseBody List<User> findAll(@RequestParam(value = "roles", required = false) Role[] roles,
			@RequestParam(value = "fullName", required = false) String fullName) {

		return userService.findAll(roles, fullName);
	}

	@Operation(summary = "Save a user", description = "Saves a user in the database. The response is the stored user from the database.")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }, description = "User saved ok")
	@ApiResponse(responseCode = "500", content = {
			@Content() }, description = "Error saving the user. See response body for more details")
	@PostMapping(value = "/save")
	@Validated(OnUserCreate.class)
	public @ResponseBody User save(@RequestBody @Valid User user) throws Exception {
		return userService.save(convertAndEncodePassword(user));
	}

	/* Swagger */
	@Operation(summary = "Update a user")
	@ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(anyOf = {
			Admin.class, Client.class }, discriminatorProperty = "role")) }, description = "User updated ok")
	@ApiResponse(responseCode = "404", content = {
			@Content(mediaType = "application/json") }, description = "Resource not found")
	/**/
	@PutMapping("/update")
	@Validated(OnUserUpdate.class)
	public @ResponseBody User update(@RequestBody @Valid User user) {
		return userService.update(convertAndEncodePassword(user));
	}

	/* Swagger */
	@Operation(summary = "Delete a user")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json") }, description = "User deleted ok")
	/**/
	@DeleteMapping("/delete/by/username/{username}")
	public void deleteByUsername(@PathVariable("username") @NotBlank String username) {
		userService.deleteByUsername(username);
	}

	private User convertAndEncodePassword(User user) {
		String rawPassword = user.getPassword();
		if (rawPassword != null) {
			user.setPassword(passwordEncoder.encode(rawPassword));
		}
		return user;
	}
}
