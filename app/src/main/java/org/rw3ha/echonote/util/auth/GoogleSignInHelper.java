package org.rw3ha.echonote.util.auth;

import android.content.Context;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GoogleSignInHelper {
    private static final String TAG = "GoogleSignInHelper";
    private final Context context;
    private final CredentialManager credentialManager;
    private final FirebaseAuth mAuth;
    private final ExecutorService executor;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface SignInCallback {
        void onSignInSuccess();
        void onSignInFailure(String errorMessage);
    }

    public GoogleSignInHelper(Context context) {
        this.context = context;
        this.credentialManager = CredentialManager.create(context);
        this.mAuth = FirebaseAuth.getInstance();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void startGoogleSignIn(SignInCallback callback, String serverClientId) {
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption
                .Builder()
                .setServerClientId(serverClientId)
                .setFilterByAuthorizedAccounts(false)
                .build();

        GetCredentialRequest request = new GetCredentialRequest
                .Builder()
                .addCredentialOption(googleIdOption)
                .build();

        credentialManager.getCredentialAsync(
                context,
                request,
                new CancellationSignal(),
                executor,
                new CredentialManagerCallback<>() {
            @Override
            public void onResult(GetCredentialResponse result) {
                handleCredential(result.getCredential(), callback);
            }

            @Override
            public void onError(@NonNull GetCredentialException e) {
                Log.e(TAG, "GetCredential error: " + e.getLocalizedMessage());
                mainHandler.post(() -> callback.onSignInFailure("Google sign-in failed: "
                        + e.getLocalizedMessage()));
            }
        });
    }

    private void handleCredential(Credential credential, SignInCallback callback) {
        if (credential instanceof GoogleIdTokenCredential) {
            GoogleIdTokenCredential googleIdTokenCredential = (GoogleIdTokenCredential) credential;
            String idToken = googleIdTokenCredential.getIdToken();
            AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);

            mAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    mainHandler.post(callback::onSignInSuccess);
                } else {
                    Exception e = task.getException();
                    if (e instanceof FirebaseAuthUserCollisionException) {
                        mainHandler.post(() -> callback.onSignInFailure(
                                "Account exists with different sign-in method.")
                        );
                    } else {
                        mainHandler.post(() -> callback.onSignInFailure(
                                "Google sign-in failed: " + e.getMessage())
                        );
                    }
                }
            });
        } else {
            mainHandler.post(() -> callback.onSignInFailure(
                    "Unexpected credential type:")
            );
        }
    }
}
