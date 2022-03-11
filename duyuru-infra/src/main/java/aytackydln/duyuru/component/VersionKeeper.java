package aytackydln.duyuru.component;

import java.util.Optional;

import aytackydln.duyuru.ContainerInitializer;
import org.springframework.stereotype.Component;

@Component
public class VersionKeeper {
	private final String title;
	private final String implementation;

	public VersionKeeper() {
		Package panelPackage = ContainerInitializer.class.getPackage();
		title = Optional.ofNullable(panelPackage.getImplementationTitle()).orElse("Link Notifier");
		implementation = Optional.ofNullable(panelPackage.getImplementationVersion()).orElse("dev");
	}

	public String getVersionFull() {
		return title + ": " + implementation;
	}

	public String getImplementation(){
		return implementation;
	}
}
