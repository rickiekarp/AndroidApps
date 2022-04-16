package net.rickiekarp.homeassistant.net.communication.vo;

import java.util.Date;

/**
 * Created by sebastian on 17.11.17.
 */

@Deprecated
public class VONote {

    private String title;
    private int id;
    private Double price;
    private Date dateAdded;
    private Date dateBought;
    private Byte store_id;

    public VONote(String title) {
        this.title = title;
    }

    public VONote(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int noteID) {
        this.id = noteID;
    }

    public Date getDate() {
        return dateAdded;
    }

    public void setDate(Date date) {
        this.dateAdded = date;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getDateBought() {
        return dateBought;
    }

    public void setDateBought(Date dateBought) {
        this.dateBought = dateBought;
    }

    public Byte getStore_id() {
        return store_id;
    }

    public void setStore_id(Byte store_id) {
        this.store_id = store_id;
    }
}
