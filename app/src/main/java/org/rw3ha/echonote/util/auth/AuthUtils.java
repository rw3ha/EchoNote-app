package org.rw3ha.echonote.util.auth;

import android.content.Context;
import android.widget.Toast;

public class AuthUtils {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static boolean isEmpty(String input) {
        return input == null || input.trim().isEmpty();
    }
}
