package com.techtravelcoder.alluniversityinformations.FragmentModel;

public class CategoryModel {
    String name,label,key,imageLink,id;
    Boolean categoryType;

    public CategoryModel(){

    }

    public Boolean getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Boolean categoryType) {
        this.categoryType = categoryType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public String toString() {
        return "MainPostModel{" +
                "name='" + name.trim() + '\'' +
                ", label='" + label.trim() + '\'' +
                '}';
    }
}
