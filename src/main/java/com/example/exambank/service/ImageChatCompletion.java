package com.example.exambank.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ImageChatCompletion {
    private static final String API_TOKEN = "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";
    private static final String ENDPOINT = "https://models.github.ai/inference/chat/completions";
    private static final String MODEL_NAME = "openai/gpt-4o";

   //  Đặt systemPrompt
    private static final String SYSTEM_PROMPT = """
        You are an advanced OCR system specialized in extracting Japanese text from images of academic exam sheets. You MUST follow these constraints strictly:

        1. Language: The image contains Japanese text only. Extract only legible Japanese questions. Do not infer or hallucinate unreadable parts. Skip unclear content completely.

        2. Content Rules:
        - Only extract actual question content.
        - Exclude any examples (marked with 「例」 or 「れい」).
        - Exclude instructions, footnotes, titles, or visual noise.
        - Remove extra characters like "1)", "2.", "5 mou4" at the beginning of questions.

        3. Output format:
        - Return a valid JSON array, where each item is a list of strings.
        - Each list represents a logical question block.
        - There must be no keys like "title" or "questions" — only pure lists of strings.
        - For example:
          [
            [
              "どうしたんですか。（ボタンを押しました。ジュースが出ません）",
              "体の調子はどうですか。（ちゃんと薬を飲んでいますよ。よくなりません）"
            ],
            [
              "3時の新幹線に間に合いましたか。(走って行きました)"
            ],
            []
          ]
        - DO NOT include markdown formatting or any explanation outside of the raw JSON.
        - Ensure the output is directly parsable using json.loads() in Python without cleaning.
        - No output contains only a line of dots.

        4. Formatting & Layout:
        - Convert any multi-column layout into a single-column format.
        - Auto-detect and correct vertical or horizontal orientation.

        5. Question Types:
        - For fill-in-the-blank questions, use: "..............".
        - For written answers, place a line of dots below the question to indicate space: "..............".

        6. Accuracy:
        - Do NOT interpret, translate, or complete Japanese text.
        - Use high-precision OCR preprocessing (binarization, layout detection, deskewing).
        - Do NOT guess content. Only return what is visually legible.

        << Begin OCR >>
        """;

    public static void runImagePrompt(String imagePath, String imageFormat) {
        try {
            String imageDataUrl = getImageDataUrl(imagePath, imageFormat);

            // 2. Build JSON payload with Gson
            JsonObject systemMsg = new JsonObject();
            systemMsg.addProperty("role", "system");
            systemMsg.addProperty("content", SYSTEM_PROMPT);

            JsonObject userMsg = new JsonObject();
            userMsg.addProperty("role", "user");
            JsonArray contentArr = new JsonArray();
            JsonObject imgObj = new JsonObject();
            imgObj.addProperty("type", "image_url");
            JsonObject urlObj = new JsonObject();
            urlObj.addProperty("url", imageDataUrl);
            imgObj.add("image_url", urlObj);
            contentArr.add(imgObj);
            userMsg.add("content", contentArr);

            JsonObject payload = new JsonObject();
            payload.addProperty("model", MODEL_NAME);
            JsonArray msgs = new JsonArray();
            msgs.add(systemMsg);
            msgs.add(userMsg);
            payload.add("messages", msgs);

            String jsonBody = new Gson().toJson(payload);

            // 3. Client với timeout dài
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
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
                String resp = response.body().string();
                if (!response.isSuccessful()) {
                    System.out.println("Lỗi API: " + response.code());
                    System.out.println(resp);
                } else {
                    // In thẳng JSON trả về
                    System.out.println(resp);
                }
            }
        } catch (IOException e) {
            System.err.println("Lỗi khi xử lý ảnh hoặc gọi API: " + e.getMessage());
        }
    }

    private static String getImageDataUrl(String imagePath, String format) throws IOException {
        File file = new File(imagePath);
        byte[] bytes = Files.readAllBytes(file.toPath());
        String b64 = Base64.getEncoder().encodeToString(bytes);
        return "data:image/" + format + ";base64," + b64;
    }
}
