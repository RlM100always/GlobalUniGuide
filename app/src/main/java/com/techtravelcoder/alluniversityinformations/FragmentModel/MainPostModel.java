package com.techtravelcoder.alluniversityinformations.FragmentModel;


public class MainPostModel {
    String title,postId,image,key,label,uniqueNum,category,date;
    Long views,ratingNum,postLoves;
    Double avgRating;

    public MainPostModel(){

    }

    public Long getRatingNum() {
        return ratingNum;
    }

    public void setRatingNum(Long ratingNum) {
        this.ratingNum = ratingNum;
    }

    public Long getPostLoves() {
        return postLoves;
    }

    public void setPostLoves(Long postLoves) {
        this.postLoves = postLoves;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUniqueNum() {
        return uniqueNum;
    }

    public void setUniqueNum(String uniqueNum) {
        this.uniqueNum = uniqueNum;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    @Override
    public String toString() {
        return "MainPostModel{" +
                "title='" + title.trim() + '\'' +
                ", category='" + category.trim() + '\'' +
                ", label"+ label.trim()+ '\''+
                '}';
    }
}
