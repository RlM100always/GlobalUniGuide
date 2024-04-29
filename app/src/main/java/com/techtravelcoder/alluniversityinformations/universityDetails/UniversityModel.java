package com.techtravelcoder.alluniversityinformations.universityDetails;

public class UniversityModel {
    String uniName,best,contryName,key,privates,publics,suggested,uniImageLink,uniWebLink,date;
    Long ratingNum,postLoves;
    Double avgRating;


    public UniversityModel(){

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getUniName() {
        return uniName;
    }

    public void setUniName(String uniName) {
        this.uniName = uniName;
    }

    public String getBest() {
        return best;
    }

    public void setBest(String best) {
        this.best = best;
    }

    public String getContryName() {
        return contryName;
    }

    public void setContryName(String contryName) {
        this.contryName = contryName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPrivates() {
        return privates;
    }

    public void setPrivates(String privates) {
        this.privates = privates;
    }

    public String getPublics() {
        return publics;
    }

    public void setPublics(String publics) {
        this.publics = publics;
    }

    public String getSuggested() {
        return suggested;
    }

    public void setSuggested(String suggested) {
        this.suggested = suggested;
    }

    public String getUniImageLink() {
        return uniImageLink;
    }

    public void setUniImageLink(String uniImageLink) {
        this.uniImageLink = uniImageLink;
    }

    public String getUniWebLink() {
        return uniWebLink;
    }

    public void setUniWebLink(String uniWebLink) {
        this.uniWebLink = uniWebLink;
    }

    @Override
    public String toString() {
        return "UniversityModel{" +
                "uniName='" + uniName.trim() + '\'' +
                ", contryName='" + contryName.trim() + '\'' +
                '}';
    }

}
