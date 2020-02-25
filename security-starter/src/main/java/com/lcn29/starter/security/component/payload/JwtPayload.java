package com.lcn29.starter.security.component.payload;

import java.util.Date;
import java.util.Map;

/**
 * <pre>
 *  Jwt content
 *  {
 *      Header : {
 "          "alg": algorithm type,
 *          "typ": token tyep
 *      },
 *      Payload : {
 *          "iss": Issuer
 *          "sub": Subject
 *          "aud": Audience
 *          "exp": Expiration time
 *          "nbf": Not Before
 *          "iat": Issued at
 *          "jti": JWT ID
 *          ...
 *      },
 *      Signature: Signature String
 *  }
 *
 *  in this class, only provider the aud, exp, nbf and customized param for user to set
 *  when the user set the default config in the JwtService, it will use the default config
 *  iat set to now()
 * </pre>
 *
 * @author LCN
 * @date 2019-12-14 22:17
 */
public class JwtPayload {

    private String aud;

    private Date exp;

    private Date nbf;

    /**
     * customized  param
     */
    private Map<String, Object> customizedParamMap;


    public JwtPayload(){
    }

    public JwtPayload(String aud, Date exp, Date nbf, Map<String, Object> customizedParamMap) {
        this.aud = aud;
        this.exp = exp;
        this.nbf = nbf;
        this.customizedParamMap = customizedParamMap;
    }

    public String getAud() {
        return aud;
    }

    public JwtPayload setAud(String aud) {
        this.aud = aud;
        return this;
    }

    public Date getExp() {
        return exp;
    }

    public JwtPayload setExp(Date exp) {
        this.exp = exp;
        return this;
    }

    public Date getNbf() {
        return nbf;
    }

    public JwtPayload setNbf(Date nbf) {
        this.nbf = nbf;
        return this;
    }

    public Map<String, Object> getCustomizedParamMap() {
        return customizedParamMap;
    }

    public JwtPayload setCustomizedParamMap(Map<String, Object> customizedParamMap) {
        this.customizedParamMap = customizedParamMap;
        return this;
    }
}
