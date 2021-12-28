package org.o2.metadata.management.client.domain.co;

import lombok.Data;

import java.util.List;

/**
 * 分页展示对象
 *
 * @author yipeng.zhu@hand-china.com 2021-09-16
 */
@Data
public class PageCO<E> {
    private Integer totalPages;
    private Long totalElements;
    private Integer numberOfElements;
    private Integer size;
    private Integer number;
    private List<E> content;
}
