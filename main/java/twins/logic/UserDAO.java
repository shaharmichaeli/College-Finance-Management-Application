package twins.logic;

import org.springframework.data.repository.PagingAndSortingRepository;

import twins.data.UserEntity;


public interface UserDAO extends PagingAndSortingRepository<UserEntity, String> {

}
