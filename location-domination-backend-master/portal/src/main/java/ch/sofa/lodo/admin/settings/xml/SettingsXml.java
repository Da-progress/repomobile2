package ch.sofa.lodo.admin.settings.xml;

import ch.sofa.lodo.admin.settings.xml.jaxb.EventModule;
import ch.sofa.lodo.admin.settings.xml.jaxb.Settings;
import ch.sofa.xml.XmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import java.io.File;

@Component
public class SettingsXml {

	private final ServletContext servletContext;

	@Value("${dir.settings}")
	private String settingsDir;

	@Value("${xml.settings}")
	private String settingsFile;

	private Settings settings;

	@Autowired
	public SettingsXml(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@PostConstruct
	public void init() {
		try {
			System.out.println("... settings file path: " + settingsDir + File.separator + settingsFile);
			settings = XmlReader.fromFile(new File(settingsDir + File.separator + settingsFile), Settings.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("An error occurred while reading the Settings XML file: " + e.getMessage());
		}
	}

	public EventModule getEventModule() {
		return settings.getEventModule();
	}
}
