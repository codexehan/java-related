package codexe.han.fcm.test;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

/**
 * HTTP V1通信
 * 向特定设备发送消息
 * POST https://fcm.googleapis.com/v1/projects/myproject-b5ae1/messages:send HTTP/1.1
 *
 * Content-Type: application/json
 * Authorization: Bearer ya29.ElqKBGN2Ri_Uz...HnS_uNreA
 *
 * {
 *   "message":{
 *     "token" : "bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1...",
 *     "notification" : {
 *       "body" : "This is an FCM notification message!",
 *       "title" : "FCM Message",
 *       }
 *    }
 * }
 *
 * 向主题发送消息
 * POST https://fcm.googleapis.com/v1/projects/myproject-b5ae1/messages:send HTTP/1.1
 *
 * Content-Type: application/json
 * Authorization: Bearer ya29.ElqKBGN2Ri_Uz...HnS_uNreA
 * {
 *   "message":{
 *     "topic" : "foo-bar",
 *     "notification" : {
 *       "body" : "This is a Firebase Cloud Messaging Topic Message!",
 *       "title" : "FCM Message"
 *       }
 *    }
 * }
 *
 * XMPP
 * 需要客户端发送信息到server的时候 启用
 * 不支持多个接收者。
 * FCM 会添加字段 message_id，此为必填字段。此 ID 可唯一标识某个 XMPP 连接中的消息。来自 FCM 的 ACK 或 NACK 使用 message_id 来标识从应用服务器发送到 FCM 的消息。因此，此 message_id 不仅要对每个发送者 ID 而言都是唯一的，还要始终存在。
 * XMPP 使用服务器密钥来向接入 FCM 的持久性连接提供授权。如需了解详情，请参阅向发送请求提供授权。
 */
