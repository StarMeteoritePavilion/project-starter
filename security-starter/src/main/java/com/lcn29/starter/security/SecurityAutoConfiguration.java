package com.lcn29.starter.security;

import com.lcn29.starter.security.property.LcnSecurityProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * <pre>
 *
 * </pre>
 *
 * @author LCN
 * @date 2020-02-22 17:33
 */
@EnableConfigurationProperties(LcnSecurityProperty.class)
public class SecurityAutoConfiguration {

    // 生成 公私钥 字符串，然后将其保存在某个位置，
    // 后续启动判断，如果该位置有内容，读取里面的内容
    // https://blog.csdn.net/liuchaoxuan/article/details/82718879
}
