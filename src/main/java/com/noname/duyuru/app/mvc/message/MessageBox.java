package com.noname.duyuru.app.mvc.message;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class MessageBox {
	private final Collection<IViewMessage> messages = new ArrayDeque<>(16);

	public List<IViewMessage> getMessages() {
		List<IViewMessage> contents = new ArrayList<>(messages);
		messages.clear();
		return contents;
	}

	public void add(IViewMessage message) {
		messages.add(message);
	}

	public void addAll(Collection<IViewMessage> messages) {
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
