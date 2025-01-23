package cat.institutmarianao.sailing.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import cat.institutmarianao.sailing.ws.model.TripType;

public interface TripTypeRepository extends JpaRepository <TripType, Long>{
		List<TripType> findAll();
}
