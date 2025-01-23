package cat.institutmarianao.sailing.ws.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import cat.institutmarianao.sailing.ws.SailingWsApplication;
import cat.institutmarianao.sailing.ws.model.Action;
import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.service.TripService;
import cat.institutmarianao.sailing.ws.service.UserService;
import cat.institutmarianao.sailing.ws.validation.groups.OnActionCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnTripCreate;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.Trip.Status;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;

@Tag(name = "Trip", description = "Trip API")

@RestController
@RequestMapping("/trips")
@SecurityRequirement(name = "Bearer Authentication")
@Validated
public class TripController {

	@Autowired
	private TripService tripService;

	@Autowired
	private UserService userService;

	//@Autowired
	//private BookedPlaceService bookedPlaceService;

	@Operation(summary = "Retrieve all trips filtered", description = "Retrieve all trips filtered from the database.")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Trip.class))) }, description = "Trips retrieved ok")
	@GetMapping(value = "/find/all")
	public @ResponseBody List<Trip> findAll(@RequestParam(value = "category", required = false) Category category,
											@RequestParam(value = "status", required = false) Status status,
											@RequestParam(value = "client", required = false) String clientUsername,
											@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = SailingWsApplication.DATE_PATTERN) @Parameter(description = SailingWsApplication.DATE_PATTERN) Date from,
											@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = SailingWsApplication.DATE_PATTERN) @Parameter(description = SailingWsApplication.DATE_PATTERN) Date to) {
		return findAllTrips(category, status, clientUsername, from, to);
	}

	@Operation(summary = "Retrieve all trips filtered by client username", description = "Retrieve all trips filtered by client username from the database.")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Trip.class))) }, description = "Trips by client username retrieved ok")
	@GetMapping(value = "/find/all/by/client/username/{username}")
	public @ResponseBody List<Trip> findAllByClientUsername(@PathVariable("username") @NotNull String username,
			@RequestParam(value = "category", required = false) Category category,
			@RequestParam(value = "status", required = false) Status status,
			@RequestParam(value = "from", required = false) @DateTimeFormat(pattern = SailingWsApplication.DATE_PATTERN) @Parameter(description = SailingWsApplication.DATE_PATTERN) Date from,
			@RequestParam(value = "to", required = false) @DateTimeFormat(pattern = SailingWsApplication.DATE_PATTERN) @Parameter(description = SailingWsApplication.DATE_PATTERN) Date to) {

		return findAllTrips(category, status, username, from, to);
	}

	/* Swagger */
	@Operation(summary = "Get trip by id")
	@ApiResponse(responseCode = "200", description = "Trip retrieved ok")
	@ApiResponse(responseCode = "404", description = "Resource not found")
	/**/
	@GetMapping("/get/by/id/{id}")
	public Trip findById(@PathVariable("id") @NotNull Long id) {
		return tripService.getById(id);
	}
	
	
	@Operation(summary = "Save a trip", description = "Saves a trip in the database. The response is the stored trip from the database.")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Trip.class)) }, description = "Trip saved ok")
	@ApiResponse(responseCode = "500", content = {
			@Content() }, description = "Error saving the trip. See response body for more details")
	@PostMapping(value = "/save")
	public Trip save(@RequestBody @Validated(OnTripCreate.class) @NotNull Trip trip) {
		return tripService.save(trip);
	}

	/* Swagger */
	@Operation(summary = "Save an action of a trip (in its tracking). Action at the end of trip tracking")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Action.class)) }, description = "Trip action saved ok")
	/**/
	@PostMapping("/save/action")
	public Action saveAction(@RequestBody @Validated(OnActionCreate.class) Action action) {
		return tripService.save(action);
	}

	private List<Trip> findAllTrips(Category category, Status status, String clientUsername, Date from, Date to) {
		User client = null;
		if (clientUsername != null) {
			client = userService.getByUsername(clientUsername);
		}
		
		return tripService.findAll(category, status, client, from, to);
	}
}
