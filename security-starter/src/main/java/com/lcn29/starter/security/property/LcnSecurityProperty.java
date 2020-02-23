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
}
