package test.twitter.onix.com.onixtwitter.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
        View view = inflater.inflate(R.layout.fragment_vertical_view_pager_item, container, false);
        Log.d(TAG, "onCreateView ");

        Log.d(TAG, "mViewPager.getCurrentItem()*******  " + VerticalViewPagerFragment.sViewPager.getCurrentItem());

        FloatingActionButton fabUp;
        fabUp = (FloatingActionButton) view.findViewById(R.id.view_pager_placeholder_fab_up);
        fabUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTweedIDPosition > 0) {
                    Log.d(TAG, "fabUp" + "\n");
                    mTweedIDPosition--;
                    Log.d(TAG, "mTweedIDPosition-- " + mTweedIDPosition);
                    if (mTweedIDPosition == 0) {
                        VerticalViewPagerFragment.sViewPager.setCurrentItem(mTweedIDPosition + 1);
                        Log.d(TAG, "IF******setCurrentItem   " + (mTweedIDPosition + 1));
                    } else {
                        VerticalViewPagerFragment.sViewPager.setCurrentItem(mTweedIDPosition);
                        Log.d(TAG, "ELSE******setCurrentItem  " + mTweedIDPosition);
                    }

                    mTweetId = Constants.TWEET_ID_LIST.get(mTweedIDPosition);
                    loadTweet(mTweetId);
                    Log.d(TAG, "loadTweet " + mTweetId);
                }
            }
        });

        FloatingActionButton fabDown;
        fabDown = (FloatingActionButton) view.findViewById(R.id.view_pager_placeholder_fab_down);
        fabDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTweedIDPosition < Constants.TWEETS_COUNT - 1) {
                    Log.d(TAG, "fabDown" + "\n");
                    mTweedIDPosition++;
                    Log.d(TAG, "mTweedIDPosition++ " + mTweedIDPosition);
                    if (mTweedIDPosition == Constants.TWEETS_COUNT - 1) {
                        VerticalViewPagerFragment.sViewPager.setCurrentItem(mTweedIDPosition - 1);
                        Log.d(TAG, "IF******setCurrentItem  " + (mTweedIDPosition - 1));

                    } else {
                        VerticalViewPagerFragment.sViewPager.setCurrentItem(mTweedIDPosition);
                        Log.d(TAG, "ELSE******setCurrentItem" + mTweedIDPosition);
                    }
                    mTweetId = Constants.TWEET_ID_LIST.get(mTweedIDPosition);
                    loadTweet(mTweetId);
                    Log.d(TAG, "loadTweet " + mTweetId);
                }
            }
        });

        ButterKnife.bind(this, view);
        mTweetLayout = (LinearLayout) view.findViewById(R.id.view_pager_placeholder_tweet_layout);
        mTweedIDPosition = getArguments().getInt(ARG_SECTION_NUMBER);
        Log.d(TAG, "mTweedIDPosition = getArguments().getInt(ARG_SECTION_NUMBER) " + mTweedIDPosition);
        try {
            mTweetId = Constants.TWEET_ID_LIST.get(getArguments().getInt(ARG_SECTION_NUMBER));
        } catch (Exception e) {
            Log.d(TAG, "getArguments().getInt(ARG_SECTION_NUMBER) is null");
        }

        loadTweet(mTweetId);
        Log.d(TAG, "loadTweet " + mTweetId);
        return view;
    }

    private void loadTweet(long tweetId) {

        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
            @Override
            public void success(final Result<Tweet> result) {
                mTweetLayout.removeAllViews();
                mTweetLayout.addView(new TweetView(getActivity(), result.data));
                if (result.data.entities.media != null)
                {
                    Log.e(TAG, "******************" + result.data.entities.urls.toArray());

                }else {

                        mTweetLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                           VerticalViewPagerFragment.sViewPager.setAdapter(null);

                            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    ZoomFragment.newInstance()).commit();
                        }
                    });
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