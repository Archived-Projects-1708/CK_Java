package com.example.exambank.service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.io.File;

public class OCRService {
    public String extractTextFromImage(File imageFile) throws TesseractException {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath("src/main/resources/tessdata");
        tesseract.setLanguage("jpn"); // Tiếng Nhật
        return tesseract.doOCR(imageFile);
    }
}