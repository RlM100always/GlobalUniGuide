package com.techtravelcoder.alluniversityinformations.books;

public class Bookmark {
     Boolean userBookmark;
     Long time;

    public Bookmark() {
        // Default constructor required for calls to DataSnapshot.getValue(Bookmark.class)
    }
    public Boolean getUserBookmark() {
        return userBookmark;
    }

    public void setUserBookmark(Boolean userBookmark) {
        this.userBookmark = userBookmark;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
