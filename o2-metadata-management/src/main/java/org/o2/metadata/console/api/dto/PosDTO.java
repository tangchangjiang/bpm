package org.o2.metadata.console.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hzero.mybatis.domian.SecurityToken;
import org.o2.metadata.console.infra.entity.Pos;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */

@ApiModel("服务点视图")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@EqualsAndHashCode(callSuper = true)
public class PosDTO extends Pos {

    @Override
    public Class<? extends SecurityToken> associateEntityClass() {
        return Pos.class;
    }
}
