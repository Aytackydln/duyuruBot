package aytackydln.duyuru.mvc.message;

import lombok.Value;

@Value
public class WarningMessage implements ViewMessage {
	String message;

	@Override
	public final String getHtmlClass() {
		return "alert-warning";
	}

	@Override
	public String getHeader() {
		return "Warning!";
	}
}
