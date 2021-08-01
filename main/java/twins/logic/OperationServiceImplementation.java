package twins.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import twins.data.ItemEntity;
import twins.data.OperationEntity;

@Service
public class OperationServiceImplementation implements AdvancedOperationService {

	private AdvancedUserService userService;
	private AdvancedItemService itemService;
	private ItemDAO itemDAO;
	private OperationDAO operationsDAO;
	private ObjectMapper jackson;
	private String space;

	public OperationServiceImplementation() {
	}

	@Autowired
	public OperationServiceImplementation(OperationDAO operationsDAO, AdvancedUserService userService,
			AdvancedItemService itemService, ItemDAO itemDAO) {
		super();
		this.operationsDAO = operationsDAO;
		this.jackson = new ObjectMapper();
		this.userService = userService;
		this.itemService = itemService;
		this.itemDAO = itemDAO;
	}

	// Read space name from Spring configuration property file
	@Value("${spring.application.name:CollegeFinance}")
	public void setSpace(String space) {
		this.space = space;
	}

	@Override
	@Transactional
	public String invokeOperation(OperationBoundary input, int size, int page) {

		UserBoundary userResult = this.userService.login(input.getInvokedBy().getUserId().getSpace(),
				input.getInvokedBy().getUserId().getEmail());

		if (!userResult.getRole().equals("PLAYER"))
			throw new MessageNotFoundException("OperationServiceImplementaion: invokeOperation: user MUST be player");

		itemService.getActiveItem(input.getInvokedBy().getUserId().getEmail(),
				input.getInvokedBy().getUserId().getSpace(), input.getItem().getItemId().getSpace(),
				input.getItem().getItemId().getId());

		OperationEntity entity = this.convertFromBoundary(input);
		entity.setCreatedTimestamp(new Date());
		if (input.getOperationId() == null) {
			entity.setOperationId(UUID.randomUUID().toString());
			entity.setOperationSpace(this.space);
		}
		// store entity to database using INSERT query
		entity = this.operationsDAO.save(entity);

		String rv = "";

		switch (input.getType().toLowerCase()) {
		//Deprecated
//		case "student_payment":
//			rv = getStudentPayment(input.getItem().getItemId().getSpace(), input.getItem().getItemId().getId());
//			break;
//		case "worker_salary":
//			rv = getWorkerSalary(input.getItem().getItemId().getSpace(), input.getItem().getItemId().getId());
//			break;
//		case "get_student_information":
//			rv = getStudentInformation(input.getItem().getItemId().getSpace(), input.getItem().getItemId().getId());
//			break;
//		case "get_worker_information":
//			rv = getWorkerInformation(input.getItem().getItemId().getSpace(), input.getItem().getItemId().getId());
//			break;
		case "get_student_information":
			rv = getStudents(size, page);
			break;
		case "get_worker_information":
			rv = getWorkers(size, page);
			break;
		case "generate_report":
			rv = generateReport(size, page);
			break;
		default:
			break;
		}

		return rv;
	}

	@Deprecated
	@Override
	@Transactional(readOnly = true)
	public String invokeOperation(OperationBoundary input) {

		throw new MessageNotFoundException("deprecated operation - use the new API invokeOperation(input ,size, page)");
	}

	@Override
	@Transactional
	public OperationBoundary invokeAsynchronusOperation(OperationBoundary input) {

		UserBoundary result = this.userService.login(input.getInvokedBy().getUserId().getSpace(),
				input.getInvokedBy().getUserId().getEmail());

		if (!result.getRole().equals("PLAYER"))
			throw new MessageNotFoundException("OperationServiceImplementaion: invokeAsynchronusOperation: user MUST be player");

		itemService.getActiveItem(input.getInvokedBy().getUserId().getEmail(),
				input.getInvokedBy().getUserId().getEmail(), input.getItem().getItemId().getSpace(),
				input.getItem().getItemId().getId());

		OperationEntity entity = this.convertFromBoundary(input);
		entity.setCreatedTimestamp(new Date());
		if (input.getOperationId() == null) {
			entity.setOperationId(UUID.randomUUID().toString());
			entity.setOperationSpace(this.space);
		}
		// store entity to database using INSERT query
		entity = this.operationsDAO.save(entity);

		return this.convertToBoundary(entity); // convert entity to boundary
	}

	@Deprecated
	@Override
	@Transactional(readOnly = true)
	public List<OperationBoundary> getAllOperation(String adminSpace, String adminEmail) {
//		if (!isBoundaryValidAdmin(adminSpace, adminEmail))
//			throw new RuntimeException(); // TODO: Change to 404 error
//
//		Iterable<OperationEntity> allEntities = this.OperationsDAO.findAll();
//
//		List<OperationBoundary> rv = new ArrayList<>();
//		for (OperationEntity entity : allEntities) {
//			OperationBoundary boundary = convertToBoundary(entity);
//			rv.add(boundary);
//		}
//		return rv;
		throw new MessageNotFoundException(
				"deprecated operation - use the new API getAllOperation(adminSpace, adminEmail, size, page)");

	}

