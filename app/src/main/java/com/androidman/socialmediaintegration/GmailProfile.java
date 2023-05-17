//zIq1yipUtlNIrtJw1lqVkHsM0sg=
//SHA1: CC:8A:B5:CA:2A:54:B6:53:48:AE:D2:70:D6:5A:95:90:7B:0C:D2:C8
//MD5: 9E:B4:DF:C5:40:6E:8D:50:77:6C:88:16:06:6E:06:E7
//SHA-256: 4C:A8:34:7A:07:E0:90:8E:E5:D6:32:53:4D:BE:75:00:F0:1B:95:4A:B7:EF:9C:C1:18:14:0F:9D:C5:94:97:B9
//Client ID : 759663141717-lgae6677j7tt6dql9t5a8dldl6g40274.apps.googleusercontent.com
//Client Secret : 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
package com.androidman.socialmediaintegration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.hdodenhof.circleimageview.CircleImageView;

public class GmailProfile extends AppCompatActivity {

    TextView email, name;
    CircleImageView imageView;
    SignInButton signInButton;
    Button signOutButton;

    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail_profile);

        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        imageView = findViewById(R.id.imageView);

        signInButton = findViewById(R.id.sign_in_button);

        signOutButton = findViewById(R.id.sign_out_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        signInButton.setOnClickListener(view -> signIn());

        signOutButton.setOnClickListener(view -> signOut());
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    name.setText(" ");
                    email.setText(" ");
                    imageView.setImageResource(0);
                });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personEmail = acct.getEmail();
                Uri personPhoto = acct.getPhotoUrl();

                name.setText(personName);
                email.setText(personEmail);
                Glide.with(this).load(String.valueOf(personPhoto)).into(imageView);
            }

        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}