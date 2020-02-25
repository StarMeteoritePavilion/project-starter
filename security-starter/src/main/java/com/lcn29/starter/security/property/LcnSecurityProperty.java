package com.lcn29.starter.security.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <pre>
 *
 * </pre>
 *
 * @author LCN
 * @date 2020-02-22 17:33
 */

@ConfigurationProperties(prefix = "lcn.security")
public class LcnSecurityProperty {

    /**
     * whether need open security
     */
    private boolean enable = false;

    /**
     * the default issuer for token
     */
    private String issuer = "";

    /**
     * the default subject for token
     */
    private String subject = "";

    /**
     * token expire time, default 30 days
     */
    private long tokenExpTime = 30 * 24 * 60 * 60;

    /**
     * the way to create token's algorithm
     * 1: use the system default privateKey and publicKey
     * 2: use the customized privateKey String and publicKey String
     * 3: use the customized privateKey and publicKey in the file
     */
    private int tokenAlgorithmWay = 1;

    /**
     * the string of the public key or the file path of the public key
     */
    private String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiJhQDtqpwMoCal7SkFvgJAznAqgo8AE3eMQfDZN+CjR9Vu3JyA/U6CLrtDYtbHTBOiyGjS/Fmjm8dGth4V2/oJolf0pRQVd51gLNqz/6nVjgvvroWfgMPpTThPlFkfPIDXsOsyvgcgloMpN06/3oV+R81Hu2unixvdO5837HJA61IvkzxW9kaXyyeTv1hHxsZTKc397Hr1l0d6eF7f+elkyijmVGXCoFHRyaS6DW/tFMznNFievWmizKmMdIswY41je6Qgm1tjADRyNPBsII4P2mIiGJ/hrNkvz8s4QuL+y1NkviqfO6UiY0ueWCBAzOOpGrBrfU1m0kfi1owgqfdQIDAQAB";

    /**
     * the string of the private key or the file path of the private key
     */
    private String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCImFAO2qnAygJqXtKQW+AkDOcCqCjwATd4xB8Nk34KNH1W7cnID9ToIuu0Ni1sdME6LIaNL8WaObx0a2HhXb+gmiV/SlFBV3nWAs2rP/qdWOC++uhZ+Aw+lNOE+UWR88gNew6zK+ByCWgyk3Tr/ehX5HzUe7a6eLG907nzfsckDrUi+TPFb2RpfLJ5O/WEfGxlMpzf3sevWXR3p4Xt/56WTKKOZUZcKgUdHJpLoNb+0UzOc0WJ69aaLMqYx0izBjjWN7pCCbW2MANHI08Gwgjg/aYiIYn+Gs2S/PyzhC4v7LU2S+Kp87pSJjS55YIEDM46kasGt9TWbSR+LWjCCp91AgMBAAECggEATzJwiIxDjCIUEHR2BXkflgddEW9KfQ1Xik+76XUwyN3yJiqsqbB0Mzu8Cbq3z4BKYF5cjRO7sFX1bFKuZ6qhZo65WvpN7mK3o1vwv2/S8NQNSu3TNgbZopdLpHea3KrJvPcyaLPx6GyH23sfeYy33+38an5oTemYtIVD9Wf2MnLq4r32r8ZpLn2+Fcz+E7k2rTgK6AOEXq2CPB3nQylMHKZ2L9P1DKw7aCR7W4l4kK16Efo7tJumgNOxkVBNK58cls4mMXrhyHH0Du3+t/45hE81CHGsdLq/yzzm6XqwqmzuOn4th3OH+lBc4v483OLw5fIKDjoKk23rgNb1bNZQ6QKBgQD4o5GeUuvQ1YHwdPiNnazsoYD3/z1N63kbIqKs51LcFj5QXRH1MWUGxN55Kd0yJQZM/E1cPhA4fCUNgYg3A5cQD7JAFxPOV6Y7LqMumFEaqC0unUyhw0E/v86R7//j4Y9YWefozIV/T32TDABOCT9ZL16erdZ1A4kgl3cnnMUnjwKBgQCMo5BtsxpSUMNVdOABjwsKK6TeloFZoJ1ckYgQyhcXUmac9ctsAPoOHuV8GO7UJRZshcJ8Y/6vGvTs/Ej0vijqdPudLUHngjF3kRiQmxbDYhvEJmzg9udgDXCdoINRsi+LnvVBvWiFbjWDAJ7arWS8VK6z5kQ7A+uweeO+A6CmuwKBgQDCg/ydFxQjeZVBT9FPwSYSrbreyVDlj61cHDmV+Rbq1YAEkPtGjlER/Zxqv+SroSmPuuaunmNEPWMjcM3m9CkPSKhT2XoGOK30qOTCxCjXKPDIAgl8l+uI+v6ZT2AqoEDrhtKWXsKlucNs8bufUT69AAEPNMKNIqn51GVEmTHoYwKBgGTMGgi1tCjJH6bQ9HfJiNUKQMkTv7N4LTCaUZYO8YvSkB7w/5HyBUY3mKxfxlSgHDk9iQ3UMUZZIK/1hWh8HiEkJO4a8MA3jLKweJWzwhXY7COikqiN7XjnSQ6Aa34TIoaEMC2kzhPL0GiK+k+qtKdSXkAryj0t6y95vOOEABFfAoGAR5m62tKiZWRTKJQ30hWw82Lv3fR318OWhWkmuaGwASvOglkM4s+REsMU7geYK34ZNvLKHilq9dwvtGhNAEn9EMQJfDAjZZnD2RCVpG/fyx4VI8HMMtp5JA36+pGGX4rVkR6KkbcZuNkSwoJjNZlHcme7hsHjglxgrN5ETDD/Uxk=";

    /**
     * the url can pass without token, urls separated by ","
     */
    private String antMatchers = "";

    /**
     * the token key in the http header
     */
    private String jwtHttpHeader = "Authentication";

    // 生成 公私钥 字符串，然后将其保存在某个位置，
    // 后续启动判断，如果该位置有内容，读取里面的内容
    // https://blog.csdn.net/liuchaoxuan/article/details/82718879


    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public long getTokenExpTime() {
        return tokenExpTime;
    }

    public void setTokenExpTime(long tokenExpTime) {
        this.tokenExpTime = tokenExpTime;
    }

    public int getTokenAlgorithmWay() {
        return tokenAlgorithmWay;
    }

    public void setTokenAlgorithmWay(int tokenAlgorithmWay) {
        this.tokenAlgorithmWay = tokenAlgorithmWay;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getAntMatchers() {
        return antMatchers;
    }

    public void setAntMatchers(String antMatchers) {
        this.antMatchers = antMatchers;
    }

    public String getJwtHttpHeader() {
        return jwtHttpHeader;
    }

    public void setJwtHttpHeader(String jwtHttpHeader) {
        this.jwtHttpHeader = jwtHttpHeader;
    }
}
