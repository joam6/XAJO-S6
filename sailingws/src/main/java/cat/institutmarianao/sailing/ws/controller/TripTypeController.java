package cat.institutmarianao.sailing.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import cat.institutmarianao.sailing.ws.service.TripTypeService;
import cat.institutmarianao.sailing.ws.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Tag(name = "Trip Type", description = "Trip type API")

@RestController
@RequestMapping("/triptypes")
public class TripTypeController {
	@Autowired
	private TripTypeService TripTypeService;
	
	/* Swagger */
	@Operation(summary = "Find all trip types filtered", description = "Retrieve all trip types filtered from the database.")
	@ApiResponse(responseCode = "200", content = {
			@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = TripType.class))) }, description = "Trip types retrieved ok")
	/**/
	@GetMapping("/find/all")
	public List<TripType> findAll(@RequestParam(value = "category", required = false) Category category,
								@RequestParam(value = "priceFrom", required = false) @PositiveOrZero Double priceFrom,
								@RequestParam(value = "priceTo", required = false) @PositiveOrZero Double priceTo,
								@RequestParam(value = "maxPlacesFrom", required = false) @PositiveOrZero Integer maxPlacesFrom,
								@RequestParam(value = "maxPlacesTo", required = false) @PositiveOrZero Integer maxPlacesTo,
								@RequestParam(value = "durationFrom", required = false) @PositiveOrZero Integer durationFrom,
								@RequestParam(value = "durationTo", required = false) @PositiveOrZero Integer durationTo) {
		// TODO Find all trip types filtered
		return TripTypeService.findAll();
	}

	/* Swagger */
	@Operation(summary = "Get trip type by id")
	@ApiResponse(responseCode = "200", description = "Trip type retrieved ok")
	@ApiResponse(responseCode = "404", description = "Resource not found")
	/**/
	@GetMapping("/get/by/id/{id}")
	public TripType findById(@PathVariable("id") @NotNull Long id) {
		// TODO Get trip type by id
		return TripTypeService.getById(id);
	}
}
