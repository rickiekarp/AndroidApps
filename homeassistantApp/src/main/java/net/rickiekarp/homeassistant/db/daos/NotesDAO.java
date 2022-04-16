package net.rickiekarp.homeassistant.db.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import net.rickiekarp.homeassistant.db.entities.Notes;

/**
 * Created by sebastian on 17.11.17.
 */

@Dao
public interface NotesDAO {

    @Query("SELECT * FROM Notes WHERE id = :id")
    Notes loadNotesById(int id);

    @Insert
    void insert(Notes notes);
}
