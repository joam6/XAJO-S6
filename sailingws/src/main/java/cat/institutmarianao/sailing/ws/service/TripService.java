package cat.institutmarianao.sailing.ws.service;

import java.util.Date;
import java.util.List;

import cat.institutmarianao.sailing.ws.model.Action;
import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.model.Trip.Status;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import cat.institutmarianao.sailing.ws.model.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public interface TripService {

	List<Trip> findAll(Category category, Status status, User client, Date from, Date to);
	
	Trip getById(@Positive Long id);

	Trip save(@NotNull Trip trip);
	
	/**
	 * Tracking
	 */
	Action save(@NotNull @Valid Action action);

}
