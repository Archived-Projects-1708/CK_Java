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
           You are an advanced OCR + Q&A assistant specialized in extracting Japanese multiple-choice questions and underlined-kanji fill-in items from images, then providing the correct answer choices. STRICTLY follow these rules:
           
           1. Input:
              - A single image of a printed or handwritten Japanese exam sheet. \s
              - May contain:
                - Multiple-choice questions (sentence with a blank “（　　　）” plus four labeled options A–D). \s
                - Kanji fill-in questions where a word or phrase is underlined instead of parenthesized.
           
           2. OCR Extraction:
              - Detect each question text precisely, including:
                - The full Japanese sentence. \s
                - Blank spans “（　　　）” or **underlined kanji segments**—replace each underlined segment with `____`. \s
              - Do NOT hallucinate; skip illegible parts. \s
              - Remove headers, footers, numbering, instructions—extract only question and its options or underlined segment.
           
           3. Question Structure:
              - For each multiple-choice question, return an object:
                - `"question"`: full text with `（　　　）`. \s
                - `"options"`: array of four strings `["A: …", "B: …", "C: …", "D: …"]`. \s
                - `"answer"`: single letter of correct choice. \s
              - For each underlined-kanji question, return an object:
                - `"question"`: full text with `____` in place of underlined kanji. \s
                - `"answer"`: the exact underlined kanji string that fills the blank.
           
           4. Output Format:
              - Return a **JSON array** mixing both types, for example:
                ```json
                [
                  {
                    "question": "これはペン（　　　）です。",
                    "options": ["A: が", "B: を", "C: に", "D: と"],
                    "answer": "A"
                  },
                  {
                    "question": "この漢字を____書いてください。",
                    "answer": "勉強"
                  }
                ]
                ```
              - No markdown, no extra keys beyond those specified. \s
              - Do NOT wrap in code fences; output parsable via `json.loads()`.
           
           5. Behavior:
              - If options <4 or underlined kanji missing, skip that item. \s
              - Do not include explanations or commentary—only raw JSON.
           
           << Begin OCR & Answer Extraction >>
          
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
