package ch.sofa.lodo.data.repositories;

import ch.sofa.lodo.data.constants.UserType;
import ch.sofa.lodo.data.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	User findByUsernameAndUserType(String username, UserType userType);

	List<User> findAllByUsernameAndDeletedFalse(String username);

	List<User> findAllByUserType(UserType userType);

	List<User> findAllByUserType(UserType userType, Sort sort);

	List<User> findAllByUserTypeAndDeletedFalse(UserType userType);

	List<User> findAllByUserTypeAndDeletedFalse(UserType userType, Sort sort);

	List<User> findAllByIdAndAuthorizationCode(Long id, String code);

	List<User> findAllByMobileNumber(String mobileNr);
}
