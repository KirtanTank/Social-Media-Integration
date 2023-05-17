//zIq1yipUtlNIrtJw1lqVkHsM0sg=
//SHA1: CC:8A:B5:CA:2A:54:B6:53:48:AE:D2:70:D6:5A:95:90:7B:0C:D2:C8
//MD5: 9E:B4:DF:C5:40:6E:8D:50:77:6C:88:16:06:6E:06:E7
//SHA-256: 4C:A8:34:7A:07:E0:90:8E:E5:D6:32:53:4D:BE:75:00:F0:1B:95:4A:B7:EF:9C:C1:18:14:0F:9D:C5:94:97:B9

package com.androidman.socialmediaintegration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class FacebookProfile extends AppCompatActivity {

    TextView email, name, total_friends, dob, gender;
    ProfilePictureView image;
    LoginButton loginButton;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_profile);

        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        total_friends = findViewById(R.id.friends);
        dob = findViewById(R.id.dob);
        gender = findViewById(R.id.gender);

        image = findViewById(R.id.image);

        loginButton = findViewById(R.id.login_button);

        callbackManager = CallbackManager.Factory.create();

        final String EMAIL = "email";
        loginButton.setPermissions(Arrays.asList(EMAIL));
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(@NonNull FacebookException exception) {
                // App code
            }
        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken accessToken, AccessToken accessToken1) {
            if (accessToken1 == null) {
                name.setText("");
                email.setText("");
                gender.setText("");
                dob.setText("");
                total_friends.setText("");

                Toast.makeText(FacebookProfile.this, "User Logged out!", Toast.LENGTH_LONG).show();
            } else {
                loadUserProfile(accessToken1);
            }
        }
    };

    private void loadUserProfile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                if (jsonObject != null) {
                    try {
                        String username = jsonObject.getString("name");
                        String emailid = jsonObject.getString("email");
                        String date_of_birth = jsonObject.getString("birthday");
                        String usergender = jsonObject.getString("gender");
                        String allfriends = jsonObject.getJSONObject("friends").getJSONObject("summary").getString("total_count");
                        String id = jsonObject.getString("id");
                        String imageUrl = "https://graph.facebook.com/" + id + "/picture?type=normal";

                        name.setText(username);
                        email.setText(emailid);
                        dob.setText(date_of_birth);
                        gender.setText(usergender);
                        total_friends.setText(allfriends);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        Bundle parameter = new Bundle();
        parameter.putString("fields", "id, name, email, friends, gender, birthday");
        request.setParameters(parameter);
        request.executeAsync();
    }
}