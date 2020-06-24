package com.axel.help.common;

import com.axel.help.remote.IGoogleAPI;
import com.axel.help.remote.RetrofitClient;

public class Common {

    public static final String baseURL = "https://maps.googleapis.com";
    public static IGoogleAPI getGoogleAPI()
    {
        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);
    }
}
