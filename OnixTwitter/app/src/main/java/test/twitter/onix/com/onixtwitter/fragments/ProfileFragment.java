package test.twitter.onix.com.onixtwitter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.User;

import test.twitter.onix.com.onixtwitter.PreferencesHelper;
import test.twitter.onix.com.onixtwitter.R;

public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();

    private PreferencesHelper mSPHelper;
    private ImageView mUserImage;
    private TextView mUserName;
    private TextView mUserNickName;
    private TextView mUserTweets;
    private TextView mUserFollowers;
    private TextView mUserFollowing;
    private TextView mUserLocation;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSPHelper = new PreferencesHelper(getActivity().getApplicationContext());

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mUserImage = (ImageView) view.findViewById(R.id.profile_user_image_view);
        mUserName = (TextView) view.findViewById(R.id.profile_name);
        mUserNickName = (TextView) view.findViewById(R.id.profile_nick_name);
        mUserTweets = (TextView) view.findViewById(R.id.profile_tweets);
        mUserFollowers = (TextView) view.findViewById(R.id.profile_followers);
        mUserFollowing = (TextView) view.findViewById(R.id.profile_following);
        mUserLocation = (TextView) view.findViewById(R.id.profile_location);

        TwitterSession session = Twitter.getSessionManager()
                .getActiveSession();

        Twitter.getApiClient(session).getAccountService()
                .verifyCredentials(true, false, new Callback<User>() {

                    @Override
                    public void success(Result<User> userResult) {
                        User user = userResult.data;

                        mUserName.setText(user.name);
                        mUserNickName.setText("@" + mSPHelper.getString("NICK_NAME"));
                        if ((user.location == "")) {
                            mUserLocation.setText(getString(R.string.profile_no_location));
                        } else {
                            mUserLocation.setText(user.location);
                        }
                        mUserTweets.setText(Integer.toString(user.statusesCount));
                        mUserFollowers.setText(Integer.toString(user.followersCount));
                        mUserFollowing.setText(Integer.toString(user.friendsCount));

                        Picasso.with(getActivity()).load(user.profileImageUrl.replace("_normal", "_bigger")).into(mUserImage);
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.e(TAG, "failure(TwitterException e)");
                    }
                });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated(Bundle savedInstanceState)");
    }
}