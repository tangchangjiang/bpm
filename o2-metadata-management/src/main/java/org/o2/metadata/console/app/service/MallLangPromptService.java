package org.o2.metadata.console.app.service;

import io.choerodon.core.domain.Page;
import org.o2.core.response.BatchResponse;
import org.o2.metadata.console.infra.entity.MallLangPrompt;

import java.util.List;


/**
 * 商城前端多语言内容维护表应用服务
 *
 * @author changjiang.tang@hand-china.com 2021-08-05 09:57:27
 */
public interface MallLangPromptService {


    /**
     * 批量保存商城前端多语言内容维护表
     *
     * @param mallLangPromptList 商城前端多语言内容维护表对象列表
     * @return 商城前端多语言内容维护表对象列表
     */
    List<MallLangPrompt> batchSave(List<MallLangPrompt> mallLangPromptList);


    /**
     * 保存商城前端多语言内容维护表
     *
     * @param mallLangPrompt 商城前端多语言内容维护表对象
     * @return 商城前端多语言内容维护表对象
     */
    MallLangPrompt save(MallLangPrompt mallLangPrompt);

    /**
     * 商城前端多语言 发布
     *
     * @param mallLangPromptList 商城前端多语言表
     */
    BatchResponse<MallLangPrompt> release(List<MallLangPrompt> mallLangPromptList, Long tenantId);

    /**
     * 编码与名称转换
     */
    void list(Page<MallLangPrompt> list, Long organizationId);
}
