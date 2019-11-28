package org.o2.metadata.app.service;

import org.o2.metadata.app.bo.SysParameterBO;

/**
 * 系统参数缓存
 *
 * @author mark.bao@hand-china.com 2019-05-30
 */
public interface SysParameterCacheService {

    /**
     * 保存系统参数缓存
     *
     * @param sysParameterBO 系统参数业务实体
     */
    void saveSysParameter(SysParameterBO sysParameterBO);

    /**
     * 删除系统参数缓存
     *
     * @param code 系统参数编码
     */
    void deleteSysParameter(String code);

    /**
     * 根据系统参数编码获取实体
     *
     * @param code 系统参数编码
     * @return 系统参数业务实体
     */
    SysParameterBO getSysParameter(String code);

    /**
     * 根据系统参数编码获取参数值
     *
     * @param code 系统参数编码
     * @return 系统参数值
     */
    String getSysParameterValue(String code);

    /**
     * 根据系统参数编码判断参数是否有效
     *
     * @param code 系统参数编码
     * @return true 有效 false 无效
     */
    boolean isSysParameterActive(String code);
}
