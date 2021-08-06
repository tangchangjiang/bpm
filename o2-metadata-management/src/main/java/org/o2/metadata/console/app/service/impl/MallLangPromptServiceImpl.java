package org.o2.metadata.console.app.service.impl;

import com.google.common.base.Joiner;
import io.choerodon.core.exception.CommonException;
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
import org.o2.metadata.console.app.service.MallLangPromptService;
import org.o2.metadata.console.infra.constant.MetadataConstants;
import org.o2.metadata.console.infra.constant.SystemParameterConstants;
import org.o2.metadata.console.infra.entity.MallLangPrompt;
import org.o2.metadata.console.infra.entity.StaticResource;
import org.o2.metadata.console.infra.repository.MallLangPromptRepository;
import org.o2.metadata.console.infra.repository.StaticResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public MallLangPromptServiceImpl(StaticResourceRepository staticResourceRepository, MallLangPromptRepository mallLangPromptRepository,
                                     LockService lockService, FileStorageProperties fileStorageProperties,
                                     FileClient fileClient, RedisCacheClient redisCacheClient){
        this.mallLangPromptRepository = mallLangPromptRepository;
        this.fileStorageProperties = fileStorageProperties;
        this.staticResourceRepository = staticResourceRepository;
        this.redisCacheClient = redisCacheClient;
        this.lockService = lockService;
        this.fileClient = fileClient;
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
                // TODO: 唯一性校验
                UniqueHelper.valid(item,MallLangPrompt.O2MD_MALL_LANG_PROMPT_U1);
                mallLangPromptRepository.updateByPrimaryKeySelective(item);
            });
        }
        // 新增
        if (statusMap.containsKey(AuditDomain.RecordStatus.create)) {
            List<MallLangPrompt> createList = statusMap.get(AuditDomain.RecordStatus.create);
            createList.forEach(item -> {
                // TODO: 唯一性校验
                UniqueHelper.valid(item,MallLangPrompt.O2MD_MALL_LANG_PROMPT_U1);
                mallLangPromptRepository.insertSelective(item);
            });
        }
        return mallLangPromptList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public MallLangPrompt save(MallLangPrompt mallLangPrompt) {
        //保存商城前端多语言内容维护表
        UniqueHelper.valid(mallLangPrompt,MallLangPrompt.O2MD_MALL_LANG_PROMPT_U1);
        if (mallLangPrompt.getLangPromptId() == null) {
            mallLangPromptRepository.insertSelective(mallLangPrompt);
        } else {
            mallLangPromptRepository.updateOptional(mallLangPrompt,
                    MallLangPrompt.FIELD_LANG,
                    MallLangPrompt.FIELD_PROMPT_DETAIL,
                    MallLangPrompt.FIELD_MALL_TYPE,
                    MallLangPrompt.FIELD_TENANT_ID,
                    MallLangPrompt.FIELD_STATUS
            );
        }

        return mallLangPrompt;
    }

    @Override
    public BatchResponse<MallLangPrompt> release(List<MallLangPrompt> mallLangPromptList) {
        BatchResponse<MallLangPrompt> batchResponse = new BatchResponse<>();
        List<String> errorMsg = new ArrayList<>();
        for(MallLangPrompt mallLangPrompt : mallLangPromptList){
            final LockData lockData = new LockData(MetadataConstants.MallLangPromptConstants.MALL_LANG_LOCK_KEY, 0L,
                    3000L, TimeUnit.MILLISECONDS, LockType.FAIR);
            lockService.lock(lockData, () -> releaseProcess(mallLangPrompt,errorMsg,batchResponse),null);
        }
        //详情页面校验报错
        if (mallLangPromptList.size() == 1 && CollectionUtils.isNotEmpty(errorMsg)) {
            StringBuilder stringBuilder = new StringBuilder();
            errorMsg.forEach(eMsg -> stringBuilder.append(MessageAccessor.getMessage(eMsg)).append("\r\n"));
            throw new CommonException(stringBuilder.toString());
        }
        return batchResponse;
    }


    private void releaseProcess(MallLangPrompt mallLangPrompt, List<String> errorMsg, BatchResponse<MallLangPrompt> batchResponse){
        StaticResource resource = new StaticResource();
        //上传静态文件
        if(!uploadStaticFile(mallLangPrompt,resource,errorMsg)){
            batchResponse.addFailedBody(mallLangPrompt);
            return;
        }
        //保存发布记录
        saveMallLangPromptInfo(mallLangPrompt,resource);
        batchResponse.addSuccessBody(mallLangPrompt);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveMallLangPromptInfo(MallLangPrompt mallLangPrompt,StaticResource resource){
        StaticResource request = new StaticResource();
        request.setLang(resource.getLang());
        request.setTenantId(resource.getTenantId());
        request.setResourceCode(resource.getResourceCode());
        List<StaticResource> staticResource = staticResourceRepository.select(request);
        if(staticResource.isEmpty()){
            staticResourceRepository.insertSelective(resource);
        }else{
            resource.setResourceId(staticResource.get(0).getResourceId());
            resource.setObjectVersionNumber(staticResource.get(0).getObjectVersionNumber());
            staticResourceRepository.updateByPrimaryKeySelective(resource);
        }
        mallLangPromptRepository.updateOptional(mallLangPrompt, MallLangPrompt.FIELD_STATUS);
    }


    /**
     * 静态文件上传
     */
    private boolean uploadStaticFile(MallLangPrompt mallLangPrompt, StaticResource resource, List<String> errorMsg) {
        final Long tenantId = mallLangPrompt.getTenantId();
        final String fileName = mallLangPrompt.getMallType() + "_" + mallLangPrompt.getLang();
        final String bucketCode = fileStorageProperties.getBucketCode();
        final String storageCode = fileStorageProperties.getStorageCode();
        final String jsonFile = mallLangPrompt.getPromptDetail();
        // 上传路径全小写
        String directory = fileStorageProperties.getStoragePath() + Joiner.on(BaseConstants.Symbol.SLASH).skipNulls().join(
                 MetadataConstants.MallLangPromptConstants.NAME,mallLangPrompt.getMallType(),
                mallLangPrompt.getLang()).toLowerCase();
        // 下载
        String key = String.format(SystemParameterConstants.Redis.KEY, tenantId, SystemParameterConstants.ParamType.KV);
        Object valueObj = redisCacheClient.opsForHash().get(key, SystemParameterConstants.FileConfig.FILE_PREFIX);
        log.info("redis systemParameter file_prefix ({}), ({})", key, valueObj);
        if (valueObj == null) {
            return false;
        }
        String urlPrefix = String.valueOf(valueObj);
        String relativeUrl = directory + BaseConstants.Symbol.SLASH + tenantId + BaseConstants.Symbol.SLASH + fileName + SystemParameterConstants.FileConfig.FILE_SUFFIX_JSON;
        String url = urlPrefix + relativeUrl;
        log.info("download inputStream url ({}) ({}) ({})", tenantId, bucketCode, url);
        // 上传/新增
        try {
            fileClient.uploadFile(tenantId, bucketCode, directory,
                    fileName + SystemParameterConstants.FileConfig.FILE_SUFFIX_JSON, SystemParameterConstants.FileConfig.FILE_JSON_TYPE,
                    storageCode, jsonFile.getBytes());
        }catch (Exception e){
            errorMsg.add(MetadataConstants.ErrorCode.STATIC_FILE_UPLOAD_FAIL);
            return false;
        }
        //记录上传文件信息
        resource.setResourceCode(MetadataConstants.Constants.MODE_NAME + "_" + MetadataConstants.MallLangPromptConstants.RESOURCE_CODE + "_" + fileName);
        resource.setSourceModuleCode(MetadataConstants.Constants.MODE_NAME);
        resource.setResourceUrl(relativeUrl);
        resource.setDescription(MessageAccessor.getMessage(MetadataConstants.MallLangPromptConstants.DESCRIPTION).getDesc());
        resource.setLang(mallLangPrompt.getLang());
        resource.setTenantId(tenantId);
        return true;
    }

}
