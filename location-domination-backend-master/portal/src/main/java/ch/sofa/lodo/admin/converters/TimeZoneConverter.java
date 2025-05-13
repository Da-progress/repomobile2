package ch.sofa.lodo.admin.converters;

import ch.sofa.lodo.admin.session.ClientBrowserData;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@SpringComponent
public class TimeZoneConverter {

	private ClientBrowserData clientBrowserData;
	private String sourceZoneId;

	@Autowired
	public TimeZoneConverter(ClientBrowserData clientBrowserData) {
		this.clientBrowserData = clientBrowserData;
		this.sourceZoneId = "UTC";
	}

	public LocalDateTime convertToTarget(LocalDateTime source) {
		return convert(source, sourceZoneId, clientBrowserData.getDetails()
				.getTimeZoneId());
	}

	public LocalDateTime convertToSource(LocalDateTime target) {
		return convert(target, clientBrowserData.getDetails()
				.getTimeZoneId(), sourceZoneId);
	}

	public LocalDateTime convert(LocalDateTime source, String sourceTimeZoneId, String targetTimeZoneId) {
		if (source == null) {
			return null;
		}
		ZonedDateTime sourceZoned = source.atZone(ZoneId.of(sourceTimeZoneId));
		ZonedDateTime targetZoned = sourceZoned.withZoneSameInstant(ZoneId.of(targetTimeZoneId));
		return targetZoned.toLocalDateTime();
	}
}
