package ch.sofa.lodo.data.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;

@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service
public class FirebaseMessaging {

	public static final String MESSAGE_KEY = "message";
	private static final String BASE_URL = "https://fcm.googleapis.com";
	private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
	private static final String[] SCOPES = {MESSAGING_SCOPE};
	@Value("${firebase.messaging.service.json.classpath}")
	private String firebaseServiceFileName;
	@Value("${firebase.messaging.service.project.name}")
	private String firebaseServiceProjectName;
	@SuppressWarnings("deprecation")
	private GoogleCredential googleCredential;

	private void prettyPrint(JsonObject jsonObject) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(jsonObject) + "\n");
	}

	private String inputStreamToString(InputStream inputStream) {
		StringBuilder stringBuilder = new StringBuilder();
		Scanner scanner = new Scanner(inputStream);
		while (scanner.hasNext()) {
			stringBuilder.append(scanner.nextLine());
		}
		return stringBuilder.toString();
	}

	public void sendNotificationMessage3(MessageDto message) throws IOException {
		System.out.println(" ........... start send firebase message");

		JsonObject notificationMessage = buildNotificationMessage3(message);

		System.out.println("FCM request body for message using common notification object:");
		prettyPrint(notificationMessage);
		sendMessage3(notificationMessage);
	}

	private JsonObject buildNotificationMessage3(MessageDto message) {
		JsonObject jNotification = new JsonObject();
		jNotification.addProperty("title", message.getTitle());
		jNotification.addProperty("body", message.getMessage());

		JsonObject data = new JsonObject();
		if (message.getData().containsKey(MessageDto.GAME_STATE_KEY_MAP)) {
			data.addProperty(MessageDto.GAME_STATE_KEY, message.getData().get(MessageDto.GAME_STATE_KEY_MAP));
		}
		if (message.getData().containsKey(MessageDto.GAME_ID_KEY)) {
			data.addProperty(MessageDto.GAME_ID_KEY, message.getData().get(MessageDto.GAME_ID_KEY));
		}

		JsonObject jMessage = new JsonObject();

		// notification type of message
		jMessage.add("notification", jNotification);

		// dta type of message
		jMessage.add("data", data);

		// use just one of this XOR properties: token or topic. Can not do both
		jMessage.addProperty("token", message.getUserDeviceFirebaseToken());

		JsonObject jFcm = new JsonObject();
		jFcm.add(MESSAGE_KEY, jMessage);

		return jFcm;
	}

	private void sendMessage3(JsonObject fcmMessage) throws IOException {

		HttpURLConnection connection = sendNotification(fcmMessage);

		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			String response = inputStreamToString(connection.getInputStream());
			System.out.println(LocalDateTime.now() + ",Message sent to Firebase for delivery, response:");
			System.out.println(response);
		} else if (responseCode == 401) {
			System.out.println(LocalDateTime.now() + ", Unable to send message to Firebase:");
			String response = inputStreamToString(connection.getErrorStream());
			System.out.println(LocalDateTime.now() + ", " + response);
			refreshGoogleCredentials();
			// single retry
			HttpURLConnection connection2 = sendNotification(fcmMessage);
			responseCode = connection2.getResponseCode();
			System.out.println(LocalDateTime.now()
					+ ", Refreshed FCM token. Repeated send message to Firebase. Response code=" + responseCode);
		} else {
			System.out.println(LocalDateTime.now() + ",Unable to send message to Firebase:");
			String response = inputStreamToString(connection.getErrorStream());
			System.out.println(LocalDateTime.now() + ", " + response);
		}
	}

	private HttpURLConnection sendNotification(JsonObject fcmMessage) throws IOException {
		HttpURLConnection connection = getConnection3();

		System.out.println("Connection established to: " + connection.getURL());
		connection.setDoOutput(true);

		// debug
		for (Entry<String, List<String>> s : connection.getRequestProperties().entrySet()) {
			System.out.println("h:" + s.getKey() + "," + s.getValue());
		}
		//

		// do not use BufferedWriter nor DataOutputStream to send non-ascii characters
		byte[] message = fcmMessage.toString().getBytes(UTF_8);
		connection.getOutputStream().write(message);

		return connection;
	}

	private HttpURLConnection getConnection3() throws IOException {
		// [START use_access_token]
		String FCM_SEND_ENDPOINT = "/v1/projects/" + firebaseServiceProjectName + "/messages:send";
		URL url = new URL(BASE_URL + FCM_SEND_ENDPOINT);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestProperty("Authorization", "Bearer " + googleCredential.getAccessToken());
		httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
		return httpURLConnection;
		// [END use_access_token]
	}

	@PostConstruct
	public void loadFirebaseServerConfig() {
		refreshGoogleCredentials();
	}

	@SuppressWarnings("deprecation")
	private void refreshGoogleCredentials() {
		try {
			GoogleCredential googleCredential = GoogleCredential.fromStream(
					new ClassPathResource(firebaseServiceFileName).getInputStream()).createScoped(
					Arrays.asList(SCOPES));
			googleCredential.refreshToken();
			this.googleCredential = googleCredential;
			System.out.println(LocalDateTime.now() + " .. FCM refreshed token");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class MessageDto {

		public static final String GAME_STATE_KEY_MAP = "game_state_map";
		public static final String GAME_ID_KEY = "game_id";
		public static final String GAME_STATE_KEY = "game_state"; // must be equal in frontend

		private final String userDeviceFirebaseToken;
		private final String title;
		private final String message;
		private Map<String, String> data = new HashMap<>();

		public MessageDto(String userDeviceFirebaseToken, String title, String message) {
			super();
			this.userDeviceFirebaseToken = userDeviceFirebaseToken;
			this.title = title;
			this.message = message;
		}

		public String getUserDeviceFirebaseToken() {
			return userDeviceFirebaseToken;
		}

		public String getTitle() {
			return title;
		}

		public String getMessage() {
			return message;
		}

		public Map<String, String> getData() {
			return data;
		}

		public void setData(Map<String, String> data) {
			this.data = data;
		}

		@Override
		public String toString() {
			return "MessageDto{" +
					"userDeviceFirebaseToken='" + userDeviceFirebaseToken + '\'' +
					", title='" + title + '\'' +
					", message='" + message + '\'' +
					", data=" + data +
					'}';
		}
	}
}
