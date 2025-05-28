package org.rw3ha.echonote.data.local.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Note {

    @PrimaryKey
    public int id;

    @NonNull
    public String title;

    @NonNull
    public String content;

    @NonNull
    public String category;

    public long timestamp;
}
