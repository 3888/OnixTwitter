package test.twitter.onix.com.onixtwitter.presenters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import test.twitter.onix.com.onixtwitter.views.TimelineView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.util.List;

public class HomeTimelinePresenterImpl extends BasePresenter implements HomeTimelinePresenter {

    private static final String TAG = HomeTimelinePresenterImpl.class.getSimpleName();

    private TimelineView timelineView;

    public static HomeTimelinePresenterImpl newInstance(@NonNull TimelineView timelineView) {
        return new HomeTimelinePresenterImpl(timelineView);
    }

    HomeTimelinePresenterImpl(@NonNull TimelineView timelineView) {
        Log.d(TAG, "UserTimelinePresenterImpl(@NonNull TimelineView timelineView)");
        this.timelineView = timelineView;
    }

    @Override
    public void createTimeline(final Context context) {
        Log.d(TAG, "createTimeline(Context mContext)");

        StatusesService statusesService = getStatusesService();
        final int count = 10;
        statusesService.homeTimeline(count, null, null, null, null, null, null, getTwitterCallback(context, false));
    }

    @Override
    public void updateTimeline(final Context context) {
        Log.d(TAG, "createTimeline(Context mContext)");

        StatusesService statusesService = getStatusesService();
        final int count = 10;
        statusesService.homeTimeline(count, null, null, null, null, null, null, getTwitterCallback(context, true));
    }

    @NonNull
    private TwitterCallback getTwitterCallback(@NonNull Context context, boolean isUpdate) {
        return new TwitterCallback(context, isUpdate, timelineView);
    }

    private static final class TwitterCallback extends Callback<List<Tweet>> {

        private final String TAG = TwitterCallback.class.getSimpleName();

        private Context context;
        private boolean isUpdate;
        private TimelineView timelineView;

        TwitterCallback(Context context, boolean isUpdate, TimelineView timelineView) {
            this.context = context;
            this.isUpdate = isUpdate;
            this.timelineView = timelineView;
        }

        @Override
        public void success(Result<List<Tweet>> result) {
            Log.d(TAG, "success(Result<List<Tweet>> result)");

            List<Tweet> tweets = result.data;

            if (tweets == null) {
                Log.d(TAG, "Tweets is null");
            }

            FixedTweetTimeline fixedTweetTimeline = new FixedTweetTimeline.Builder()
                    .setTweets(tweets)
                    .build();
            TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(context, fixedTweetTimeline);

            if (isUpdate) {
                Log.d(TAG, "update user tweet list");
                timelineView.updateUserTweetList(adapter);
            } else {
                Log.d(TAG, "create user tweet list");
                timelineView.displayUserTweetList(adapter);
            }

            resetMembers();
        }

        @Override
        public void failure(TwitterException exception) {
            Log.e(TAG, "failure(TwitterException exception)", exception);
            timelineView.displayError(exception.getMessage());
            resetMembers();
        }

        private void resetMembers() {
            context = null;
            timelineView = null;
        }
    }
}