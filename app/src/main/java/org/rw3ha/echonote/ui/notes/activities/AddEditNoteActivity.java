package org.rw3ha.echonote.ui.notes.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;

import org.rw3ha.echonote.R;
import org.rw3ha.echonote.ui.base.BaseActivity;

public class AddEditNoteActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_note);
        View mainContent = findViewById(android.R.id.content);
        setupWindowInsets(mainContent);
    }
}