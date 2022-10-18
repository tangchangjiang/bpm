package org.o2.rule.engine.management.app.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.delay.queue.entity.DelayMessage;
import org.o2.delay.queue.service.impl.AbstractDelayMessageListener;
import org.o2.rule.engine.management.domain.entity.Rule;
import org.o2.rule.engine.management.domain.repository.RuleRepository;
import org.o2.rule.engine.management.infra.constants.RuleEngineConstants;
import org.o2.rule.engine.management.infra.constants.RuleEngineRedisConstants;
import org.springframework.stereotype.Component;


/**
 * @author xiang.zhao@hand-china.com 2022/10/15
 */
@Component("ruleAutoExpireListener")
@Slf4j
public class RuleAutoExpireListener extends AbstractDelayMessageListener<DelayMessage> {

    private final RuleRepository ruleRepository;

    public RuleAutoExpireListener(final RuleRepository ruleRepository) {
        this.ruleRepository = ruleRepository;
    }

    @Override
    public String getTopic() {
        return RuleEngineRedisConstants.RedisKey.SCENE_EXPIRE;
    }

    @Override
    public void onMessage(DelayMessage delayMessage) {
        final String message = delayMessage.getMessage();
        if (StringUtils.isBlank(message)) {
            log.error("[rule scene expire delayMessage] consumer failed : message is null");
            return;
        }

        if (log.isInfoEnabled()) {
            log.info("[ruleSceneAutoExpireListener] delayMessage:{}", message);
        }

        final String[] args = message.split(BaseConstants.Symbol.COLON);
        final Long tenantId = Long.valueOf(args[0]);
        final String ruleCode = args[1];

        final Rule query = new Rule();
        query.setRuleCode(ruleCode);
        query.setTenantId(tenantId);

        final Rule rule = ruleRepository.selectOne(query);
        rule.setRuleStatus(RuleEngineConstants.RuleStatus.INVALID);
        ruleRepository.updateOptional(rule, Rule.FIELD_RULE_STATUS);
    }
}
