package test.twitter.onix.com.onixtwitter.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import test.twitter.onix.com.onixtwitter.R;
import test.twitter.onix.com.onixtwitter.callbacks.TweetComposerCallback;
import test.twitter.onix.com.onixtwitter.presenters.TweetComposerPresenter;
import test.twitter.onix.com.onixtwitter.presenters.TweetComposerPresenterImpl;
import test.twitter.onix.com.onixtwitter.views.TweetComposerView;

public class TweetComposerFragment extends Fragment implements TweetComposerView {

    private static final String TAG = TweetComposerFragment.class.getSimpleName();

    private Button mTweetButton;
    private EditText mEditText;
    private TweetComposerCallback mTweetComposerCallback;
    private TweetComposerPresenter mTweetComposerPresenter;

    public TweetComposerFragment() { }

    public static TweetComposerFragment newInstance() {
        return new TweetComposerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");

        mTweetComposerPresenter = TweetComposerPresenterImpl.newInstance(this, getActivity());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mTweetComposerCallback = (TweetComposerCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement TweetComposerCallback");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tweet, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated(Bundle savedInstanceState)");

        mTweetButton = (Button) getActivity().findViewById(R.id.tweet_button);
        mEditText = (EditText) getActivity().findViewById(R.id.tweet_box);
        mEditText.requestFocus();

        mTweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "click before building tweet");

                mTweetComposerPresenter.createTweet(mEditText.getText().toString());
                InputMethodManager imm = (InputMethodManager) getActivity().
                        getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        InputMethodManager imm = (InputMethodManager) getActivity().
                getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void tweetSent() {
        mTweetComposerCallback.showUpdatedTimeline();
    }

    @Override
    public void tweetError(String error) {
        displayToast(error);
    }

    void displayToast(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }
}