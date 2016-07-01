package hr.semgen.pr;

import java.io.IOException;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.jface.window.Window;
import org.eclipse.wb.swt.ResourceManager;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

@SuppressWarnings("restriction")
public class E4LifeCycle {
	private static final Logger log = LoggerFactory.getLogger(E4LifeCycle.class);

	@PostContextCreate
	void postContextCreate(IEclipseContext workbenchContext) {
		log.debug("Post context create");
		Window.setDefaultImage(ResourceManager.getPluginImage("PeptideReader", "icons/skeleton-icon-16.png"));
	}

	// @PreSave
	// void preSave(IEclipseContext workbenchContext) {
	// }
	//
	@ProcessAdditions
	void processAdditions(IEclipseContext workbenchContext) throws IOException {
		setUpLogger();

	}
	//
	// @ProcessRemovals
	// void processRemovals(IEclipseContext workbenchContext) {
	// }

	private void setUpLogger() throws IOException {
		ILoggerFactory iLoggerFactory = LoggerFactory.getILoggerFactory();
		LoggerContext loggerContext = (LoggerContext) iLoggerFactory;
		loggerContext.reset();
		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(loggerContext);
		try {
			configurator.doConfigure(getClass().getResourceAsStream("/logback.xml"));
		} catch (JoranException e) {
			throw new IOException(e.getMessage(), e);
		}
	}
}
