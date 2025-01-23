package cat.institutmarianao.sailing.ws.service.impl;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.sailing.ws.exception.ForbiddenException;
import cat.institutmarianao.sailing.ws.exception.NotFoundException;
import cat.institutmarianao.sailing.ws.model.Action;
import cat.institutmarianao.sailing.ws.model.Action.Type;
import cat.institutmarianao.sailing.ws.model.Booking;
import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.Trip.Status;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import cat.institutmarianao.sailing.ws.model.Departure;
import cat.institutmarianao.sailing.ws.model.Rescheduling;
import cat.institutmarianao.sailing.ws.repository.ActionRepository;
import cat.institutmarianao.sailing.ws.repository.DepartureRepository;
import cat.institutmarianao.sailing.ws.repository.TripRepository;
import cat.institutmarianao.sailing.ws.security.JwtUtils;
import cat.institutmarianao.sailing.ws.service.TripService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Service
@Validated
public class TripServiceImpl implements TripService {
	@Autowired
	private TripRepository tripRepository;

	@Autowired
	private ActionRepository actionRepository;
	
	@Autowired
	private DepartureRepository departureRepository;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Override
	public List<Trip> findAll(Category category, Status status, User client, Date from, Date to) {
		if (!(jwtUtils.isAdmin() || jwtUtils.isAuthUser(client==null?"":client.getUsername()))) 
			throw new ForbiddenException(messageSource.getMessage("error.Forbidden.trips.find", null, LocaleContextHolder.getLocale()));
		
		return tripRepository.findAllByFilters(category, status, client, from, to);
	}
	
