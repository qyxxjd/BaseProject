package com.classic.core.http.cookie;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

@SuppressWarnings({"unused", "WeakerAccess"}) public interface CookieStore {

    void add(HttpUrl uri, List<Cookie> cookie);

    List<Cookie> get(HttpUrl uri);

    List<Cookie> getCookies();

    boolean remove(HttpUrl uri, Cookie cookie);

    boolean removeAll();
}
