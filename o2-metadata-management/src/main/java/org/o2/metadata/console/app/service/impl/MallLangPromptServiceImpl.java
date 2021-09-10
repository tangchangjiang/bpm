package org.o2.metadata.console.app.service.impl;

import com.google.common.base.Joiner;
import io.choerodon.core.domain.Page;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.choerodon.mybatis.domain.AuditDomain;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.hzero.boot.file.FileClient;
import org.hzero.core.base.BaseConstants;
import org.hzero.core.message.MessageAccessor;
import org.hzero.mybatis.helper.UniqueHelper;
import org.o2.core.file.FileStorageProperties;
import org.o2.core.response.BatchResponse;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.lock.app.service.LockService;
import org.o2.lock.domain.data.LockData;
import org.o2.lock.domain.data.LockType;
import org.o2.lov.app.service.HzeroLovQueryService;
import org.o2.metadata.console.api.dto.StaticResourceConfigDTO;
import org.o2.metadata.console.api.dto.StaticResourceSaveDTO;
import org.o2.metadata.console.app.service.MallLangPromptService;
import org.o2.metadata.console.app.service.StaticResourceConfigService;
import org.o2.metadata.console.app.service.StaticResourceInternalService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.SystemParameterConstants;
import org.o2.metadata.console.infra.entity.MallLangPrompt;
import org.o2.metadata.console.infra.entity.StaticResource;
import org.o2.metadata.console.infra.entity.StaticResourceConfig;
import org.o2.metadata.console.infra.repository.MallLangPromptRepository;
import org.o2.metadata.console.infra.repository.StaticResourceRepository;
import org.o2.user.helper.IamUserHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 商城前端多语言内容维护表应用服务默认实现
 *
 * @author changjiang.tang@hand-china.com 2021-08-05 09:57:27
 */
@Slf4j
@Service
public class MallLangPromptServiceImpl implements MallLangPromptService {

    private final MallLangPromptRepository mallLangPromptRepository;

    private final FileStorageProperties fileStorageProperties;

    private final StaticResourceRepository staticResourceRepository;

    private final RedisCacheClient redisCacheClient;

    private final LockService lockService;

    private final FileClient fileClient;

    private final StaticResourceInternalService staticResourceInternalService;

    private final StaticResourceConfigService staticResourceConfigService;

    private final HzeroLovQueryService hzeroLovQueryService;

