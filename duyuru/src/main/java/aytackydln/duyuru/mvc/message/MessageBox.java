package aytackydln.duyuru.mvc.message;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class MessageBox {
	private final Collection<ViewMessage> messages = new ArrayDeque<>(16);

	public List<ViewMessage> getMessages() {
		List<ViewMessage> contents = new ArrayList<>(messages);
		messages.clear();
		return contents;
	}

	public void add(ViewMessage message) {
		messages.add(message);
	}

	public void addAll(Collection<ViewMessage> messages) {
		this.messages.addAll(messages);
	}

	public void addDanger(String message) {
		messages.add(new DangerMessage(message));
	}

	public void addSuccess(String message) {
		messages.add(new SuccessMessage(message));
	}

	public void addWarning(String message) {
		messages.add(new WarningMessage(message));
	}

	public void addInfo(String message) {
		messages.add(new InfoMessage(message));
	}
}
