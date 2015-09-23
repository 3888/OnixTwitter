package test.twitter.onix.com.onixtwitter.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetui.TweetUi;

import java.util.Locale;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;
import test.twitter.onix.com.onixtwitter.Constants;
import test.twitter.onix.com.onixtwitter.PreferencesHelper;
import test.twitter.onix.com.onixtwitter.R;

public class SignInActivity extends AppCompatActivity {

    private PreferencesHelper mSPHelper;
    private TwitterLoginButton mLoginButton;

    private static final String TAG = SignInActivity.class.getSimpleName();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        mLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // Change locale settings in the app.
        Resources res = getApplicationContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale("en");
        res.updateConfiguration(conf, dm);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(Constants.TWITTER_KEY, Constants.TWITTER_SECRET);
        setUpFabricKits(authConfig);

        setContentView(R.layout.activity_signin);

        mSPHelper = new PreferencesHelper(getApplicationContext());

        if (mSPHelper.getBoolean("IS_LOGGED", false)) {
            finish();
            Intent baseIntent = new Intent(getApplicationContext(), BaseActivity.class);
            baseIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(baseIntent);
        }

        mLoginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                //Twitter session is not used as user is automatically logged in
                Log.d(TAG, "Sign in success");
                mSPHelper.setBoolean("IS_LOGGED", true);
                mSPHelper.setString("NICK_NAME", result.data.getUserName());
                finish();
                Intent baseIntent = new Intent(getApplicationContext(), BaseActivity.class);
                baseIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(baseIntent);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e(TAG, "Sign in failure", exception);
            }
        });
    }

    private void setUpFabricKits(TwitterAuthConfig authConfig) {
        Fabric.with(this, new Kit[]{
                new TwitterCore(authConfig), new TweetUi(), new Twitter(authConfig)
        });
    }
}