    public MallLangPromptServiceImpl(StaticResourceRepository staticResourceRepository, MallLangPromptRepository mallLangPromptRepository,
                                     LockService lockService, FileStorageProperties fileStorageProperties,
                                     FileClient fileClient, RedisCacheClient redisCacheClient,
                                     StaticResourceInternalService staticResourceInternalService,
                                     StaticResourceConfigService staticResourceConfigService, HzeroLovQueryService hzeroLovQueryService) {
        this.mallLangPromptRepository = mallLangPromptRepository;
        this.fileStorageProperties = fileStorageProperties;
        this.staticResourceRepository = staticResourceRepository;
        this.redisCacheClient = redisCacheClient;
        this.lockService = lockService;
        this.fileClient = fileClient;
        this.staticResourceInternalService = staticResourceInternalService;
        this.staticResourceConfigService = staticResourceConfigService;
        this.hzeroLovQueryService = hzeroLovQueryService;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MallLangPrompt> batchSave(List<MallLangPrompt> mallLangPromptList) {
        Map<AuditDomain.RecordStatus, List<MallLangPrompt>> statusMap = mallLangPromptList.stream().collect(Collectors.groupingBy(MallLangPrompt::get_status));
        // 删除
        if (statusMap.containsKey(AuditDomain.RecordStatus.delete)) {
            List<MallLangPrompt> deleteList = statusMap.get(AuditDomain.RecordStatus.delete);
            mallLangPromptRepository.batchDeleteByPrimaryKey(deleteList);
        }
        // 更新
        if (statusMap.containsKey(AuditDomain.RecordStatus.update)) {
            List<MallLangPrompt> updateList = statusMap.get(AuditDomain.RecordStatus.update);
            updateList.forEach(item -> {
                // 唯一性校验
                UniqueHelper.valid(item, MallLangPrompt.O2MD_MALL_LANG_PROMPT_U1);
                item.setStatus(MetadataConstants.MallLangPromptConstants.UNAPPROVED);
                mallLangPromptRepository.updateByPrimaryKeySelective(item);
            });
        }
        // 新增
        if (statusMap.containsKey(AuditDomain.RecordStatus.create)) {
            List<MallLangPrompt> createList = statusMap.get(AuditDomain.RecordStatus.create);
            createList.forEach(item -> {
                // 唯一性校验
                UniqueHelper.valid(item, MallLangPrompt.O2MD_MALL_LANG_PROMPT_U1);
                mallLangPromptRepository.insertSelective(item);
            });
        }
        return mallLangPromptList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public MallLangPrompt save(MallLangPrompt mallLangPrompt) {
        //保存商城前端多语言内容维护表
        UniqueHelper.valid(mallLangPrompt, MallLangPrompt.O2MD_MALL_LANG_PROMPT_U1);
        if (mallLangPrompt.getLangPromptId() == null) {
            mallLangPromptRepository.insertSelective(mallLangPrompt);
        } else {
            mallLangPrompt.setStatus(MetadataConstants.MallLangPromptConstants.UNAPPROVED);
            mallLangPromptRepository.updateOptional(mallLangPrompt,
                    MallLangPrompt.FIELD_LANG,
                    MallLangPrompt.FIELD_PROMPT_DETAIL,
                    MallLangPrompt.FIELD_MALL_TYPE,
                    MallLangPrompt.FIELD_TENANT_ID,
                    MallLangPrompt.FIELD_STATUS,
                    MallLangPrompt.FIELD_DESCRIPTION,
                    MallLangPrompt.FIELD_SITE_RANG
            );
        }
        return mallLangPrompt;
    }

    @Override
    public BatchResponse<MallLangPrompt> release(List<MallLangPrompt> mallLangPromptList) {
        BatchResponse<MallLangPrompt> batchResponse = new BatchResponse<>();
        List<String> errorMsg = new ArrayList<>();
        for (MallLangPrompt mallLangPrompt : mallLangPromptList) {
            //获取静态资源
            StaticResourceConfigDTO staticResourceConfigDTO = new StaticResourceConfigDTO();
            staticResourceConfigDTO.setResourceCode(MetadataConstants.MallLangPromptConstants.PCM_LANG_PROMPT);
            staticResourceConfigDTO.setTenantId(mallLangPrompt.getTenantId());
            List<StaticResourceConfig> listStaticResourceConfig = staticResourceConfigService.listStaticResourceConfig(staticResourceConfigDTO);
            for (StaticResourceConfig staticResourceConfig : listStaticResourceConfig) {
                final LockData lockData = new LockData(MetadataConstants.MallLangPromptConstants.MALL_LANG_LOCK_KEY, 0L,
                        3000L, TimeUnit.MILLISECONDS, LockType.FAIR);
                lockService.lock(lockData, () -> releaseProcess(mallLangPrompt, errorMsg, batchResponse, staticResourceConfig), null);
            }
            mallLangPrompt.setStatus(MetadataConstants.MallLangPromptConstants.APPROVED);
            mallLangPromptRepository.updateOptional(mallLangPrompt, MallLangPrompt.FIELD_STATUS);
        }
        //详情页面校验报错
        if (mallLangPromptList.size() == 0 && CollectionUtils.isNotEmpty(errorMsg)) {
            StringBuilder stringBuilder = new StringBuilder();
            errorMsg.forEach(eMsg -> stringBuilder.append(MessageAccessor.getMessage(eMsg)).append("\r\n"));
            throw new CommonException(stringBuilder.toString());
        }
        return batchResponse;
    }

    @Override
    public void list(Page<MallLangPrompt> list, Long organizationId) {
        List<MallLangPrompt> content = list.getContent();

        List<String> updateUserIds = content.stream().filter(i -> i.getLastUpdatedBy() != null)
                .map(a -> a.getLastUpdatedBy().toString()).collect(Collectors.toList());
        List<String> createUserIds = content.stream().filter(i -> i.getCreatedBy() != null)
                .map(a -> a.getCreatedBy().toString()).collect(Collectors.toList());
        List<String> siteRangs = content.stream().filter(i -> i.getSiteRang() != null)
                .map(a -> a.getSiteRang()).collect(Collectors.toList());

        Map<Long, String> updateUserMap = IamUserHelper.getRealNameMap(updateUserIds);
        Map<Long, String> createUserMap = IamUserHelper.getRealNameMap(createUserIds);

        List<Map<String, Object>> maps = hzeroLovQueryService.queryLovValueMeaning(2L, "O2CMS.SITE", new HashMap<>());
        Map<String, Object> siteRangName = new HashMap<>();
        for (String siteCode : siteRangs) {
            String[] result = siteCode.split(",");
            for (String siteCodeResult : result) {
                for (Map<String, Object> s : maps) {
                    if (siteCodeResult.equals(s.get("siteCode"))) {
                        siteRangName.put(siteCodeResult, s.get("siteName"));
                    }
                }
            }
        }
        list.getContent().forEach(
                prompt -> {
                    prompt.setLastUpdatedByName(updateUserMap.get(prompt.getLastUpdatedBy()));
                    prompt.setCreatedByName(createUserMap.get(prompt.getCreatedBy()));
                    prompt.setSiteRangForName(siteRangName);
                }
        );
    }


    private void releaseProcess(MallLangPrompt mallLangPrompt, List<String> errorMsg, BatchResponse<MallLangPrompt> batchResponse, StaticResourceConfig staticResourceConfig) {
        StaticResource resource = new StaticResource();
        //上传静态文件
        if (!uploadStaticFile(mallLangPrompt, resource, errorMsg, staticResourceConfig)) {
            batchResponse.addFailedBody(mallLangPrompt);
            return;
        }
        //保存发布记录
        saveMallLangPromptInfo(mallLangPrompt, resource);
        batchResponse.addSuccessBody(mallLangPrompt);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveMallLangPromptInfo(MallLangPrompt mallLangPrompt, StaticResource resource) {
        String resourceOwner = resource.getResourceOwner();
        String[] result = resourceOwner.split(",");
        for (String r : result) {
            resource.setResourceOwner(r);
            StaticResourceSaveDTO request = new StaticResourceSaveDTO();
            request.setLang(resource.getLang());
            request.setTenantId(resource.getTenantId());
            request.setResourceCode(resource.getResourceCode());
            request.setDescription(resource.getDescription());
            request.setResourceUrl(resource.getResourceUrl());
            request.setResourceOwner(r);
            request.setResourceLevel(resource.getResourceLevel());
            request.setResourceHost(resource.getResourceHost());
            staticResourceInternalService.saveResource(request);
        }
        mallLangPromptRepository.updateOptional(mallLangPrompt, MallLangPrompt.FIELD_STATUS);
    }


    /**
     * 静态文件上传
     */
    private boolean uploadStaticFile(MallLangPrompt mallLangPrompt, StaticResource resource, List<String> errorMsg, StaticResourceConfig staticResourceConfig) {
        final Long tenantId = mallLangPrompt.getTenantId();
        final String fileName = mallLangPrompt.getMallType() + "_" + mallLangPrompt.getLang();
        final String bucketCode = fileStorageProperties.getBucketCode();
        final String storageCode = fileStorageProperties.getStorageCode();
        final String jsonFile = mallLangPrompt.getPromptDetail();
        // 上传路径全小写
        String directory = fileStorageProperties.getStoragePath() + Joiner.on(BaseConstants.Symbol.SLASH).skipNulls().join(
                MetadataConstants.MallLangPromptConstants.NAME, mallLangPrompt.getMallType(),
                mallLangPrompt.getLang()).toLowerCase();
        // 下载
        String key = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.KV);
        Object valueObj = redisCacheClient.opsForHash().get(key, SystemParameterConstants.FileConfig.FILE_PREFIX);

        log.info("redis systemParameter file_prefix ({}), ({})", key, valueObj);
        if (valueObj == null) {
            return false;
        }
        // 上传/新增
        try {
            String resultUrl = fileClient.uploadFile(tenantId, bucketCode, directory,
                    fileName + SystemParameterConstants.FileConfig.FILE_SUFFIX_JSON, SystemParameterConstants.FileConfig.FILE_JSON_TYPE,
                    storageCode, jsonFile.getBytes());
            resource.setResourceUrl(resultUrl);
        } catch (Exception e) {
            errorMsg.add(MetadataConstants.ErrorCode.STATIC_FILE_UPLOAD_FAIL);
            return false;
        }
        String domainAndUrl = trimDomainPrefix(resource.getResourceUrl());
        int indexOfSlash = domainAndUrl.indexOf(BaseConstants.Symbol.SLASH);
        //记录上传文件信息
        resource.setSourceModuleCode(MetadataConstants.Constants.MODE_NAME);

        resource.setTenantId(tenantId);
        resource.setJsonKey(staticResourceConfig.getJsonKey());
        resource.setResourceCode(MetadataConstants.MallLangPromptConstants.PCM_LANG_PROMPT);
        resource.setDescription(staticResourceConfig.getDescription());
        resource.setResourceLevel(staticResourceConfig.getResourceLevel());
        resource.setEnableFlag(1);
        resource.setLang(mallLangPrompt.getLang());
        resource.setLastUpdatedBy(DetailsHelper.getUserDetails().getUserId());
        if (!"PUBLIC".equals(staticResourceConfig.getResourceLevel())) {
            resource.setResourceOwner(mallLangPrompt.getSiteRang());
        }
        resource.setResourceUrl(domainAndUrl.substring(indexOfSlash));
        resource.setResourceHost("http://" + domainAndUrl.substring(0, indexOfSlash));
        return true;
    }

    /**
     * 裁剪掉域名 + 端口
     *
     * @param resourceUrl resourceUrl
     * @return result
     */
    private static String trimDomainPrefix(String resourceUrl) {
        if (org.apache.commons.lang3.StringUtils.isBlank(resourceUrl)) {
            return "";
        }
        String[] httpSplits = resourceUrl.split(BaseConstants.Symbol.DOUBLE_SLASH);
        if (httpSplits.length < 2) {
            return resourceUrl;
        }
        return httpSplits[1];
    }

}
