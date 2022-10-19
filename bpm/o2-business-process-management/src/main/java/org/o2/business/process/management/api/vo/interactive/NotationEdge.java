package org.o2.business.process.management.api.vo.interactive;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/28 15:45
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NotationEdge extends Cell {

    private Integer zIndex;

    private TerminalData source;

    private TerminalData target;

    private Condition data;

    @Data
    public class Condition{

        private String key;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }
    }
}
