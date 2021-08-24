package org.o2.metadata.console.infra.mapper;

        import io.choerodon.mybatis.common.BaseMapper;
        import org.apache.ibatis.annotations.Param;
        import org.o2.metadata.console.api.dto.CatalogRelVersionQueryDTO;
        import org.o2.metadata.console.infra.entity.CatalogVersion;

        import java.util.List;

/**
 * 版本目录Mapper
 *
 * @author jiu.yang@hand-china.com 2019-12-02 15:33:52
 */
public interface CatalogVersionMapper extends BaseMapper<CatalogVersion> {

    /**
     * 通过编码查询目录版本
     * @param  catalogVersionCodes 目录版本编码
     * @param  tenantId 租户ID
     * @return 目录版本
     */
    List<CatalogVersion> batchSelectByCodes(@Param(value = "catalogVersionCodes") List<String> catalogVersionCodes,
                                            @Param("tenantId") Long tenantId);

    /**
     * 查询目录&目录版本
     * @param queryDTO 入参
     * @return  list
     */
    List<CatalogVersion> catalogRelVersion(CatalogRelVersionQueryDTO queryDTO);
}
