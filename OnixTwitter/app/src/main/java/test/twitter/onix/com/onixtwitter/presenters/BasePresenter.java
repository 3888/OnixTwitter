package test.twitter.onix.com.onixtwitter.presenters;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.services.StatusesService;

public abstract class BasePresenter {

    protected StatusesService getStatusesService() {
        return TwitterCore.getInstance().getApiClient().getStatusesService();
    }
}