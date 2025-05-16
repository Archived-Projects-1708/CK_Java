package com.example.exambank.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Question {
    private int questionId;
    private String content;
    private int typeId;
    private int levelId;
    private String audioPath;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public Question() {}

    public Question(int questionId,
                    String content,
                    int typeId,
                    int levelId,
                    String audioPath,
                    LocalDateTime createdDate,
                    LocalDateTime updatedDate) {
        this.questionId = questionId;
        this.content = content;
        this.typeId = typeId;
        this.levelId = levelId;
        this.audioPath = audioPath;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Question)) return false;
        Question that = (Question) o;
        return questionId == that.questionId &&
                typeId == that.typeId &&
                levelId == that.levelId &&
                Objects.equals(content, that.content) &&
                Objects.equals(audioPath, that.audioPath) &&
                Objects.equals(createdDate, that.createdDate) &&
                Objects.equals(updatedDate, that.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(questionId, content, typeId, levelId, audioPath, createdDate, updatedDate);
    }

    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", content='" + content + '\'' +
                ", typeId=" + typeId +
                ", levelId=" + levelId +
                ", audioPath='" + audioPath + '\'' +
                ", createdDate=" + createdDate +
                ", updatedDate=" + updatedDate +
                '}';
    }
}
