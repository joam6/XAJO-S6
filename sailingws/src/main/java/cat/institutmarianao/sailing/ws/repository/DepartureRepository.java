package cat.institutmarianao.sailing.ws.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import cat.institutmarianao.sailing.ws.model.Departure;

public interface DepartureRepository extends JpaRepository<Departure, Long> {
	
	@Nullable
	Departure findOneBytripTypeIdAndDateAndDeparture(Long tripTypeId, Date date, Date departure);
	
	List<Departure> findAllBytripTypeIdAndDate(Long tripTypeId, Date date);
	
}
