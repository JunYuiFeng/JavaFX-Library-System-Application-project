package com.example.javaendassignment2022;

import java.io.Serializable;
import java.time.LocalDate;

public class Item implements Serializable {
    private int itemCode;
    private boolean availability;
    private String title;
    private String author;
    private Member member;
    private LocalDate lendDate;

    public Item(int itemCode, boolean availability, String title, String author, Member member, LocalDate lendDate) {
        this.itemCode = itemCode;
        this.availability = availability;
        this.title = title;
        this.author = author;
        this.member = member;
        this.lendDate = lendDate;
    }

    public int getItemCode() {
        return itemCode;
    }

    public void setItemCode(int itemCode) {
        this.itemCode = itemCode;
    }

    public String getAvailability() {
        if (availability) {
            return "Yes";
        }
        else {
            return "No";
        }
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getLendDate() {
        return lendDate;
    }

    public void setLendDate(LocalDate lendDate) {
        this.lendDate = lendDate;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
