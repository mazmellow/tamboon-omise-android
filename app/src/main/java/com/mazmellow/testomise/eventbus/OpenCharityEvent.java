package com.mazmellow.testomise.eventbus;

import com.mazmellow.testomise.model.Charity;

/**
 * Created by maz on 9/12/2017 AD.
 */

public class OpenCharityEvent {
    private Charity model;

    public OpenCharityEvent(Charity model) {
        this.model = model;
    }

    public Charity getModel() {
        return model;
    }
}
