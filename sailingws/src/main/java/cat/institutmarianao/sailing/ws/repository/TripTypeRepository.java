package cat.institutmarianao.sailing.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cat.institutmarianao.sailing.ws.model.TripType;
import cat.institutmarianao.sailing.ws.model.TripType.Category;
import jakarta.validation.constraints.PositiveOrZero;

public interface TripTypeRepository extends JpaRepository<TripType, Long> {

	@Query("SELECT t FROM TripType t WHERE "
			+ "(t.category = ?1 OR ?1 IS NULL) AND "
			+ "(t.price >= ?2 OR ?2 = 0) AND "
			+ "(t.price <= ?3 OR ?3 = 0) AND "
			+ "(t.maxPlaces >= ?4 OR ?4 = 0) AND "
			+ "(t.maxPlaces <= ?5 OR ?5 = 0) AND "
			+ "(t.duration >= ?6 OR ?6 = 0) AND "
			+ "(t.duration <= ?7 OR ?7 = 0)"
			+ "ORDER BY t.title ASC")
	List<TripType> findAllByFilters(Category category, 
			@PositiveOrZero double priceFrom, @PositiveOrZero double priceTo, 
			@PositiveOrZero int maxPlacesFrom, @PositiveOrZero int maxPlacesTo,
			@PositiveOrZero int durationFrom, @PositiveOrZero int durationTo);
}