	@Override
	@Transactional(readOnly = true)
	public List<OperationBoundary> getAllOperation(String adminSpace, String adminEmail, int size, int page) {

		UserBoundary result = this.userService.login(adminSpace, adminEmail);
		if (!result.getRole().equals("ADMIN"))
			throw new MessageNotFoundException("OperationServiceImplementaion: getAllOperation: user MUST be admin");

		Page<OperationEntity> pageOfEntities = this.operationsDAO
				.findAll(PageRequest.of(page, size, Direction.ASC, "type", "operationId"));

		List<OperationEntity> entities = pageOfEntities.getContent();
		List<OperationBoundary> rv = new ArrayList<>();
		for (OperationEntity entity : entities) {
			OperationBoundary boundary = convertToBoundary(entity);
			rv.add(boundary);
		}
		return rv;
	}

	@Override
	@Transactional
	public void deleteAllOperations(String adminSpace, String adminEmail) {

		UserBoundary result = this.userService.login(adminSpace, adminEmail);
		if (!result.getRole().equals("ADMIN"))
			throw new MessageNotFoundException("OperationServiceImplementaion: deleteAllOperations: user MUST be admin");

		this.operationsDAO.deleteAll();
	}

	/* ############ College finance functions ############ */

	//Theres no need for these funcitons.
//	public String getStudentInformation(String itemSpace, String itemId) {
//		ItemEntity student = this.itemDAO.findByItemidAndItemspace(itemId, itemSpace);
//		return marshal(student);
//	}
//
//	public String getWorkerInformation(String itemSpace, String itemId) {
//		ItemEntity worker = this.itemDAO.findByItemidAndItemspace(itemId, itemSpace);
//		return marshal(worker);
//	}

//	public String getStudentPayment(String itemSpace, String itemId) {
//		ItemEntity student = this.itemDAO.findByItemidAndItemspace(itemId, itemSpace);
//		return student.getItemAttributes();
//	}
//
//	public String getWorkerSalary(String itemSpace, String itemId) {
//		ItemEntity worker = this.itemDAO.findByItemidAndItemspace(itemId, itemSpace);
//		return worker.getItemAttributes();
//	}
	
	public String generateReport(int size, int page) {
		List<ItemEntity> students = this.itemDAO.findAllByActiveAndType(true, "student",
				PageRequest.of(page, size, Direction.DESC, "uniqueId"));
		List<ItemEntity> workers = this.itemDAO.findAllByActiveAndType(true, "worker",
				PageRequest.of(page, size, Direction.DESC, "uniqueId"));
		String rv = "[";
		for (int i = 0; i < students.size(); i++) {
				rv += "{";
				rv += "\"id\":" + "\"" + students.get(i).getItemid() + "\"" + ",";
				rv += "\"name\":" + "\"" + students.get(i).getName() + "\"" + ",";
				rv += "\"type\":" + "\"" + students.get(i).getType() + "\"" + ",";
				rv +=  "\"itemAttributes\":";
				rv += students.get(i).getItemAttributes();
				rv += "}" + ",";
		}
		for (int i = 0; i < workers.size(); i++) {
			if (i == workers.size() - 1) {
				rv += "{";
				rv += "\"id\":" + "\"" + workers.get(i).getItemid() + "\"" + ",";
				rv += "\"name\":" + "\"" + workers.get(i).getName() + "\"" + ",";
				rv += "\"type\":" + "\"" + workers.get(i).getType() + "\"" + ",";
				rv +=  "\"itemAttributes\":";
				rv += workers.get(i).getItemAttributes();
				rv += "}";
			
			} else {
				rv += "{";
				rv += "\"id\":" + "\"" + workers.get(i).getItemid() + "\"" + ",";
				rv += "\"name\":" + "\"" + workers.get(i).getName() + "\"" + ",";
				rv += "\"type\":" + "\"" + workers.get(i).getType() + "\"" + ",";
				rv +=  "\"itemAttributes\":";
				rv += workers.get(i).getItemAttributes();
				rv += "}" + ",";
			}
		}
		rv += "]";
		return rv;
		
	}
	
