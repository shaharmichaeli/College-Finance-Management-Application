package twins.logic;

import java.util.List;

import org.springframework.data.domain.Pageable;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import twins.data.ItemEntity;

public interface ItemDAO extends PagingAndSortingRepository<ItemEntity, String> {

	public ItemEntity findByItemidAndItemspaceAndActive(
			@Param("Itemid") String itemId,
			@Param("Itemspace") String itemSpace,
			@Param("active") boolean active); 
	
	public List<ItemEntity> findAllByActive(
			@Param("active") boolean active,
			Pageable pageable);
	
	public List<ItemEntity> findAllByActiveAndType(
			@Param("active") boolean active,
			@Param("type") String type,
			Pageable pageable);
	
	public ItemEntity findByItemidAndItemspace(
			@Param("Itemid") String id,@Param("Itemspace") String space);
	
	public ItemEntity findByItemid(
			@Param("Itemid") String id);
	
}
