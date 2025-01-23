package cat.institutmarianao.sailing.ws.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.sailing.ws.exception.NotFoundException;
import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.model.Trip.Status;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import cat.institutmarianao.sailing.ws.repository.TripRepository;
import cat.institutmarianao.sailing.ws.service.TripService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotNull;


@Validated
@Service
public class TripServiceImpl implements TripService{

	@Autowired
	private TripRepository TripRepository;

	@Autowired
	private MessageSource messageSource;


	@Override
	public List<Trip> findAll() {
        return TripRepository.findAll();
    }
	@Override
	public List<Trip> getByClientUsername(String clientUsername) {
	    List<Trip> trips = TripRepository.findByClientUsername(clientUsername);
	    if (trips.isEmpty()) {
	        throw new NotFoundException(messageSource.getMessage(
	            "error.NotFound.resource.by.id",
	            new String[] { "Trip", clientUsername }, 
	            LocaleContextHolder.getLocale()
	        ));
	    }
	    return trips;
	}


	@Override
	public Trip save(@NotNull @Valid Trip trip) {
		if (TripRepository.existsById(trip.getId())) 
			throw new ValidationException (messageSource.getMessage("error.UserService.username.exists",
					new String[] { trip.getId().toString() }, LocaleContextHolder.getLocale()));

		return TripRepository.saveAndFlush(trip);
	}

	@Override
	public Trip action(@NotNull @Valid Trip trip) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Trip getById(long id) {
		// TODO Auto-generated method stub
		return null;
	}




}
