package test.twitter.onix.com.onixtwitter.callbacks;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

public interface TwitterSignInCallback {
    void signInSuccess(Result<TwitterSession> result);
    void signInFailure(TwitterException exception);
}
