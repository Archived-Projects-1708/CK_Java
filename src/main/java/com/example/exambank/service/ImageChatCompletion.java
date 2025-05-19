package com.example.exambank.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

public class ImageChatCompletion {
    private static final String API_TOKEN = "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";
    // Đã sửa endpoint: bỏ /v1 và chuyển thành inference/chat/completions
    private static final String ENDPOINT = "https://models.github.ai/inference/chat/completions";
    private static final String MODEL_NAME = "openai/gpt-4o";

    public static void runImagePrompt(String imagePath, String imageFormat) {
        try {
            String imageDataUrl = getImageDataUrl(imagePath, imageFormat);

            String jsonBody = "{\n" +
                    "  \"model\": \"" + MODEL_NAME + "\",\n" +
                    "  \"messages\": [\n" +
                    "    {\"role\": \"system\", \"content\": \"You are a helpful assistant that describes images in detail and provides answer suggestions.\"},\n" +
                    "    {\n" +
                    "      \"role\": \"user\",\n" +
                    "      \"content\": [\n" +
                    "        {\"type\": \"text\", \"text\": \"What's in this image?\"},\n" +
                    "        {\"type\": \"image_url\", \"image_url\": {\"url\": \"" + imageDataUrl + "\"}}\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    // .callTimeout(120, TimeUnit.SECONDS) // nếu muốn giới hạn tổng thời gian
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, jsonBody);

            Request request = new Request.Builder()
                    .url(ENDPOINT)
                    .addHeader("Authorization", "Bearer " + API_TOKEN)
                    .addHeader("Accept", "application/vnd.github+json")
                    .addHeader("X-GitHub-Api-Version", "2022-11-28")
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.out.println("Lỗi API: " + response.code());
                    System.out.println(response.body().string());
                } else {
                    System.out.println("Phản hồi từ AI:");
                    System.out.println(response.body().string());
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi xử lý ảnh hoặc gọi API: " + e.getMessage());
        }
    }

    private static String getImageDataUrl(String imagePath, String format) throws IOException {
        File file = new File(imagePath);
        byte[] fileContent = Files.readAllBytes(file.toPath());
        String base64Image = Base64.getEncoder().encodeToString(fileContent);
        return "data:image/" + format + ";base64," + base64Image;
    }
}
