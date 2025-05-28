package org.rw3ha.echonote.ui.notes.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import org.rw3ha.echonote.R;

public class NotesActivity extends AppCompatActivity {

    private TabLayout categoryTabs;

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

        setupCategoryTabs();
    }

    private void setupCategoryTabs() {
        categoryTabs = findViewById(R.id.category_tabs);

        //TODO: Mock data. Perform setup once db is done
        String[] defaultCategories = {"All", "Important", "Bookmarked", "Favorite"};
        int[] noteCounts = {20, 5, 8, 12};

        for(int i = 0; i < defaultCategories.length; i++) {
            String label = defaultCategories[i] + " (" + noteCounts[i] + ")";
            categoryTabs.addTab(categoryTabs.newTab().setText(label));
        }

        // Load date from custom categories
        List<String> customCategories = getUserCustomCategories();

        for(String customCategory : customCategories) {
            categoryTabs.addTab(categoryTabs.newTab().setText(customCategory));
        }

        //Listener to handle tab for Tab selection actions
        categoryTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String selectedCategory = tab.getText().toString();
                //TODO: Filter notes based on selected category
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                String unselectedCategory = tab.getText().toString();
                //TODO: Unfilter notes based on unselected category

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                String reselectedCategory = tab.getText().toString();
                //TODO: Refilter notes based on reselected category

            }
        });
    }

    //TODO: Mock method. Replace later with real data after db setup
    private List<String> getUserCustomCategories() {
        List<String> customCategories = new ArrayList<>();
        customCategories.add("Work");
        customCategories.add("Personal");
        customCategories.add("Home");
        customCategories.add("Project");

        return customCategories;
    }
}