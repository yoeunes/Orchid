package com.eden.orchid.posts.permalink.pathTypes;

import com.eden.orchid.api.theme.pages.OrchidPage;
import com.eden.orchid.posts.permalink.PermalinkPathType;

import javax.inject.Inject;

public class DataPropertyPathType extends PermalinkPathType {

    @Inject
    public DataPropertyPathType() {
        super(1);
    }

    @Override
    public boolean acceptsKey(OrchidPage page, String key) {
        return page.getData().has(key);
    }

    @Override
    public String format(OrchidPage page, String key) {
        return page.getData().get(key).toString();
    }

}