	public String getStudents(int size, int page) {
		List<ItemEntity> report = this.itemDAO.findAllByActiveAndType(true, "student",
				PageRequest.of(page, size, Direction.DESC, "uniqueId"));
		String rv = "[";
		for (int i = 0; i < report.size(); i++) {
			if (i == report.size() - 1) {
				rv += "{";
				rv += "\"id\":" + "\"" + report.get(i).getItemid() + "\"" + ",";
				rv += "\"name\":" + "\"" + report.get(i).getName() + "\"" + ",";
				rv +=  "\"itemAttributes\":";
				rv += report.get(i).getItemAttributes();
				rv += "}";
			
			} else {
				rv += "{";
				rv += "\"id\":" + "\"" + report.get(i).getItemid() + "\"" + ",";
				rv += "\"name\":" + "\"" + report.get(i).getName() + "\"" + ",";
				rv +=  "\"itemAttributes\":";
				rv += report.get(i).getItemAttributes();
				rv += "}" + ",";
			}
		}
		rv += "]";
		return rv;
	}

	public String getWorkers(int size, int page) {
		List<ItemEntity> report = this.itemDAO.findAllByActiveAndType(true, "worker",
				PageRequest.of(page, size, Direction.DESC, "uniqueId"));
		String rv = "[";
		for (int i = 0; i < report.size(); i++) {
			if (i == report.size() - 1) {
				rv += "{";
				rv += "\"id\":" + "\"" + report.get(i).getItemid() + "\"" + ",";
				rv += "\"name\":" + "\"" + report.get(i).getName() + "\"" + ",";
				rv +=  "\"itemAttributes\":";
				rv += report.get(i).getItemAttributes();
				rv += "}";
			
			} else {
				rv += "{";
				rv += "\"id\":" + "\"" + report.get(i).getItemid() + "\"" + ",";
				rv += "\"name\":" + "\"" + report.get(i).getName() + "\"" + ",";
				rv +=  "\"itemAttributes\":";
				rv += report.get(i).getItemAttributes();
				rv += "}" + ",";
			}
		}
		rv += "]";
		return rv;
	}

	private OperationEntity convertFromBoundary(OperationBoundary boundary) {
		OperationEntity entity = new OperationEntity();

		if (boundary.getOperationId() != null) {
			entity.setOperationId(boundary.getOperationId().getId());
			entity.setOperationSpace(boundary.getOperationId().getSpace());
		}
		entity.setType(boundary.getType());
		entity.setCreatedTimestamp(boundary.getCreatedTimestamp());

		entity.setItemId(boundary.getItem().getItemId().getId());
		entity.setItemSpace(boundary.getItem().getItemId().getSpace());

		entity.setInvokedByEmail(boundary.getInvokedBy().getUserId().getEmail());
		entity.setInvokedBySpace(boundary.getInvokedBy().getUserId().getSpace());

		String json = this.marshal(boundary.getOperationAttributes());
		entity.setOperationAttributes(json);

		return entity;
	}

	private OperationBoundary convertToBoundary(OperationEntity entity) {
		OperationBoundary boundary = new OperationBoundary();

		boundary.setOperationId(new OperationId(entity.getOperationSpace(), entity.getOperationId()));
		boundary.setType(entity.getType());
		boundary.setCreatedTimestamp(entity.getCreatedTimestamp());
		boundary.setOperationAttributes(
				(Map<String, Object>) this.unmarshal(entity.getOperationAttributes(), Map.class));

		boundary.setItem(new Item(entity.getItemId(), entity.getItemSpace()));
		boundary.setInvokedBy(new User(entity.getInvokedByEmail(), entity.getInvokedBySpace()));

		return boundary;
	}

	// use Jackson to convert JSON to Object
	private <T> T unmarshal(String json, Class<T> type) {
		try {
			return this.jackson.readValue(json, type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String marshal(Object moreDetails) {
		try {
			return this.jackson.writeValueAsString(moreDetails);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isBoundaryValid(OperationBoundary input) {
		if (input.getType() == null)
			return false;

		if (input.getType().equals(""))
			return false;

		if (input.getInvokedBy() == null)
			return false;

		if (input.getInvokedBy().getUserId() == null)
			return false;

		if (input.getInvokedBy().getUserId().getEmail() == null)
			return false;

		if (input.getInvokedBy().getUserId().getSpace() == null)
			return false;

		if (input.getInvokedBy().getUserId().getEmail().equals(""))
			return false;

		if (input.getInvokedBy().getUserId().getSpace().equals(""))
			return false;

		if (input.getItem() == null)
			return false;

		if (input.getItem().getItemId() == null)
			return false;

		if (input.getItem().getItemId().getId() == null)
			return false;

		if (input.getItem().getItemId().getSpace() == null)
			return false;

		if (input.getItem().getItemId().getId().equals(""))
			return false;

		if (input.getItem().getItemId().getSpace().equals(""))
			return false;

		return true;
	}

}
