package org.rw3ha.echonote.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.rw3ha.echonote.R;
import org.rw3ha.echonote.ui.auth.base.BaseActivity;

public class PasswordResetActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        // Set shared element transition for the app icon
        setupSharedElementTransition(R.id.ic_logo_image);

        View mainContent = findViewById(android.R.id.content);
        setupWindowInsets(mainContent);

       // Close and return to Login Page
        ImageView closeButton = findViewById(R.id.ic_reset_close);
        closeButton.setOnClickListener(v -> supportFinishAfterTransition());
    }
}