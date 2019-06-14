package codexe.han.slack.client;


import codexe.han.slack.dto.SlackActionDTO;
import codexe.han.slack.dto.SlackDetailMessageDTO;
import codexe.han.slack.dto.SlackMessageDTO;
import codexe.han.slack.utils.JsonUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendSlackClient {

    private final static String slackWebookUrl = "https://hooks.slack.com/services/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";

    private static HttpURLConnection getConnection() throws IOException {
        URL url = new URL(slackWebookUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setRequestProperty("Content-type", "application/json");

        return httpURLConnection;
    }

    private static void sendMsgToSlack(SlackMessageDTO slackMessageDTO) throws IOException {

        HttpURLConnection connection = getConnection();
        connection.setDoOutput(true);

        Map<String, List<Object>> attachment = new HashMap<>();
        attachment.put("attachments", Arrays.asList(slackMessageDTO));

        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.writeBytes(JsonUtils.toJson(attachment));
        outputStream.flush();
        outputStream.close();

        int responseCode = connection.getResponseCode();
        System.out.println("send message to slack, response code is " + responseCode);

    }

    public static void main(String[] args) {
        SlackMessageDTO slackMessageDTO = SlackMessageDTO.builder()
                .pretext("*[URGENT]* NULL Search > 3")
                .color("danger")
                .messageUrl(slackWebookUrl)
                .build();
        slackMessageDTO.addSlackDetail(SlackDetailMessageDTO.builder().title("QUERY: "+"test").value(3+"\n"+"<http://www.google.com|Go to DI Dashboard>").build());
        //slackMessageDTO.addSlackDetail(SlackDetailMessageDTO.builder().title("QUERY: "+"test").value("NUM: "+3).build());
        slackMessageDTO.addSlackAction(SlackActionDTO.builder().type("button").text("button action").url("www.google.com").build());
        try {
            sendMsgToSlack(slackMessageDTO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


