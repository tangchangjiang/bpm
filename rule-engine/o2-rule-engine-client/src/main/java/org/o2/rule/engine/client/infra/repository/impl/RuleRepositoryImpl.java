package org.o2.rule.engine.client.infra.repository.impl;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.hzero.core.base.BaseConstants;
import org.o2.cache.util.CollectionCacheHelper;
import org.o2.core.helper.O2ResponseUtils;
import org.o2.rule.engine.client.domain.entity.Rule;
import org.o2.rule.engine.client.domain.repository.RuleRepository;
import org.o2.rule.engine.client.domain.vo.RuleVO;
import org.o2.rule.engine.client.infra.constant.RuleClientConstants;
import org.o2.rule.engine.client.infra.convertor.RuleConvertor;
import org.o2.rule.engine.client.infra.feign.O2RuleRemoteService;
import org.springframework.http.ResponseEntity;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * 规则仓库实现类
 *
 * @author wei.cai@hand-china.com
 * @date 2022/10/13
 */
@Slf4j
public class RuleRepositoryImpl implements RuleRepository {
    private static final String CACHE_FORMAT = "RULE_INFO_CACHE:%d:%s";
    private final O2RuleRemoteService o2RuleRemoteService;

    /**
     * 构造器
     *
     * @param o2RuleRemoteService 客户端
     */
    public RuleRepositoryImpl(final O2RuleRemoteService o2RuleRemoteService) {
        this.o2RuleRemoteService = o2RuleRemoteService;
    }

    @Override
    public Rule findRuleByCode(Long tenantId, String code) {
        RuleVO ruleVO = findRuleByCodes(tenantId, Collections.singleton(code)).get(code);
        if (ruleVO == null) {
            ruleVO = findRuleByCodes(BaseConstants.DEFAULT_TENANT_ID, Collections.singleton(code)).get(code);
        }
        return RuleConvertor.convertToRule(ruleVO);
    }

    /**
     * 通过编码获取规则
     *
     * @param tenantId 租户ID
     * @param codes    编码
     * @return 返回map
     */
    protected Map<String, RuleVO> findRuleByCodes(Long tenantId, Collection<String> codes) {
        return CollectionCacheHelper.getCache(
                RuleClientConstants.CacheName.O2RE_RULE,
                k -> String.format(CACHE_FORMAT, tenantId, k),
                codes,
                sceneCodes -> realFindRuleByCodes(tenantId, sceneCodes)
        );
    }

    /**
     * 真实获取Entity方法
     *
     * @param tenantId 租户ID
     * @param codes    编码
     * @return 返回信息
     */
    protected Map<String, RuleVO> realFindRuleByCodes(Long tenantId, Collection<String> codes) {
        final Map<String, RuleVO> map = Maps.newHashMapWithExpectedSize(codes.size());

        for (String code : codes) {
            try {
                final ResponseEntity<String> ruleByCode = o2RuleRemoteService.findRuleByCode(tenantId, code);
                final RuleVO ruleVO = O2ResponseUtils.getResponse(ruleByCode, RuleVO.class);
                map.put(code, ruleVO);
            } catch (Exception e) {
                log.error("Get For Object Error.", e);
            }
        }
        return map;
    }

}
