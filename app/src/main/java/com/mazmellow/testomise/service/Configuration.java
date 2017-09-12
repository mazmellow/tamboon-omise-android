package com.mazmellow.testomise.service;

import com.mazmellow.testomise.util.StringUtil;

public class Configuration {

    public String getServerUrl() {
        byte[] bytes = {104, 116, 116, 112, 115, 58, 47, 47, 116, 97, 109, 98, 111, 111, 110, 45, 111, 109, 105, 115, 101, 46, 104, 101, 114, 111, 107, 117, 97, 112, 112, 46, 99, 111, 109, 47};
        return StringUtil.bytesToString(bytes);
    }

    public String getHostName() {
        byte[] bytes = {116, 97, 109, 98, 111, 111, 110, 45, 111, 109, 105, 115, 101, 46, 104, 101, 114, 111, 107, 117, 97, 112, 112, 46, 99, 111, 109};
        return StringUtil.bytesToString(bytes);
    }

    public String getKey() {
        byte[] bytes = {51, 48, 56, 50, 48, 49, 50, 50, 51, 48, 48, 100, 48, 54, 48, 57, 50, 97, 56, 54, 52, 56, 56, 54, 102, 55, 48, 100, 48, 49, 48, 49, 48, 49, 48, 53, 48, 48, 48, 51, 56, 50, 48, 49, 48, 102, 48, 48, 51, 48, 56, 50, 48, 49, 48, 97, 48, 50, 56, 50, 48, 49, 48, 49, 48, 48, 99, 57, 99, 100, 100, 54, 98, 49, 102, 56, 51, 57, 98, 54, 50, 101, 54, 51, 57, 51, 52, 55, 54, 102, 101, 99, 53, 53, 101, 49, 53, 53, 55, 55, 100, 49, 57, 102, 57, 100, 99, 54, 57, 57, 49, 50, 55, 49, 54, 49, 53, 54, 99, 97, 50, 101, 51, 50, 51, 55, 99, 101, 57, 101, 52, 99, 101, 48, 51, 100, 102, 52, 48, 102, 52, 53, 55, 54, 101, 48, 98, 52, 100, 49, 52, 48, 101, 48, 98, 52, 99, 99, 49, 97, 48, 99, 51, 102, 101, 98, 101, 56, 98, 53, 53, 102, 98, 54, 53, 54, 57, 56, 100, 51, 50, 57, 56, 97, 50, 57, 97, 101, 51, 101, 51, 98, 54, 101, 48, 102, 49, 48, 55, 101, 52, 99, 97, 101, 97, 52, 55, 100, 100, 55, 100, 99, 56, 57, 51, 53, 50, 100, 97, 50, 102, 97, 53, 57, 52, 57, 100, 51, 49, 52, 48, 55, 98, 54, 49, 57, 52, 97, 98, 49, 54, 102, 52, 54, 49, 101, 101, 99, 98, 50, 55, 56, 101, 56, 98, 97, 54, 54, 57, 50, 98, 98, 55, 48, 102, 49, 97, 101, 54, 97, 100, 52, 55, 101, 54, 55, 57, 56, 51, 48, 56, 99, 54, 99, 52, 102, 97, 48, 99, 49, 51, 50, 98, 55, 53, 98, 51, 51, 51, 49, 55, 102, 48, 49, 53, 49, 57, 97, 54, 99, 49, 102, 101, 102, 102, 100, 52, 56, 57, 100, 98, 51, 57, 52, 50, 100, 50, 57, 52, 101, 54, 100, 99, 102, 100, 51, 97, 52, 48, 101, 54, 53, 98, 57, 49, 48, 49, 51, 51, 56, 57, 48, 99, 50, 100, 51, 57, 54, 51, 101, 51, 101, 102, 54, 102, 51, 57, 54, 53, 57, 53, 48, 54, 49, 99, 50, 55, 49, 102, 54, 102, 102, 100, 53, 100, 54, 50, 51, 50, 54, 57, 99, 53, 52, 56, 97, 99, 57, 98, 99, 97, 55, 100, 52, 99, 48, 50, 49, 57, 57, 100, 54, 101, 57, 52, 56, 49, 57, 101, 51, 99, 101, 101, 98, 101, 98, 53, 100, 56, 55, 49, 52, 57, 54, 48, 53, 55, 100, 50, 100, 100, 98, 48, 56, 55, 57, 100, 50, 101, 48, 57, 98, 50, 52, 52, 101, 99, 52, 57, 52, 56, 51, 98, 48, 50, 101, 50, 55, 51, 101, 99, 102, 99, 55, 56, 101, 56, 48, 48, 48, 97, 98, 56, 54, 50, 55, 99, 48, 97, 56, 53, 55, 100, 48, 51, 53, 49, 56, 55, 48, 50, 97, 100, 50, 55, 56, 48, 53, 99, 50, 57, 56, 55, 98, 56, 98, 97, 98, 53, 48, 50, 50, 48, 48, 51, 49, 52, 97, 99, 57, 101, 50, 53, 99, 55, 99, 102, 100, 101, 51, 102, 50, 54, 48, 54, 54, 50, 57, 97, 51, 101, 54, 98, 57, 102, 53, 100, 49, 100, 54, 100, 55, 53, 51, 54, 102, 49, 51, 56, 101, 57, 53, 100, 48, 55, 53, 56, 49, 48, 50, 48, 51, 48, 49, 48, 48, 48, 49};
        return StringUtil.bytesToString(bytes);
    }
}
