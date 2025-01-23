package cat.institutmarianao.sailing.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cat.institutmarianao.sailing.ws.model.Action;

public interface ActionRepository extends JpaRepository<Action, Long> {

	List<Action> findByTripIdOrderByDateDesc(Long tripId);
}
