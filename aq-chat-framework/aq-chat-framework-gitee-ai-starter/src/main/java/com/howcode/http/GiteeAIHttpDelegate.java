package com.howcode.http;

import com.howcode.handler.MessageHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * @Description
 * @Author ZhangWeinan
 * @Date 2024/6/30 11:25
 */
public class GiteeAIHttpDelegate {

    /**
     * 发送POST请求并流式读取响应。
     *
     * @param urlString API的URL地址
     * @param jsonBody  JSON格式的请求体
     * @param bearer    bearer
     */
    public static void sendPostAndReadStream(String urlString, String jsonBody, String bearer, MessageHandler<String> handler, StringBuilder builder) throws IOException {
        HttpURLConnection connection = buildHttpURLConnection(urlString, bearer);
        try {
            // 发送请求体
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // 检查响应码
            int status = connection.getResponseCode();
            InputStream inputStream = (status != 200) ? connection.getErrorStream() : connection.getInputStream();

            // 读取响应流
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
//                    System.out.println(line);  // 打印每行数据
                    if (builder != null) {
                        builder.append(line);
                    }
                    if (handler != null && !line.isEmpty()) {
                        String json = line.substring(line.indexOf("{"));
                        handler.handle(json);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
    }

    public static String sendPostRequestTT(String urlString, String jsonPayload, String bearer) throws IOException {
        StringBuilder builder = new StringBuilder();
        sendPostAndReadStream(urlString, jsonPayload, bearer, null, builder);
        return builder.toString();
    }


    /**
     * 发送POST请求，请求体为JSON格式，并接收字节数组响应。
     *
     * @param urlString   URL地址
     * @param jsonPayload JSON格式的请求体
     * @return 响应字节数组
     * @throws IOException 如果发生网络错误
     */
    public static byte[] sendPostRequestTB(String urlString, String jsonPayload, String bearer) throws IOException {
        HttpURLConnection connection = buildHttpURLConnection(urlString, bearer);
        try {
            // 发送请求数据
            try (OutputStream outputStream = connection.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            }

            // 获取响应数据
            try (InputStream inputStream = connection.getInputStream();
                 ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                    buffer.write(dataBuffer, 0, bytesRead);
                }
                return buffer.toByteArray();
            }
        } finally {
            connection.disconnect();
        }
    }

    //构建HttpURLConnection
    public static HttpURLConnection buildHttpURLConnection(String urlString, String bearer) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Authorization", "Bearer " + bearer);
        connection.setDoOutput(true);
        return connection;
    }
}
