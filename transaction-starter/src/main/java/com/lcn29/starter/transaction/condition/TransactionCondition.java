package com.lcn29.starter.transaction.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.StringUtils;

/**
 * <pre>
 *
 * </pre>
 *
 * @author LCN
 * @date 2020-02-19 20:18
 */
public class TransactionCondition implements Condition {

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {

        Environment env = conditionContext.getEnvironment();
        String property = env.getProperty("lcn.transaction.enable");
        // open transaction need config the lcn.transaction.enable = true
        if (StringUtils.isEmpty(property) || !Boolean.parseBoolean(property)) {
            return false;
        }
        DataSourceTransactionManager bean = conditionContext.getBeanFactory().getBean(DataSourceTransactionManager.class);
        if (bean == null) {
            return false;
        }
        return true;
    }
}
