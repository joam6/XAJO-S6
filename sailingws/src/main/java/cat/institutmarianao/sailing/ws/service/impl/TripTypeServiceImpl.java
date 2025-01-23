package cat.institutmarianao.sailing.ws.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import cat.institutmarianao.sailing.ws.exception.ForbiddenException;
import cat.institutmarianao.sailing.ws.exception.NotFoundException;
import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import cat.institutmarianao.sailing.ws.service.TripTypeService;
import cat.institutmarianao.sailing.ws.repository.TripTypeRepository;
import cat.institutmarianao.sailing.ws.validation.groups.OnTripTypeCreate;
import cat.institutmarianao.sailing.ws.validation.groups.OnTripTypeUpdate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Validated
@Service
public class TripTypeServiceImpl implements TripTypeService {
	
	@Autowired
	private TripTypeRepository TripTypeRepository;

	@Autowired
	private MessageSource messageSource;
	
	

	
	@Override
	public TripType getById(long id) {
		return TripTypeRepository.findById(id).orElseThrow(() -> 
		new NotFoundException(messageSource.getMessage("error.NotFound.resource.by.id", 
				new Object[] { "TripType", id}, LocaleContextHolder.getLocale())));
	}




	@Override
	public List<TripType> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	/*@Override
	public List<TripType> findAllWithFilters(Category category, @PositiveOrZero Double priceFrom,
			@PositiveOrZero Double priceTo, @PositiveOrZero Integer maxPlacesFrom, @PositiveOrZero Integer maxPlacesTo,
			@PositiveOrZero Integer durationFrom, @PositiveOrZero Integer durationTo) {
		// TODO Auto-generated method stub
		return TripTypeRepository.findAllWithFilters(category, priceTo, priceTo, durationTo, durationTo, durationTo, durationTo);
		}*/
}
