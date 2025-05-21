package com.example.exambank.service;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import com.google.gson.JsonParser;
import okhttp3.*;

import com.google.gson.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.Conversions.trim;

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
              **only** a single JSON array in this exact form:
           
                 [
                   {
                     "question": "<full Japanese sentence with （　　　） or ____ >",
                     "options": ["A: ...", "B: ...", "C: ...", "D: ..."],
                     "answer": "A"
                   },
                   {
                     "question": "<kanji fill-in with ____>",
                     "answer": "<the exact kanji string>"
                   }
                 ]
           
                 Rules:
                 – No markdown (```), no explanation, no extra keys. \s
                 – No wrapping quotes around the array. \s
                 – Use plain ASCII quotes ("). \s
                 – If illegible, skip the item. \s
           
           5. Behavior:
              - If options <4 or underlined kanji missing, skip that item. \s
              - Do not include explanations or commentary—only raw JSON.
           
           << Begin OCR & Answer Extraction >>
          
        """;
    public static String processImageArray(String imagePath, String format) throws IOException {
        // 1) Lấy raw response
        String raw = runImagePrompt(getImageDataUrl(imagePath, format));
        // 2) Parse wrapper
        JsonObject root = JsonParser.parseString(raw).getAsJsonObject();

        // 3) Extract the content field
        String content = root
                .getAsJsonArray("choices")
                .get(0).getAsJsonObject()
                .getAsJsonObject("message")
                .get("content").getAsString()
                .trim();
        JsonElement parsed = JsonParser.parseString(content);
        if (!parsed.isJsonArray()) {
            throw new IOException("Expected JSON array in content, got: " + parsed);
        }
        // Trả mảng ở dạng chuỗi
        return parsed.getAsJsonArray().toString();
    }

    public static String runImagePrompt(String dataUrl) throws IOException {
        // 1. System + User messages
        JsonObject systemMsg = new JsonObject();
        systemMsg.addProperty("role", "system");
        systemMsg.addProperty("content", SYSTEM_PROMPT);

        JsonObject userMsg = new JsonObject();
        userMsg.addProperty("role", "user");
        JsonArray contentArr = new JsonArray();
        JsonObject imgObj = new JsonObject();
        imgObj.addProperty("type", "image_url");
        JsonObject urlObj = new JsonObject();
        urlObj.addProperty("url", dataUrl);
        imgObj.add("image_url", urlObj);
        contentArr.add(imgObj);
        userMsg.add("content", contentArr);

        // 2. Payload chung
        JsonObject payload = new JsonObject();
        payload.addProperty("model", MODEL_NAME);
        JsonArray msgs = new JsonArray();
        msgs.add(systemMsg);
        msgs.add(userMsg);
        payload.add("messages", msgs);

        // 3. Gửi HTTP
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build();

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                new Gson().toJson(payload)
        );
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
                throw new IOException("API error " + response.code() + ": " + response.body().string());
            }
            return response.body().string();
        }
        catch (SocketTimeoutException e) {
            throw new IOException("Yêu cầu hết thời gian chờ (timeout). Thử tăng thời gian read/write timeout.", e);
        }

    }


    private static String getImageDataUrl(String imagePath, String format) throws IOException {
        File file = new File(imagePath);
        byte[] bytes = Files.readAllBytes(file.toPath());
        String b64 = Base64.getEncoder().encodeToString(bytes);
        return "data:image/" + format + ";base64," + b64;
    }
    public static String processImage(String imagePath, String format) throws IOException {
        try {
            return runImagePrompt(getImageDataUrl(imagePath, format));
        } catch (IOException e) {
            throw new IOException("Failed to process image: " + e.getMessage(),e);
        }
    }

}
