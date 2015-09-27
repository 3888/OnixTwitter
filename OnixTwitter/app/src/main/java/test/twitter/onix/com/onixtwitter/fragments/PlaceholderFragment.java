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
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.List;

import butterknife.ButterKnife;
import test.twitter.onix.com.onixtwitter.Constants;
import test.twitter.onix.com.onixtwitter.R;
import test.twitter.onix.com.onixtwitter.activities.BaseActivity;

public class PlaceholderFragment extends android.support.v4.app.Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = PlaceholderFragment.class.getSimpleName();
    private LinearLayout mTweetLayout;
    private View mFocusButton;
    private long mTweetId;
    private int mTweedIDPosition;
    private String mTweetImageUrl;


    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vertical_view_pager_item, container, false);

        mFocusButton = view.findViewById(R.id.view_pager_focus);

        FloatingActionButton fabUp;
        fabUp = (FloatingActionButton) view.findViewById(R.id.view_pager_placeholder_fab_up);
        fabUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTweedIDPosition > 0) {
                    setPage(--mTweedIDPosition);
                }
            }
        });

        FloatingActionButton fabDown;
        fabDown = (FloatingActionButton) view.findViewById(R.id.view_pager_placeholder_fab_down);
        fabDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTweedIDPosition < Constants.TWEET_ID_LIST.size() - 1) {
                    setPage(++mTweedIDPosition);
                }
            }
        });

        ButterKnife.bind(this, view);
        mTweetLayout = (LinearLayout) view.findViewById(R.id.view_pager_placeholder_tweet_layout);
        mTweedIDPosition = getArguments().getInt(ARG_SECTION_NUMBER);

        try {
            mTweetId = Constants.TWEET_ID_LIST.get(getArguments().getInt(ARG_SECTION_NUMBER));
        } catch (Exception e) {
            Log.d(TAG, "getArguments().getInt(ARG_SECTION_NUMBER) is null");
        }

        loadTweet(mTweetId);
        Log.d(TAG, "loadTweet " + mTweetId + " pos = " + mTweedIDPosition);
        return view;
    }

    private void setPage(int page) {
        ((BaseActivity) getActivity()).getVerticalViewPagerFragment().getViewPager().setCurrentItem(page);
    }

    private void loadTweet(long tweetId) {
        TweetUtils.loadTweet(tweetId, new Callback<Tweet>() {
            @Override
            public void success(final Result<Tweet> result) {
                mTweetLayout.removeAllViews();
                mTweetLayout.addView(new TweetView(getActivity(), result.data));
                if (result.data.entities.media != null) {
                    List<MediaEntity> urls = result.data.entities.media;
                    for (int i = 0; i < urls.size(); i++) {
                        mTweetImageUrl = urls.get(i).mediaUrl;
                        Log.e(TAG, "urls.get(i).mediaUrl = " + urls.get(i).mediaUrl);
                    }
                    mFocusButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                    PreviewZoomFragment.newInstance(mTweetImageUrl)).commit();
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