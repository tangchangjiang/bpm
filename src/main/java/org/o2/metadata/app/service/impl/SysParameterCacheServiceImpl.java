package org.o2.metadata.app.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.app.bo.SysParameterBO;
import org.o2.metadata.app.service.SysParameterCacheService;
import org.o2.metadata.infra.constants.MetadataConstants;

/**
 * 系统参数默认缓存实现
 *
 * @author mark.bao@hand-china.com 2019-05-30
 */
public class SysParameterCacheServiceImpl implements SysParameterCacheService {
    private final RedisCacheClient redisCacheClient;

    public SysParameterCacheServiceImpl(final RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public void saveSysParameter(final SysParameterBO sysParameter) {
        if (sysParameter == null) {
            return;
        }
        final String cacheKey = MetadataConstants.SysParameterCache.sysParameterKey(sysParameter.getParameterCode());
        this.redisCacheClient.opsForValue().set(cacheKey, FastJsonHelper.objectToString(sysParameter));
    }

    @Override
    public void deleteSysParameter(final String code) {
        if (StringUtils.isBlank(code)) {
            return;
        }
        final String cacheKey = MetadataConstants.SysParameterCache.sysParameterKey(code);
        this.redisCacheClient.delete(cacheKey);
    }

    @Override
    public SysParameterBO getSysParameter(final String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        final String cacheKey = MetadataConstants.SysParameterCache.sysParameterKey(code);
        final String results = this.redisCacheClient.opsForValue().get(cacheKey);
        if (StringUtils.isBlank(results)) {
            return null;
        }
        return FastJsonHelper.stringToObject(results, SysParameterBO.class);
    }

    @Override
    public String getSysParameterValue(final String code) {
        final SysParameterBO sysParameterBo = getSysParameter(code);
        if (sysParameterBo == null) {
            return null;
        } else {
            return sysParameterBo.getParameterValue();
        }
    }

    @Override
    public boolean isSysParameterActive(final String code) {
        final SysParameterBO sysParameterBo = getSysParameter(code);
        if (sysParameterBo == null) {
            return false;
        } else {
            return BaseConstants.Flag.YES.equals(sysParameterBo.getIsActive());
        }
    }
}
