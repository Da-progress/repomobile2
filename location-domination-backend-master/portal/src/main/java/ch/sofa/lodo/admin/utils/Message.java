package ch.sofa.lodo.admin.utils;

import ch.sofa.lodo.admin.i18n.I18N;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;

public class Message {

	private Message() {
	}

	public static void showWarning(String message) {

		Div content = new Div();
		content.add(message);
		content.getStyle()
				.set("color", CSS.MESSAGE_COLOR_WARNING);

		Notification notification = new Notification(content);
		notification.setDuration(3000);
		notification.setPosition(Position.MIDDLE);
		notification.open();
	}

	public static void showError(String message) {

		Div content = new Div();
		content.add(message);
		content.getStyle()
				.set("color", CSS.MESSAGE_COLOR_ERROR);

		Notification notification = new Notification(content);
		notification.setDuration(3000);
		notification.setPosition(Position.MIDDLE);
		notification.open();
	}

	public static void showLongSuccess(String message) {

		Div content = new Div();
		content.add(message);
		content.getStyle()
				.set("color", CSS.MESSAGE_COLOR_SUCCESS);

		Notification notification = new Notification(content);
		notification.setDuration(3000);
		notification.setPosition(Position.MIDDLE);
		notification.open();
	}

	public static void showException(String sUserMessage, Throwable e) {

		String messages = ExceptionUtils.getExceptionMessages(e.getCause());

		String message = sUserMessage + " " + e.getMessage();
		if ((message == null || message.isEmpty()) && (messages == null || messages.isEmpty())) {
			message = e.getClass()
					.getSimpleName();
		}

		Div content = new Div();
		content.add(message + " " + messages);
		content.getStyle()
				.set("color", CSS.MESSAGE_COLOR_ERROR);

		Dialog notification = new Dialog(content);
		notification.open();
	}

	public static void showException(Throwable e, I18N i18N) {

		String message = e.getMessage();
		if (message == null || message.isEmpty()) {
			if (!ExceptionUtils.getExceptionMessageChain(e.getCause())
					.isEmpty()) {
				showException(e.getCause(), i18N);
				return;
			} else {
				message = e.getClass()
						.getSimpleName();
			}
		}

		String messages = ExceptionUtils.getExceptionMessages(e.getCause());
		if (messages != null && messages.isEmpty()) {
			messages = null;
		}

		Div content = new Div();
		content.add(i18N.getTranslationWithAppLocale("global.exception.error.message") + message + " " + messages);
		content.getStyle()
				.set("color", CSS.MESSAGE_COLOR_ERROR);

		Notification notification = new Notification(content);
		notification.setDuration(3000);
		notification.setPosition(Position.MIDDLE);
		notification.open();
	}
}
