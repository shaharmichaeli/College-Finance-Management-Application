package twins.logic;

import java.util.List;

public interface OperationService {

	public String invokeOperation(OperationBoundary operation);

	public OperationBoundary invokeAsynchronusOperation(OperationBoundary operation);

	public List<OperationBoundary> getAllOperation(String adminSpace, String adminEmail);

	public void deleteAllOperations(String adminSpace, String adminEmail);

}
