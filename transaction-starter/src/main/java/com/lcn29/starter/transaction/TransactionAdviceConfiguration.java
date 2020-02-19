package com.lcn29.starter.transaction;

import com.lcn29.starter.transaction.property.LcnTransactionProperty;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * <pre>
 *
 * </pre>
 *
 * @author LCN
 * @date 2020-02-19 18:32
 */

@Aspect
@AutoConfigureAfter(DataSourceTransactionManager.class)
@ConditionalOnBean(DataSourceTransactionManager.class)
@ConditionalOnProperty(value = "lcn.transaction.enable", havingValue = "true")
@EnableConfigurationProperties({LcnTransactionProperty.class})
public class TransactionAdviceConfiguration {

    @Resource
    private LcnTransactionProperty lcnTransactionProperty;

    @Resource
    private DataSourceTransactionManager transactionManager;

    private final static int[] NO_ALLOW_ISOLATION_LEVEL = {0, 3, 5};

    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(lcnTransactionProperty.getAdvisorExpression());
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }

    /**
     * create advice
     * @return
     */
    private TransactionInterceptor txAdvice() {

        if (Objects.isNull(lcnTransactionProperty.getAdvisorExpression())) {
            throw new UnsupportedOperationException("the \"lcn.transaction.advisorExpression\" must be set !");
        }

        DefaultTransactionAttribute txAttrRequired = new DefaultTransactionAttribute();
        txAttrRequired.setPropagationBehavior(getPropagationBehavior(lcnTransactionProperty.getPropagationBehavior()));
        txAttrRequired.setIsolationLevel(getIsolationLevel(lcnTransactionProperty.getIsolationLevel()));
        txAttrRequired.setReadOnly(lcnTransactionProperty.getReadOnly());
        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
        source.addTransactionalMethod("*", txAttrRequired);
        return new TransactionInterceptor(transactionManager, source);
    }

    /**
     * if the curValue > 6 or curValue < 0, return 0;
     *
     * @param curValue handler value
     * @return
     */
    private int getPropagationBehavior(int curValue) {
        if (curValue >= TransactionDefinition.PROPAGATION_REQUIRED &&
            curValue <= TransactionDefinition.PROPAGATION_NESTED) {
            return curValue;
        }
        return TransactionDefinition.PROPAGATION_REQUIRED;
    }


    /**
     * if the curValue > 8 or curValue < 0, return -1;
     * or the curValue = 0, 3, 5 return - ;
     *
     * @param curValue handler value
     * @return
     */
    private int getIsolationLevel(int curValue) {
        for (int value : NO_ALLOW_ISOLATION_LEVEL) {
            if (curValue == value) {
                return TransactionDefinition.ISOLATION_DEFAULT;
            }
        }
        if (curValue >= TransactionDefinition.ISOLATION_DEFAULT &&
            curValue <= TransactionDefinition.ISOLATION_SERIALIZABLE) {
            return curValue;
        }
        return TransactionDefinition.ISOLATION_DEFAULT;
    }
}
