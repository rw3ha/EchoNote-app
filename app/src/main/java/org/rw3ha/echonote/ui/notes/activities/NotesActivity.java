package org.rw3ha.echonote.ui.notes.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import org.rw3ha.echonote.R;
import org.rw3ha.echonote.adapter.NoteAdapter;
import org.rw3ha.echonote.data.local.model.Note;
import org.rw3ha.echonote.viewmodel.NoteViewModel;

public class NotesActivity extends AppCompatActivity {

    private TabLayout categoryTabs;
    private NoteViewModel noteViewModel;
    private NoteAdapter adapter;
    private TextView emptyPlaceholder;
    private TextInputEditText searchBar;

    private String currentCategory = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notes);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        emptyPlaceholder = findViewById(R.id.empty_placeholder);
        searchBar = findViewById(R.id.search_bar_editText);

        setupRecyclerView();
        setupCategoryTabs();
        setupSearchBar();
        loadAllNotes();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.note_recyclerView);
        adapter = new NoteAdapter(new NoteAdapter.OnNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                //TODO: Open Note details or note edit screen
            }

            @Override
            public void onNoteLongClick(Note note) {
                //TODO: Open Note options menu
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
        ) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Note noteToDelete = adapter.getNoteAt(position);
                noteViewModel.delete(noteToDelete);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setupCategoryTabs() {
        categoryTabs = findViewById(R.id.category_tabs);

        String[] defaultCategories = {"All", "Important", "Bookmarked", "Favorite"};
        for (String category : defaultCategories) {
            categoryTabs.addTab(categoryTabs.newTab().setText(category));
        }

        List<String> customCategories = getUserCustomCategories();
        for (String customCategory : customCategories) {
            categoryTabs.addTab(categoryTabs.newTab().setText(customCategory));
        }

        categoryTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentCategory = tab.getText().toString();
                applyFilters();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                applyFilters();
            }
        });
    }

    private void setupSearchBar() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void applyFilters() {
        String query =  searchBar.getText() != null ? searchBar.getText().toString() : "";
        if (currentCategory.equals("All")) {
            noteViewModel.searchNotesByTitle(query).observe(this, notes -> updateNotesList(notes));
        } else {
            noteViewModel.searchNotesByCategory(currentCategory).observe(
                    this,
                    notes -> {
                        updateNotesList(notes);
                    }
            );
        }
    }

    private void updateNotesList(List<Note> notes) {
        adapter.setNotes(notes);
        if (notes.isEmpty()) {
            emptyPlaceholder.setVisibility(View.VISIBLE);
        } else {
            emptyPlaceholder.setVisibility(View.GONE);
        }
    }

    private void loadAllNotes() {
        noteViewModel.getAllNotes().observe(this, notes -> updateNotesList(notes));
    }
    private List<String> getUserCustomCategories() {
        List<String> customCategories = new ArrayList<>();
        customCategories.add("Work");
        customCategories.add("Personal");
        customCategories.add("Home");
        customCategories.add("Project");

        return customCategories;
    }
}