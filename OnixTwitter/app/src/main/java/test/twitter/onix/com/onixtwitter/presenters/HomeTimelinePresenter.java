package test.twitter.onix.com.onixtwitter.presenters;

import android.content.Context;

public interface HomeTimelinePresenter {

    String TWITTER_SESSION_USER_ID = "TWITTER_SESSION_USER_ID";
    String TWITTER_SESSION_USER_NAME = "TWITTER_SESSION_USER_NAME";

    void createTimeline(Context context);
    void updateTimeline(Context context);
}