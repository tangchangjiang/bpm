package org.o2.metadata.app.bo;

import lombok.Data;

import java.util.List;

/**
 * 运费模板传输对象
 *
 * @author peng.xu@hand-china.com 2019-06-19
 */
@Data
public class FreightTemplateBO {

    /**
     * 运费模板
     */
    private FreightBO freight;

    /**
     * 运费模板明细列表
     */
    private List<FreightDetailBO> freightDetailList;
}
