package twins.logic;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class OperationController {

	private AdvancedOperationService operationService;

	@Autowired
	public OperationController(AdvancedOperationService operationService) {
		super();
		this.operationService = operationService;
	}

	@RequestMapping(path = "/twins/operations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public String invokeOperation(@RequestBody OperationBoundary input,
			@RequestParam(name = "size", required = false, defaultValue = "5") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return this.operationService.invokeOperation(input,size,page);
	}

	@RequestMapping(path = "/twins/operations/async", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary invokeAsyncOperation(@RequestBody OperationBoundary input) {
		return this.operationService.invokeAsynchronusOperation(input);
	}

	/* !!! ADMIN MAPPING RELATED TO OPERATIONS !!! */

	@RequestMapping(path = "/twins/admin/operations/{userSpace}/{userEmail}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public OperationBoundary[] getOperationsList(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "5") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		List<OperationBoundary> rv = this.operationService.getAllOperation(userSpace, userEmail, size, page);
		return rv.toArray(new OperationBoundary[0]);
	}

	@RequestMapping(path = "/twins/admin/operations/{userSpace}/{userEmail}", method = RequestMethod.DELETE)
	public void deleteOperationsList(@PathVariable("userSpace") String userSpace,
			@PathVariable("userEmail") String userEmail) {
		this.operationService.deleteAllOperations(userSpace, userEmail);
	}
	
	
}
