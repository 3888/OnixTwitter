package test.twitter.onix.com.onixtwitter.presenters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import test.twitter.onix.com.onixtwitter.Constants;
import test.twitter.onix.com.onixtwitter.views.TimelineView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeTimelinePresenterImpl extends BasePresenter implements HomeTimelinePresenter {

    private static final String TAG = HomeTimelinePresenterImpl.class.getSimpleName();

    private TimelineView mTimelineView;

    public static HomeTimelinePresenterImpl newInstance(@NonNull TimelineView timelineView) {
        return new HomeTimelinePresenterImpl(timelineView);
    }

    HomeTimelinePresenterImpl(@NonNull TimelineView timelineView) {
        Log.d(TAG, "UserTimelinePresenterImpl(@NonNull TimelineView mTimelineView)");
        this.mTimelineView = timelineView;
    }

    @Override
    public void createTimeline(final Context context) {
        Log.d(TAG, "createTimeline(Context mContext)");

        StatusesService statusesService = getStatusesService();

        statusesService.homeTimeline(Constants.TWEETS_COUNT, null, null, null, null, null, null,
                getTwitterCallback(context, false));
    }

    @Override
    public void updateTimeline(final Context context) {
        Log.d(TAG, "createTimeline(Context mContext)");

        StatusesService statusesService = getStatusesService();
        statusesService.homeTimeline(Constants.TWEETS_COUNT, null, null, null, null, null, null,
                getTwitterCallback(context, true));
    }

    @NonNull
    private TwitterCallback getTwitterCallback(@NonNull Context context, boolean isUpdate) {
        return new TwitterCallback(context, isUpdate, mTimelineView);
    }

    private static final class TwitterCallback extends Callback<List<Tweet>> {

        private final String TAG = TwitterCallback.class.getSimpleName();

        private Context mContext;
        private boolean isUpdate;
        private TimelineView mTimelineView;

        TwitterCallback(Context context, boolean isUpdate, TimelineView timelineView) {
            this.mContext = context;
            this.isUpdate = isUpdate;
            this.mTimelineView = timelineView;
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

            TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(mContext, fixedTweetTimeline);

            Constants.TWEET_ID_LIST = new ArrayList<>(adapter.getCount());
            for (int i = 0; i <= adapter.getCount() - 1; i++) {
                Constants.TWEET_ID_LIST.add(adapter.getItemId(i));
            }

            if (isUpdate) {
                Log.d(TAG, "update user tweet list");
                mTimelineView.updateUserTweetList(adapter);
            } else {
                Log.d(TAG, "create user tweet list");
                mTimelineView.displayUserTweetList(adapter);
            }

            resetMembers();
        }

        @Override
        public void failure(TwitterException exception) {
            Log.e(TAG, "failure(TwitterException exception)", exception);
            mTimelineView.displayError(exception.getMessage());
            resetMembers();
        }

        private void resetMembers() {
            mContext = null;
            mTimelineView = null;
        }
    }
}