package cat.institutmarianao.sailing.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cat.institutmarianao.sailing.ws.model.User;
import cat.institutmarianao.sailing.ws.model.User.Role;

public interface UserRepository extends JpaRepository<User, String> {
	
	@Query("SELECT u FROM User u WHERE "
			+ "(u.role IN ?1 OR ?1 IS NULL) AND "
			+ "(u.username LIKE %?2% OR ?2 IS NULL) "
			+ "ORDER BY u.username ASC ")
	List<User> findAllByFilters(Role[] roles, String fullName);
}
