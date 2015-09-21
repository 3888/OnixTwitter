package test.twitter.onix.com.onixtwitter.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import test.twitter.onix.com.onixtwitter.R;


public class HomeTimelineViewPagerFragment extends Fragment {

    private static final String TAG = HomeTimelineViewPagerFragment.class.getSimpleName();

    public static HomeTimelineViewPagerFragment newInstance() {
        Log.d(TAG, "HomeTimelineFragment newInstance()");
        return new HomeTimelineViewPagerFragment();
    }

    public HomeTimelineViewPagerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_pager, container, false);

        final LinearLayout myLayout
                = (LinearLayout) view.findViewById(R.id.my_tweet_layout);

        final long tweetId = 645947103408054272L;
        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                myLayout.addView(new TweetView(getActivity(), result.data));
            }

            @Override
            public void failure(TwitterException exception) {
                // Toast.makeText(...).show();
            }

        });

        return view;
    }
}