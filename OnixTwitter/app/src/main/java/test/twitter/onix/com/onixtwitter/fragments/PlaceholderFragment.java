package test.twitter.onix.com.onixtwitter.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.melnykov.fab.FloatingActionButton;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import butterknife.ButterKnife;
import test.twitter.onix.com.onixtwitter.Constants;
import test.twitter.onix.com.onixtwitter.R;

public class PlaceholderFragment extends android.support.v4.app.Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = PlaceholderFragment.class.getSimpleName();

    private LinearLayout mTweetLayout;
    private int mTweetIdPosition;
    private long mTweetId;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_pager_item, container, false);
        Log.d(TAG, "onCreateView ");

        FloatingActionButton fabUp;
        fabUp = (FloatingActionButton) rootView.findViewById(R.id.view_pager_placeholder_fab_up);
        fabUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTweetIdPosition > 0) {
                    mTweetIdPosition--;
                    Log.d(TAG, "mTweetIdPosition-- " + mTweetIdPosition);
                    mTweetId = Constants.TWEET_ID_LIST.get(mTweetIdPosition);
                    loadTweet(mTweetId);
                    Log.d(TAG, "loadTweet " + mTweetId);
                    newInstance(mTweetIdPosition);
                }
            }
        });

        FloatingActionButton fabDown;
        fabDown = (FloatingActionButton) rootView.findViewById(R.id.view_pager_placeholder_fab_down);
        fabDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTweetIdPosition < Constants.TWEETS_COUNT - 1) {
                    mTweetIdPosition++;
                    Log.d(TAG, "mTweetIdPosition++ " + mTweetIdPosition);
                    mTweetId = Constants.TWEET_ID_LIST.get(mTweetIdPosition);
                    loadTweet(mTweetId);
                    Log.d(TAG, "loadTweet " + mTweetId);
                    newInstance(mTweetIdPosition);
                }
            }
        });

        ButterKnife.bind(this, rootView);
        mTweetLayout = (LinearLayout) rootView.findViewById(R.id.view_pager_placeholder_tweet_layout);
        mTweetIdPosition = getArguments().getInt(ARG_SECTION_NUMBER);
        Log.d(TAG, "mTweetIdPosition " + mTweetIdPosition);
        mTweetId = Constants.TWEET_ID_LIST.get(getArguments().getInt(ARG_SECTION_NUMBER));
        loadTweet(mTweetId);
        Log.d(TAG, "loadTweet " + mTweetId);
        return rootView;
    }

    private void loadTweet(long tweetId) {
        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                mTweetLayout.removeAllViews();
                mTweetLayout.addView(new TweetView(getActivity(), result.data));
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e(TAG, "Sign in failure", exception);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}