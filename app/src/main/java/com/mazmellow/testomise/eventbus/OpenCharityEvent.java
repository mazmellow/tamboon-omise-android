package com.mazmellow.testomise.eventbus;

import com.mazmellow.testomise.model.CharityModel;

/**
 * Created by maz on 9/12/2017 AD.
 */

public class OpenCharityEvent {
    private CharityModel model;

    public OpenCharityEvent(CharityModel model) {
        this.model = model;
    }

    public CharityModel getModel() {
        return model;
    }
}
