package org.o2.metadata.console.app.job;

import io.choerodon.mybatis.helper.LanguageHelper;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.scheduler.infra.tool.SchedulerTool;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.util.Pair;
import org.hzero.mybatis.domian.Language;
import org.o2.core.O2CoreConstants;
import org.o2.core.thread.ThreadJobPojo;
import org.o2.metadata.console.app.service.LovAdapterService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.scheduler.job.AbstractBatchThreadJob;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 元数据job抽象类
 *
 * @author chao.yang05@hand-china.com 2023-05-19
 */
public abstract class AbstractMetadataBatchThreadJob extends AbstractBatchThreadJob<Long> {

    private final LovAdapterService lovAdapterService;

    protected AbstractMetadataBatchThreadJob(LovAdapterService lovAdapterService) {
        this.lovAdapterService = lovAdapterService;
    }

    @Override
    public Pair<Boolean, List<Long>> prepareExecuteData(SchedulerTool tool, int times, ThreadJobPojo threadJobPojo) {
        Map<String, String> jobParams = Optional.ofNullable(threadJobPojo.getJobParams()).orElse(new HashMap<>());
        String tenantId = jobParams.get(O2CoreConstants.EntityDomain.FIELD_TENANT_ID);
        // 如果设置了租户Id，则不查询所有租户
        if (StringUtils.isNotBlank(tenantId)) {
            return new Pair<>(Boolean.TRUE, Collections.singletonList(Long.parseLong(tenantId)));
        }
        int page = (times - BaseConstants.Digital.ONE);
        int size = Math.toIntExact(threadJobPojo.getBatchSize());
        List<Map<String, Object>> tenantResult = lovAdapterService.queryLovValueMeaning(BaseConstants.DEFAULT_TENANT_ID, "O2MD.ALL_TENANT_ID", page, size, new HashMap<>());
        List<Long> tenantIds = tenantResult.stream().map(tenant
                -> Long.valueOf(String.valueOf(tenant.get(O2CoreConstants.EntityDomain.FIELD_TENANT_ID)))).collect(Collectors.toList());
        return new Pair<>(Boolean.TRUE, tenantIds);
    }

    /**
     * 获取多语言
     * @param threadJobPojo 参数job
     * @return 多语言集合
     */
    public  List<String> getLang(ThreadJobPojo threadJobPojo) {
        Map<String, String> map = Optional.ofNullable(threadJobPojo.getJobParams()).orElse(new HashMap<>());
        String langStr = map.get(MetadataConstants.RefreshJobConstants.LANG);
        List<String> langs = new ArrayList<>(16);
        if(StringUtils.isNotBlank(langStr)) {
            langs = Arrays.asList(langStr.split(BaseConstants.Symbol.COMMA));
        }
        if (CollectionUtils.isEmpty(langs)) {
            List<Language> languages = LanguageHelper.languages();
            for (Language language : languages) {
                langs.add(language.getCode());
            }
        }
        return langs;
    }
}
