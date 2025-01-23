package cat.institutmarianao.sailing.ws.service;

import java.util.List;

import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public interface TripTypeService {

	List<TripType> findAll(Category category, 
						@PositiveOrZero double priceFrom, @PositiveOrZero double priceTo, 
						@PositiveOrZero int maxPlacesFrom, @PositiveOrZero int maxPlacesTo,
						@PositiveOrZero int durationFrom, @PositiveOrZero int durationTo);

	TripType getById(@Positive Long id);
}
