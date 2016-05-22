package test.twitter.onix.com.onixtwitter.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import test.twitter.onix.com.onixtwitter.R;

public class PreviewZoomFragment extends Fragment {

    private static final String ARG_TWEET_IMAGE_URL = "ARG_TWEET_IMAGE_URL";

    public static PreviewZoomFragment newInstance(String tweetImageUrl) {
        PreviewZoomFragment fragment = new PreviewZoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TWEET_IMAGE_URL, tweetImageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview_zoom, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.zoom_image_view);
        Picasso.with(getActivity()).load(getArguments().getString(ARG_TWEET_IMAGE_URL)).into(imageView);
        return view;
    }
}