public class PushNotifcation {
    private static final String PROJECT_ID = "test-fcm-1401a";
    private static final String BASE_URL = "https://fcm.googleapis.com";
    private static final String FCM_SEND_ENDPOINT = "/v1/projects/" + PROJECT_ID + "/messages:send";
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };

    private static final String TITLE = "FCM Notification";
    private static final String BODY = "Notification from FCM";
    public static final String MESSAGE_KEY = "message";

    public static String getAccessToken() throws IOException{
        Resource resource = new ClassPathResource("test-fcm-1401a-firebase-adminsdk-bea7w-daf7266744.json");
        File file = resource.getFile();
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(new FileInputStream(file))
                .createScoped(Arrays.asList(SCOPES));
        System.out.println("get refresh token "+googleCredential.refreshToken());
        System.out.println("access token is ");//ya29.c.ElrsBmem2P1T-Rz1d2qyhjifuVq1jW6VNmMW0UWZcDslUStkvegTyJ02p8eLo1RSlTuVhZg44eOMIKaw8lkY2vZ9w5LEuZUudXtDt6IMzAVM72VOJ8NEM-0GjUA
        System.out.println(googleCredential.getAccessToken());
        return googleCredential.getAccessToken();
    }

    private static HttpURLConnection getConnection() throws IOException{
        URL url = new URL(BASE_URL + FCM_SEND_ENDPOINT);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Authorization","Bearer "+getAccessToken());
        httpURLConnection.setRequestProperty("Content-Type","application/json; UTF-8");//Content-Type: application/json（JSON 格式）；application/x-www-form-urlencoded;charset=UTF-8（纯文本格式
        return httpURLConnection;
    }

    private static void sendMessage(JsonObject fcmMessage) throws IOException{
        HttpURLConnection connection = getConnection();
        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(fcmMessage.toString());
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        if(responseCode == 200){
            String response = inputstreamToString(connection.getInputStream());
            System.out.println("Message sent to Firebase for delivery, response: ");
            System.out.println(response);
        }
        else{
            System.out.println("Unable to send message to Firebase: ");
            String response = inputstreamToString(connection.getInputStream());
            System.out.println(response);
        }
    }

    private static void sendOverrideMessage() throws IOException{
        JsonObject overrideMessage = buildOverrideMessage();
        System.out.println("FCM request body for override message:");
        prettyPrint(overrideMessage);
        sendMessage(overrideMessage);
    }

    private static JsonObject buildOverrideMessage(){
        JsonObject jNotificationMessage = buildSpecificNotificationMessage();

        JsonObject messagePayload = jNotificationMessage.get(MESSAGE_KEY).getAsJsonObject();
        messagePayload.add("android", buildAndroidOverridePayload());

        JsonObject apnsPayload = new JsonObject();
        apnsPayload.add("headers", buildApnsHeadersOverridePayload());
        apnsPayload.add("payload", buildApnsOverridePayload());

        messagePayload.add("apns", apnsPayload);
        jNotificationMessage.add(MESSAGE_KEY, messagePayload);

        System.out.println("override message is ");
        System.out.println(jNotificationMessage);

        return jNotificationMessage;
    }

    private static JsonObject buildAndroidOverridePayload(){
        JsonObject androidNotification = new JsonObject();
        androidNotification.addProperty("click_action","android.intent.action.MAIN");//??

        System.out.println("android notification is "+androidNotification);

        JsonObject androidNotificationPayload = new JsonObject();
        androidNotificationPayload.add("notification", androidNotification);

        System.out.println("android payload is "+androidNotificationPayload);
        return androidNotificationPayload;
    }

    private static JsonObject buildApnsHeadersOverridePayload(){
        JsonObject apnsHeaders = new JsonObject();
        apnsHeaders.addProperty("apns-priority", "10");

        System.out.println("apns header pay load is "+apnsHeaders);
        return apnsHeaders;
    }

    private static JsonObject buildApnsOverridePayload(){
        JsonObject badgePayload = new JsonObject();
        badgePayload.addProperty("badge",1);

        JsonObject apsPayload = new JsonObject();
        apsPayload.add("aps",badgePayload);

        System.out.print("apns pay load is "+apsPayload);
        return apsPayload;
    }

    public static void sendCommonMessage() throws IOException{
        JsonObject notificationMessage = buildSpecificNotificationMessage();
        System.out.println("FCM request body for message using common notification object:");
        prettyPrint(notificationMessage);
        sendMessage(notificationMessage);
    }

    private static JsonObject buildTopicNotificationMessage(){
        JsonObject jNotification = new JsonObject();
        jNotification.addProperty("title", TITLE);
        jNotification.addProperty("body", BODY);

        JsonObject jMessage = new JsonObject();
        jMessage.add("notification", jNotification);
        jMessage.addProperty("topic", "news");

        JsonObject jFcm = new JsonObject();
        jFcm.add(MESSAGE_KEY, jMessage);

        return jFcm;
    }

    /**
     * Send message to specific user
     * @return
     */
    private static JsonObject buildSpecificNotificationMessage(){
        JsonObject jNotification = new JsonObject();
        jNotification.addProperty("title", TITLE);
        jNotification.addProperty("body", BODY);

        JsonObject jMessage = new JsonObject();
        jMessage.add("notification", jNotification);
        jMessage.addProperty("token", "");

        JsonObject jFcm = new JsonObject();
        jFcm.add(MESSAGE_KEY, jMessage);

        return jFcm;
    }

    private static String inputstreamToString(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(inputStream);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    private static void prettyPrint(JsonObject jsonObject){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(jsonObject)+"\n");
    }

    public static void main(String[] args) throws IOException {
        sendCommonMessage();
        if (args.length == 1 && args[0].equals("common-message")) {
            sendCommonMessage();
        } else if (args.length == 1 && args[0].equals("override-message")) {
            sendOverrideMessage();
        } else {
            System.err.println("Invalid command. Please use one of the following commands:");
            // To send a simple notification message that is sent to all platforms using the common
            // fields.
            System.err.println("./gradlew run -Pmessage=common-message");
            // To send a simple notification message to all platforms using the common fields as well as
            // applying platform specific overrides.
            System.err.println("./gradlew run -Pmessage=override-message");
        }
    }
}
