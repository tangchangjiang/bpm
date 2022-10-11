package org.o2.process.domain.engine.definition.event;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.o2.process.domain.engine.definition.BaseNode;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/9/28 15:04
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BaseEvent extends BaseNode {
}
