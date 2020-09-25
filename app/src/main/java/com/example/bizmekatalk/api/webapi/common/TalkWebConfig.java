package com.example.bizmekatalk.api.webapi.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TalkWebConfig {
    private String ip;
    private String port;
    private boolean isSsl = false;

    @Setter(value = AccessLevel.NONE)
    private String rootPath = "api";

    @Getter(value = AccessLevel.NONE)
    @Setter(value = AccessLevel.NONE)
    private static TalkWebConfig _instance = new TalkWebConfig();

    private TalkWebConfig() {}

    public static TalkWebConfig getInstance(){ return _instance; }

    public String httpScheme() { return httpScheme(isSsl); }
    public String httpScheme(boolean isSsl){ return isSsl ? "https://" : "http://"; }
}
