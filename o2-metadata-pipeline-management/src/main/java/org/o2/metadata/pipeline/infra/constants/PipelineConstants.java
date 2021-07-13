package org.o2.metadata.pipeline.infra.constants;

import org.springframework.core.io.ClassPathResource;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * 流程管理器常量
 *
 * @author mark.bao@hand-china.com 2019-03-27
 */
public interface PipelineConstants {


    interface Redis {
        String PIPELINE_KEY = "o2pl:pipeline:%s";
        String PIPELINE_NODE_INFO = "info";
        String PIPELINE_VERSION = "pipeline_version";
        long EXPIRE_TIME_MINUTES = 3;
        ResourceScriptSource PIPELINE_CONF_UPDATE_LUA =
                new ResourceScriptSource(new ClassPathResource("script/lua/pipeline_conf_update.lua"));

    }

    /**
     * 错误信息
     */
    interface ErrorCode {
        /**
         * 未找到该流程器
         */
        String PIPELINE_NOT_FIND = "pipeline.not_find";
        /**
         * 流程器已存在
         */
        String PIPELINE_ALREADY_EXISTS = "pipeline.already_exists";
        /**
         * 上传失败，确认yaml格式是否正确
         */
        String PIPELINE_UPLOAD_ERROR = "pipeline.upload_error";
    }

    /**
     * 提示信息
     */
    interface Message {
        /**
         * 文件为空
         */
        String PIPELINE_FILE_IS_NULL = "pipeline.file_is_null";
        /**
         * 导入成功
         */
        String PIPELINE_UPLOAD_SUCCESS = "pipeline.upload_success";
        /**
         * 文件名不能为空
         */
        String PIPELINE_FILENAME_IS_NULL = "pipeline.filename_is_null";
        /**
         * 没有找到任何流程器数据
         */
        String PIPELINE_NOT_FOUND = "pipeline.not_fount";
        /**
         * 流水线编码不可重复，成功保存N条数据
         */
        String PIPELINE_SUCCESS_NUM = "pipeline.success_num";
        /**
         * 下个节点行为和决策类型不可重复，成功保存N条数据
         */
        String PIPELINE_NODE_SUCCESS_NUM = "pipeline.node_success_num";
    }

    /**
     * 展示类型
     */
    interface DisplayType{
        String LOV_CODE = "O2EXT.DISPLAY_TYPE";
    }
}
