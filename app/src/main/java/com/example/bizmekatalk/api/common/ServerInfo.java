package com.example.bizmekatalk.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ServerInfo {
    private String ip;
    private String port;
    private boolean ssl;
}
