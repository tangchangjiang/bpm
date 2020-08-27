package org.o2.metadata.app.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.hzero.core.base.BaseConstants;
import org.o2.context.metadata.vo.SysParameterVO;
import org.o2.core.helper.FastJsonHelper;
import org.o2.data.redis.client.RedisCacheClient;
import org.o2.metadata.app.service.SysParameterService;
import org.o2.metadata.core.infra.constants.MetadataConstants;
import org.springframework.stereotype.Service;

/**
 * SysParameter RPC Provider
 *
 * @author mark.bao@hand-china.com 2019/11/29
 */
@Service
public class SysParameterServiceImpl implements SysParameterService {
    private final RedisCacheClient redisCacheClient;

    public SysParameterServiceImpl(final RedisCacheClient redisCacheClient) {
        this.redisCacheClient = redisCacheClient;
    }

    @Override
    public void saveSysParameter(final SysParameterVO sysParameter) {
        if (sysParameter == null) {
            return;
        }
        final String cacheKey = MetadataConstants.SysParameterCache.sysParameterKey(sysParameter.getParameterCode(), sysParameter.getTenantId());
        this.redisCacheClient.opsForValue().set(cacheKey, FastJsonHelper.objectToString(sysParameter));
    }

    @Override
    public void deleteSysParameter(final String code, Long tenantId) {
        if (StringUtils.isBlank(code)) {
            return;
        }
        final String cacheKey = MetadataConstants.SysParameterCache.sysParameterKey(code, tenantId);
        this.redisCacheClient.delete(cacheKey);
    }

    @Override
    public SysParameterVO getSysParameter(final String code, Long tenantId) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        final String cacheKey = MetadataConstants.SysParameterCache.sysParameterKey(code, tenantId);
        final String results = this.redisCacheClient.opsForValue().get(cacheKey);
        if (StringUtils.isBlank(results)) {
            return null;
        }
        return FastJsonHelper.stringToObject(results, SysParameterVO.class);
    }

    @Override
    public String getSysParameterValue(final String code, Long tenantId) {
        final SysParameterVO sysParameterVO = getSysParameter(code, tenantId);
        if (sysParameterVO == null) {
            return null;
        } else {
            return sysParameterVO.getParameterValue();
        }
    }

    @Override
    public boolean isSysParameterActive(final String code, Long tenantId) {
        final SysParameterVO sysParameterVO = getSysParameter(code, tenantId);
        if (sysParameterVO == null) {
            return false;
        } else {
            return BaseConstants.Flag.YES.equals(sysParameterVO.getActiveFlag());
        }
    }
}
