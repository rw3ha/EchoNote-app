package org.rw3ha.echonote.ui.auth;


import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.core.splashscreen.SplashScreen;

import org.rw3ha.echonote.R;
import org.rw3ha.echonote.ui.auth.base.BaseActivity;

public class LoginActivity extends BaseActivity {

    boolean isReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        View mainContent = findViewById(R.id.main);
       setupWindowInsets(mainContent);

       //Lay out the content
        mainContent.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (!isReady) {
                    return false;
                }

                //Show screen if content is ready
                mainContent.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });

        // Keep splash on until screen ready
        splashScreen.setKeepOnScreenCondition(() -> !isReady);

        // Set shared element transition for app logo
        setupSharedElementTransition(R.id.ic_logo_image);

        // Set navigation for forgot password
        setNavigationWithSharedElement(R.id.forgot_password_textView, PasswordResetActivity.class, R.id.ic_logo_image);

        // Set navigation for sign up
        setNavigationWithSharedElement(R.id.account_sign_text, RegisterActivity.class, R.id.ic_logo_image);

        // Set the isReady variable to true to show the login activity
        isReady = true;

    }
}