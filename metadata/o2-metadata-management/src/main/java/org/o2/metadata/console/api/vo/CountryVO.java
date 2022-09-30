package org.o2.metadata.console.api.vo;

import io.choerodon.mybatis.annotation.ModifyAudit;
import io.choerodon.mybatis.annotation.MultiLanguage;
import io.choerodon.mybatis.annotation.MultiLanguageField;
import io.choerodon.mybatis.annotation.VersionAudit;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @author tingting.wang@hand-china.com 2019-3-25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@VersionAudit
@ModifyAudit
@MultiLanguage
public class CountryVO {

    @ApiModelProperty("国家编码")
    private String countryCode;

    @ApiModelProperty("国家名称")
    @MultiLanguageField
    private String countryName;

}
