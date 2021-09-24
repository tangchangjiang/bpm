package org.o2.metadata.console.api.co;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页展示对象
 *
 * @author yipeng.zhu@hand-china.com 2021-09-16
 */
@Data
public class PageCO<E> {
    private Integer totalPages;
    private Integer totalElements;
    private Integer numberOfElements;
    private Integer size;
    private Integer number;
    private List<E> content;

    public PageCO() {
        this.content = new ArrayList();
    }

    public PageCO(List<E> content, Integer page,Integer size, Integer total) {
        this.content = content;
        this.number = page;
        this.size = size;
        this.totalElements = total;
        this.totalPages = this.size > 0 ? (total - 1) / this.size + 1 : 0;
        this.numberOfElements = content.size();
    }
}
