package test.twitter.onix.com.onixtwitter.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.SafeListAdapter;
import com.twitter.sdk.android.core.models.SafeMapAdapter;
import com.twitter.sdk.android.core.models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Dummy activity that makes an api call in onCreate
 */
public class CustomServiceActivity extends Activity {
    private static final String TAG = "CustomServiceActivity";

    /**
     * See more about custom api adapters here under "Extensions"
     * https://dev.twitter.com/twitter-kit/android/api
     */
    class MyTwitterApiClient extends TwitterApiClient {
        public MyTwitterApiClient(TwitterSession session) {
            super(session);
        }

        /**
         * Provide CustomService with defined endpoints
         */
        public CustomService getCustomService() {
            return getService(CustomService.class);
        }
    }

    /**
     * Example users/show service endpoint
     * Note that we use the Response type as our callback parameter. This is because if we used
     * User the gson converter under the hood is going to be reading the input stream and closing
     * it. With this service we'll be able to get the input stream in an unconsumed state.
     */

    interface CustomService {
        @GET("/1.1/users/show.json")
        void show(@Query("screen_name") String screenName, Callback<Response> cb);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This would not be advised to create on the main thread in a real app
        final Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new SafeListAdapter())
                .registerTypeAdapterFactory(new SafeMapAdapter())
                .create();

        // This would not be advised to create on the main thread in a real app
        final MyTwitterApiClient apiClient = new MyTwitterApiClient(
                TwitterCore.getInstance().getSessionManager().getActiveSession());

        apiClient.getCustomService().show("camilarwt", new Callback<Response>() {
            /**
             * In this method we get both the json string from the body and parse it into our
             * TwitterCore model class.
             * @param result the unparsed result with a Response field.
             */
            @Override
            public void success(Result<Response> result) {
                BufferedReader reader = null;
                final StringBuilder sb = new StringBuilder();
                try {
                    try {
                        reader = new BufferedReader(
                                new InputStreamReader(result.data.getBody().in()));
                        String line;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line);
                        }

                        final String userJson = sb.toString();

                        Log.d(TAG, userJson);

                        final User user = gson.fromJson(userJson, User.class);
                        Log.d(TAG, user.screenName);
                    } finally {
                        reader.close();
                    }
                } catch (IOException e) {
                    Log.d(TAG, "failed to open and read stream");
                }
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d(TAG, "api error", exception);
            }
        });
    }
}