package com.example.exambank.service;

import com.example.exambank.dao.CategoryDao;
import com.example.exambank.dao.LevelDao;
import com.example.exambank.model.Category;
import com.example.exambank.model.Level;


import java.util.List;

public class cate_levelService {
    private final CategoryDao categoryDao;
    private final LevelDao levelDao;

    public cate_levelService() {
        this.categoryDao = new CategoryDao();
        this.levelDao = new LevelDao();
    }

    public List<Category> getAllCategories() {
        return categoryDao.findAll();
    }

    public List<Level> getAllLevels() {
        return levelDao.findAll();
    }
}