	@Override
	public Trip getById(@Positive Long id) {
		if (!jwtUtils.isAdmin()) 
			throw new ForbiddenException(messageSource.getMessage("error.Forbidden.trips.find", null, LocaleContextHolder.getLocale()));
				
		return tripRepository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("error.NotFound.resource.by.id", new String[] { "Trip", id+"" }, LocaleContextHolder.getLocale())));
	}
	
	@Override
	public Trip save(@NotNull Trip trip) {
		if (!jwtUtils.isAuthUser(trip.getClient().getUsername()))  
			throw new ForbiddenException(messageSource.getMessage("error.Forbidden.trips.booking", null, LocaleContextHolder.getLocale()));
		
		if (!trip.getTracking().isEmpty()) 
			throw new ValidationException (messageSource.getMessage("error.Tracking.is.not.empty", null, LocaleContextHolder.getLocale()));

		Departure departure = departureRepository.findOneBytripTypeIdAndDateAndDeparture(trip.getDeparture().getTripType().getId(), trip.getDeparture().getDate(), trip.getDeparture().getDeparture());
		if (departure != null) trip.setDeparture(departure);
		
		this.bookingValidations(trip);
				
		Booking booking = Booking.builder().type(Type.BOOKING).performer(trip.getClient()).date(new Date()).trip(trip).idTrip(trip.getId()).build(); 

		trip.getTracking().add(booking);
		
		trip = tripRepository.save(trip);

		return trip;
	}

	private void bookingValidations(@NotNull Trip trip) {
		// Future booking date 
		LocalDate bookingDate = Instant.ofEpochMilli(trip.getDeparture().getDate().getTime()).atZone(ZoneId.of(Departure.TZ)).toLocalDate();
		if (!bookingDate.isAfter(LocalDate.now())) 
			throw new ValidationException (messageSource.getMessage("error.Booking.wrong.date", null, LocaleContextHolder.getLocale()));
				
		// Group Trip Type. Validate departure
		if (Category.GROUP.equals(trip.getDeparture().getTripType().getCategory())) {
			//LocalTime bookingDeparture = LocalTime.ofInstant(trip.getDeparture().getDeparture().toInstant(), ZoneId.of(Departure.TZ));
			LocalTime bookingDeparture = Instant.ofEpochMilli(trip.getDeparture().getDeparture().getTime()).atZone(ZoneId.of(Departure.TZ)).toLocalTime();
			String[] departures = trip.getDeparture().getTripType().getDepartures().split(";");
					
			if (!Arrays.stream(departures).anyMatch(bookingDeparture.format(DateTimeFormatter.ofPattern("HH:mm"))::equals))
				throw new ValidationException (messageSource.getMessage("error.Booking.wrong.departure", new Object[] { bookingDeparture.format(DateTimeFormatter.ofPattern("HH:mm")) }, LocaleContextHolder.getLocale()));
				/*boolean contains = Arrays.stream(departures).anyMatch(new Predicate<String>(){
					@Override
					public boolean test(String s) {
						return s.equals(bookingDeparture.format(DateTimeFormatter.ofPattern("HH:mm")));
					} 
				});*/
		}
				
		// Private Trip Type. No other ACTIVE reservations on the same date
		if (Category.PRIVATE.equals(trip.getDeparture().getTripType().getCategory())) {
			List<Departure> departures = departureRepository.findAllBytripTypeIdAndDate(trip.getDeparture().getTripType().getId(), trip.getDeparture().getDate());
			for (Departure departure : departures) {
				if (departure.getBookedPlaces() > 0) throw new ValidationException (messageSource.getMessage("error.Booking.same.date", null, LocaleContextHolder.getLocale()));
			}
		}
		
		// Check max places
		if (trip.getPlaces() + trip.getDeparture().getBookedPlaces() > trip.getDeparture().getTripType().getMaxPlaces())
			throw new ValidationException (messageSource.getMessage("error.Booking.not.enough.places", null, LocaleContextHolder.getLocale()));
	}
	
	
	@Override
	public Action save(@NotNull @Valid Action action) {
		if (!jwtUtils.isAuthUser(action.getPerformer().getUsername())) {	// Performer is authenticated user 
			throw new ValidationException (messageSource.getMessage("error.Tracking.performer", new String[] { action.getPerformer().getUsername() }, LocaleContextHolder.getLocale()));
		}
		
		if (Type.BOOKING.equals(action.getType())) throw new ForbiddenException(messageSource.getMessage("error.Tracking.is.booking", null, LocaleContextHolder.getLocale()));

		if (jwtUtils.isAdmin()) {  // Admin => RESCHEDULING, CANCELLATION, DONE
		}
		if (jwtUtils.isClient()) {  // Client => BOOKING, CANCELLATION (Own)
			if (Type.RESCHEDULING.equals(action.getType())) throw new ForbiddenException(messageSource.getMessage("error.Forbidden.trips.rescheduling", null, LocaleContextHolder.getLocale()));
			if (Type.DONE.equals(action.getType())) throw new ForbiddenException(messageSource.getMessage("error.Forbidden.trips.done", null, LocaleContextHolder.getLocale()));
			if (Type.CANCELLATION.equals(action.getType()) && !jwtUtils.isAuthUser(action.getPerformer().getUsername())) 
				throw new ForbiddenException(messageSource.getMessage("error.Forbidden.trips.cancel", null, LocaleContextHolder.getLocale()));
		}

		Long tripId = action.getIdTrip();

		if (tripId == null) 
			throw new ValidationException (messageSource.getMessage("error.Trip.without.id", null, LocaleContextHolder.getLocale()));
		
		
		Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new NotFoundException(messageSource.getMessage("error.NotFound.resource.by.id", new String[] { "Trip", tripId+"" }, LocaleContextHolder.getLocale())));
		
		List<Action> tracking = trip.getTracking();
		
		if (tracking == null) 
			throw new ValidationException (messageSource.getMessage("error.Tracking.is.null", null, LocaleContextHolder.getLocale()));
		
		
		if (tracking.isEmpty()) 
			throw new ValidationException (messageSource.getMessage("error.Tracking.is.empty", null, LocaleContextHolder.getLocale()));
			
		if (tracking.get(tracking.size() - 1).getDate().after(action.getDate())) 
			throw new ValidationException (messageSource.getMessage("error.Tracking.wrong.date", new Object[] { action.getDate(), tracking.get(tracking.size() - 1).getDate() }, LocaleContextHolder.getLocale()));
		

		if (!Type.BOOKING.equals(tracking.get(tracking.size() - 1).getType()) && 
			!Type.RESCHEDULING.equals(tracking.get(tracking.size() - 1).getType())) 
			throw new ValidationException (messageSource.getMessage("error.Tracking.action.not.allowed", new Object[] { action.getType(), tracking.get(tracking.size() - 1).getType() }, LocaleContextHolder.getLocale()));

		// Accions Type.BOOKING [-> Type.RESCHEDULING] [-> Type.CANCELLATION] -> Type.DONE
		switch (action.getType()) {
			case RESCHEDULING:
				// Validate new Departure
				Rescheduling rescheduling = (Rescheduling) action;
				if (!trip.getDeparture().getDate().equals(rescheduling.getOldDate()) ||
					!trip.getDeparture().getDeparture().equals(rescheduling.getOldDeparture())) 
					throw new ValidationException (messageSource.getMessage("error.Tracking.wrong.rescheduling", null, LocaleContextHolder.getLocale()));
						
				Departure oldDeparture = trip.getDeparture(); 
				
				Departure newDeparture = departureRepository.findOneBytripTypeIdAndDateAndDeparture(oldDeparture.getTripType().getId(), oldDeparture.getDate(), oldDeparture.getDeparture());
				if (newDeparture != null) trip.setDeparture(newDeparture);
				else newDeparture = Departure.builder().tripType(trip.getDeparture().getTripType()).date(rescheduling.getNewDate()).departure(rescheduling.getNewDeparture()).build();
				
				trip.setDeparture(newDeparture);
					
				this.bookingValidations(trip);
					
				departureRepository.delete(oldDeparture);

				break;

			case CANCELLATION:
				// Client 48h before 
				if (jwtUtils.isClient()) {
					LocalDateTime cancelationDate = LocalDateTime.ofInstant(action.getDate().toInstant(), ZoneId.of(Departure.TZ));
					
					if (cancelationDate.plusHours(48).isAfter(LocalDateTime.of(Instant.ofEpochMilli(trip.getDeparture().getDate().getTime()).atZone(ZoneId.of(Departure.TZ)).toLocalDate(), 
																				Instant.ofEpochMilli(trip.getDeparture().getDeparture().getTime()).atZone(ZoneId.of(Departure.TZ)).toLocalTime())))
							throw new ValidationException (messageSource.getMessage("error.Tracking.cancelation.client", null, LocaleContextHolder.getLocale()));
				}
				break;

			case DONE:
					
				break;
					
			default:
				break;
		}
		
		action.setTrip(trip);
		action = actionRepository.save(action);
			
		trip.getTracking().add(action);
		trip = tripRepository.save(trip);
		
		return action;
	}
}
