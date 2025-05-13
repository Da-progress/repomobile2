package ch.sofa.lodo.admin.notifications;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;

public class Notifications {

	private Notifications() {

	}

	public static void showSuccess() {
		Notification success = new Notification("Saved");
		success.setDuration(2000);
		success.setPosition(Position.MIDDLE);
		success.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		success.open();
	}
}
