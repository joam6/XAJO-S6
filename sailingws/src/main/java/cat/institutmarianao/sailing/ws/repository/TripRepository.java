package cat.institutmarianao.sailing.ws.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cat.institutmarianao.sailing.ws.model.Trip;
import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.Trip.Status;
import cat.institutmarianao.sailing.ws.model.TripType.Category;

public interface TripRepository extends JpaRepository<Trip, Long> {

	@Query("SELECT t FROM Trip t INNER JOIN t.departure d WHERE "
			+ "(d.tripType.category = ?1 OR ?1 IS NULL) AND "
			+ "(t.status = ?2 OR ?2 IS NULL) AND "
			+ "(t.client = ?3 OR ?3 IS NULL) AND "
			+ "(d.date >= ?4 OR ?4 IS NULL) AND "
			+ "(d.date <= ?5 OR ?5 IS NULL) "
			+ "ORDER BY d.date ASC, d.departure ASC ")
	List<Trip> findAllByFilters(Category category, Status status, User client, Date from, Date to);
	
}
