package org.o2.business.process.management.api.vo.interactive;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/10/10 16:41
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "shape", visible = true,
        defaultImpl = Void.class, include = JsonTypeInfo.As.EXISTING_PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NotationEdge.class, name = "flow-edge"),
        @JsonSubTypes.Type(value = NotationNode.class, name = "start-node"),
        @JsonSubTypes.Type(value = NotationNode.class, name = "branch-node"),
        @JsonSubTypes.Type(value = NotationNode.class, name = "end-node"),
        @JsonSubTypes.Type(value = NotationNode.class, name = "flow-node"),
})
@Data
public class Cell implements NotationCell{

    private String id;

    private String shape;

}
