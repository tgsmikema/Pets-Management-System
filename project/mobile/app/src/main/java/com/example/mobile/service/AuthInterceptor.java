package com.example.mobile.service;

import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private String token;

    public AuthInterceptor(String token) {
        this.token = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String basicAuth = "Basic " + Base64.encodeToString(token.getBytes(), Base64.NO_WRAP);

        Request request = chain.request().newBuilder()
                .addHeader("Authorization", basicAuth)
                .build();
        return chain.proceed(request);
    }
}
