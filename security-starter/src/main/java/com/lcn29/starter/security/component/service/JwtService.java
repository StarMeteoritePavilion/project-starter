package com.lcn29.starter.security.component.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.lcn29.kit.util.CollectionMapUtil;
import com.lcn29.starter.security.component.payload.JwtPayload;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <pre>
 *
 * </pre>
 *
 * @author LCN
 * @date 2020-02-25 15:58
 */
public class JwtService {
    /**
     * the default issuer for token
     */
    private String issuer;

    /**
     * default available time for token
     */
    private String subject;

    /**
     * token expire time, default 30 days
     */
    private long tokenExpTime = 30 * 24 * 60 * 60;

    /**
     * encryption algorithm
     */
    private Algorithm algorithm;

    /**
     * token verifier
     */
    private JWTVerifier verifier;

    public JwtService(){
    }

    /**
     * builder the verifier
     */
    public JwtService build() {
        this.verifier = JWT.require(algorithm)
            .withIssuer(issuer)
            .withSubject(subject)
            .build();
        return this;
    }

    /**
     * generate the jwt token
     * @param payload
     * @return
     */
    public String generateToken(JwtPayload payload) {
        JWTCreator.Builder tokenBuilder = getJwtBuilder(payload);
        return tokenBuilder.sign(algorithm);
    }

    /**
     * refresh token
     * this method will make the Customized param loss
     * @param token
     * @param exp
     * @return
     */
    public String refreshToken(String token, Long exp) {

        DecodedJWT jwt = verifier.verify(token);
        List<String> audiences = jwt.getAudience();

        JwtPayload payload = new JwtPayload();
        String audienceName = CollectionMapUtil.isNotEmpty(audiences) ? "" : audiences.get(0);
        Optional.ofNullable(jwt.getNotBefore()).ifPresent(payload::setNbf);
        Date expirationDate = calculateExpirationDate(new Date(), exp);
        payload.setAud(audienceName).setExp(expirationDate);

        JWTCreator.Builder jwtBuilder = getJwtBuilder(payload);
        return jwtBuilder.sign(algorithm);
    }

    /**
     * Verify that the token is useful
     * @param token
     * @param userName
     * @return
     */
    public boolean validateToken(String token, String userName) {
        DecodedJWT jwt = verifier.verify(token);
        List<String> audiences = jwt.getAudience();
        String audienceName = CollectionMapUtil.isNotEmpty(audiences) ? "" : audiences.get(0);
        if (!audienceName.equals(userName)) {
            // userName inconformity
            return false;
        }
        Date expiredDate = jwt.getExpiresAt();
        return expiredDate.before(new Date());
    }

    /**
     * get username from token
     * @param token
     * @return
     */
    public String getAudienceFromToken(String token) {
        DecodedJWT jwt = verifier.verify(token);
        List<String> audiences = jwt.getAudience();
        if (CollectionMapUtil.isEmpty(audiences)) {
            return "";
        }
        // get first
        return audiences.get(0);
    }

    /**
     * get all the customized param
     * @param token
     * @return
     */
    public Map<String, Claim> getCustomizedParams(String token) {
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaims();
    }

    /**
     * get one customized param
     * @param token
     * @param key
     * @return
     */
    public Claim getCustomizedParam(String token, String key) {
        return getCustomizedParams(token).get(key);
    }

    /**
     * judge the token is expired or not
     *
     * @param token
     * @return
     */
    private Boolean isTokenExpired(String token) {
        Date expiration = getExpiredDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * get the expired date from token
     * @param token
     *
     * @return
     */
    private Date getExpiredDateFromToken(String token) {
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getExpiresAt();
    }

    /**
     * get issued date from token
     * @return
     */
    private Date getIssuedAtDateFromToken(String token) {
        DecodedJWT jwt = verifier.verify(token);
        Date issuedAt = jwt.getIssuedAt();
        return issuedAt;
    }

    /**
     *
     * create Jwt Builder that is used in generate token
     *
     * @return
     */
    private JWTCreator.Builder getJwtBuilder(JwtPayload payload) {

        JWTCreator.Builder builder = JWT.create();
        // default config
        Optional.ofNullable(issuer).ifPresent(builder::withIssuer);
        Optional.ofNullable(subject).ifPresent(builder::withSubject);
        // customized config
        Optional.ofNullable(payload.getAud()).ifPresent(builder::withAudience);
        Optional.ofNullable(payload.getNbf()).ifPresent(builder::withNotBefore);
        Optional.ofNullable(payload.getCustomizedParamMap()).ifPresent(map -> {
            map.forEach((k, v) -> {
                if (v instanceof Integer) {
                    builder.withClaim(k, (Integer) v);
                } else if (v instanceof Boolean) {
                    builder.withClaim(k, (Boolean)v);
                } else if (v instanceof Double) {
                    builder.withClaim(k, (Double)v);
                } else if (v instanceof Long) {
                    builder.withClaim(k,(Long)v);
                } else if (v instanceof String) {
                    builder.withClaim(k, (String)v);
                } else if (v instanceof Date) {
                    builder.withClaim(k, (Date)v);
                }
            });
        });
        builder.withExpiresAt(Optional.ofNullable(payload.getExp()).orElse(getDefaultTokenExpTime()));
        // issued date default current
        builder.withIssuedAt(new Date());
        return builder;
    }

    /**
     * get the expired date of the token
     * @return
     */
    private Date getDefaultTokenExpTime() {
        return new Date(System.currentTimeMillis() + tokenExpTime);
    }

    /**
     * judge whether the token's create date is before the last password reset date
     * @param created
     *
     * @param lastPasswordReset 上次密码重置时间
     * @return
     */
    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    /**
     * generate exp date for token
     * @param createdDate start Date
     * @param expiration how long exp (unit: seconds)
     *
     * @return
     */
    private Date calculateExpirationDate (Date createdDate, Long expiration) {
        Long expirationTime = expiration == null ? tokenExpTime : expiration;
        return new Date(createdDate.getTime() + expirationTime * 1000);
    }

    public String getIssuer() {
        return issuer;
    }

    public JwtService setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public JwtService setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public long getTokenExpTime() {
        return tokenExpTime;
    }

    public JwtService setTokenExpTime(long tokenExpTime) {
        this.tokenExpTime = tokenExpTime;
        return this;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this.algorithm = algorithm;
    }
}
