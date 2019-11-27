/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 */

package org.o2.metadata.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hzero.mybatis.domian.SecurityToken;
import org.o2.ext.metadata.domain.entity.AddressMapping;

import java.util.List;
import java.util.Objects;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@ApiModel("地区父子关系视图")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class RegionTreeChildVO extends AddressMapping implements Comparable<RegionTreeChildVO> {

    /**
     * 地区父节点id
     */
    private Long parentRegionId;

    /**
     * 地区子节点集合
     */
    private List<RegionTreeChildVO> children;

    private String levelPath;

    @Override
    public boolean equals(final Object o) {
        if (o instanceof RegionTreeChildVO) {
            return ((RegionTreeChildVO) o).getRegionId().longValue() == this.getRegionId().longValue();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parentRegionId, children);
    }

    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return AddressMapping.class;
    }

    @Override
    public int compareTo(final RegionTreeChildVO o) {
        return this.getRegionId().intValue() - o.getRegionId().intValue();
    }
}
