package net.rickiekarp.homeassistant.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by sebastian on 29.11.17.
 */
@Entity
public class User {
    @PrimaryKey
    @NonNull
    public Long id;

    public String username;
}
