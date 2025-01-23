package cat.institutmarianao.sailing.ws.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.sailing.ws.exception.NotFoundException;
import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import cat.institutmarianao.sailing.ws.repository.TripTypeRepository;
import cat.institutmarianao.sailing.ws.service.TripTypeService;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Validated
@Service
public class TripTypeServiceImpl implements TripTypeService {
	@Autowired
	private TripTypeRepository tripTypeRepository;

	@Autowired
	private MessageSource messageSource;
	
	@Override
	public List<TripType> findAll(Category category, 
			@PositiveOrZero double priceFrom, @PositiveOrZero double priceTo, 
			@PositiveOrZero int maxPlacesFrom, @PositiveOrZero int maxPlacesTo,
			@PositiveOrZero int durationFrom, @PositiveOrZero int durationTo) {
		return tripTypeRepository.findAllByFilters(category, priceFrom, priceTo, maxPlacesFrom, maxPlacesTo, durationFrom, durationTo);
	}

	@Override
	public TripType getById(@Positive Long id) {
		return tripTypeRepository.findById(id).orElseThrow(() -> new NotFoundException(messageSource.getMessage("error.NotFound.resource.by.id", new String[] { "Trip Type", id+"" }, LocaleContextHolder.getLocale())));
	}

}
