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

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import butterknife.ButterKnife;
import test.twitter.onix.com.onixtwitter.Constants;
import test.twitter.onix.com.onixtwitter.R;
import test.twitter.onix.com.onixtwitter.activities.BaseActivity;

public class PlaceholderFragment extends android.support.v4.app.Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = PlaceholderFragment.class.getSimpleName();
    private LinearLayout mTweetLayout;
    private long mTweetId;
    private int mTweedIDPosition;


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

        Log.d(TAG, "mViewPager.getCurrentItem()*******  " + BaseActivity.mViewPager.getCurrentItem());

        FloatingActionButton fabUp;
        fabUp = (FloatingActionButton) rootView.findViewById(R.id.view_pager_placeholder_fab_up);
        fabUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTweedIDPosition > 0) {
                    Log.d(TAG, "fabUp" + "\n");
                    mTweedIDPosition--;
                    Log.d(TAG, "mTweedIDPosition-- " + mTweedIDPosition);
                    if (mTweedIDPosition == 0) {
                        BaseActivity.mViewPager.setCurrentItem(mTweedIDPosition + 1);
                        Log.d(TAG, "IF******setCurrentItem   " + (mTweedIDPosition + 1));
                    } else {
                        BaseActivity.mViewPager.setCurrentItem(mTweedIDPosition);
                        Log.d(TAG, "ELSE******setCurrentItem  " + mTweedIDPosition);
                    }

                    mTweetId = Constants.TWEET_ID_LIST.get(mTweedIDPosition);
                    loadTweet(mTweetId);
                    Log.d(TAG, "loadTweet " + mTweetId);
                }
            }
        });

        FloatingActionButton fabDown;
        fabDown = (FloatingActionButton) rootView.findViewById(R.id.view_pager_placeholder_fab_down);
        fabDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTweedIDPosition < Constants.TWEETS_COUNT - 1) {
                    Log.d(TAG, "fabDown" + "\n");
                    mTweedIDPosition++;
                    Log.d(TAG, "mTweedIDPosition++ " + mTweedIDPosition);
                    if (mTweedIDPosition == Constants.TWEETS_COUNT - 1) {
                        BaseActivity.mViewPager.setCurrentItem(mTweedIDPosition - 1);
                        Log.d(TAG, "IF******setCurrentItem  " + (mTweedIDPosition - 1));

                    } else {
                        BaseActivity.mViewPager.setCurrentItem(mTweedIDPosition);
                        Log.d(TAG, "ELSE******setCurrentItem" + mTweedIDPosition);
                    }
                    mTweetId = Constants.TWEET_ID_LIST.get(mTweedIDPosition);
                    loadTweet(mTweetId);
                    Log.d(TAG, "loadTweet " + mTweetId);
                }
            }
        });

        ButterKnife.bind(this, rootView);
        mTweetLayout = (LinearLayout) rootView.findViewById(R.id.view_pager_placeholder_tweet_layout);
        mTweedIDPosition = getArguments().getInt(ARG_SECTION_NUMBER);
        Log.d(TAG, "mTweedIDPosition = getArguments().getInt(ARG_SECTION_NUMBER) " + mTweedIDPosition);
        try {
            mTweetId = Constants.TWEET_ID_LIST.get(getArguments().getInt(ARG_SECTION_NUMBER));
        } catch (Exception e) {
            Log.d(TAG, "getArguments().getInt(ARG_SECTION_NUMBER) is null");
        }

        loadTweet(mTweetId);
        Log.d(TAG, "loadTweet " + mTweetId);
        return rootView;
    }

    private void loadTweet(long tweetId) {

        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
            @Override
            public void success(final Result<Tweet> result) {
                mTweetLayout.removeAllViews();
                mTweetLayout.addView(new TweetView(getActivity(), result.data));
                if (result.data.entities.media != null) {

                    Log.e(TAG, "******************" + result.data.entities.urls.toArray());

//                    mTweetLayout.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            BaseActivity.mViewPager.setAdapter(null);
//
//                        getFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                                ZoomFragment.newInstance()).commit();
//                        }
//                    });
                }
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