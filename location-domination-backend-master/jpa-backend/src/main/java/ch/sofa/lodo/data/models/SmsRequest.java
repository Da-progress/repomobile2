package ch.sofa.lodo.data.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SmsRequest {

	public static JSONObject createSmsRequest(String username, String password, String originator, String number, String message)
			throws JSONException {
		JSONObject sms = new JSONObject();
		sms.put("UserName", username);
		sms.put("Password", password);
		sms.put("Originator", originator);
		JSONArray recipients = new JSONArray();
		recipients.put(number + ":1"); // the transaction-id is irrelevant
		sms.put("Recipients", recipients);
		sms.put("MessageText", message);

		return sms;
	}
}
