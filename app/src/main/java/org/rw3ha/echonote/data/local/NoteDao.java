package org.rw3ha.echonote.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import org.rw3ha.echonote.data.local.model.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    long insertNote(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    List<Note> getAllNotes();

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :search || '%' ORDER BY timestamp DESC")
    List<Note> searchNotesByTitle(String search);

    @Query("SELECT * FROM notes WHERE category = :category ORDER BY timestamp DESC")
    List<Note> searchNotesByCategory(String category);
}
