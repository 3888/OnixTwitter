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

import butterknife.Bind;
import butterknife.ButterKnife;
import test.twitter.onix.com.onixtwitter.PreferencesHelper;
import test.twitter.onix.com.onixtwitter.R;

public class ProfileFragment extends Fragment {

    private static final String TAG = ProfileFragment.class.getSimpleName();
    private PreferencesHelper mSPHelper;

    @Bind(R.id.profile_user_image_view) ImageView mUserImage;
    @Bind(R.id.profile_name) TextView mUserName;
    @Bind(R.id.profile_nick_name) TextView mUserNickName;
    @Bind(R.id.profile_tweets) TextView mUserTweets;
    @Bind(R.id.profile_followers) TextView mUserFollowers;
    @Bind(R.id.profile_following) TextView mUserFollowing;
    @Bind(R.id.profile_location) TextView mUserLocation;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mSPHelper = new PreferencesHelper(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        TwitterSession session = Twitter.getSessionManager()
                .getActiveSession();

        Twitter.getApiClient(session).getAccountService()
                .verifyCredentials(true, false, new Callback<User>() {

                    @Override
                    public void success(Result<User> userResult) {
                        User user = userResult.data;

                        mUserName.setText(user.name);
                        mUserNickName.append("@" + mSPHelper.getString("NICK_NAME"));
                        if ((user.location.equals(""))) {
                            mUserLocation.setText(getString(R.string.profile_no_location));
                        } else {
                            mUserLocation.setText(user.location);
                        }
                        mUserTweets.setText(String.valueOf(user.statusesCount));
                        mUserFollowers.setText(String.valueOf(user.followersCount));
                        mUserFollowing.setText(String.valueOf(user.friendsCount));

                        Picasso.with(getActivity()).load(user.profileImageUrl.
                                replace("_normal", "_bigger")).into(mUserImage);
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