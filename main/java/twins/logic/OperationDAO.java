package twins.logic;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import twins.data.ItemEntity;
import twins.data.OperationEntity;

public interface OperationDAO extends PagingAndSortingRepository<OperationEntity, String> {

	// C - Create
	
	// R - Read


	// BE VERY CAREFUL!!!! the name of the method have meaning!!!
	//public List<MessageEntity> findAllByImpihefilhwefiwheghe(
	//		@Param("important") boolean important,
	//		Pageable pageable);
	
	// U - Update
	
	// D - Delete
}
