package twins.logic;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MessageNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -3852861819213897449L;

	public MessageNotFoundException() {
	}

	public MessageNotFoundException(String message) {
		super(message);
	}

	public MessageNotFoundException(Throwable cause) {
		super(cause);
	}

	public MessageNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
