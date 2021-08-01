package twins.logic;

import java.util.List;

public interface AdvancedOperationService extends OperationService {

	public List<OperationBoundary> getAllOperation(String adminSpace, String adminEmail, int size, int page);

	public String invokeOperation(OperationBoundary input, int size, int page);
}
