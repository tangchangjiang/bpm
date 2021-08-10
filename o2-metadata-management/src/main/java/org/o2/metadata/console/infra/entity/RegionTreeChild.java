/*
 * Copyright (C) HAND Enterprise Solutions Company Ltd.
 */

package org.o2.metadata.console.infra.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hzero.mybatis.domian.SecurityToken;

import java.util.List;
import java.util.Objects;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@ApiModel("地区父子关系视图")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class RegionTreeChild extends AddressMapping implements Comparable<RegionTreeChild> {

    /**
     * 地区父节点id
     */
    private String parentRegionCode;

    /**
     * 地区子节点集合
     */
    private List<RegionTreeChild> children;

    private String levelPath;

    private Long regionId;


    @Override
    public boolean equals(Object o) {
        if (o instanceof RegionTreeChild) {
            return ((RegionTreeChild) o).getRegionCode().equals(this.getRegionCode());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parentRegionCode, children);
    }

    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return AddressMapping.class;
    }

    @Override
    public int compareTo(final RegionTreeChild o) {
        if (null == this.getRegionId()){
            return 0;
        }
        return this.getRegionId().intValue() - o.getRegionId().intValue();
    }
}
