package org.o2.metadata.queue.client.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 元数据常量
 *
 * @author chao.yang05@hand-china.com 2023-05-10
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class O2MetadataQueueConstants {

    /**
     * 队列编码
     */
    public static class QueueCode {

        /**
         * 商家初始化元数据队列
         */
        public static final String O2MD_MERCHANT_META_INIT_EVT = "O2MD_MERCHANT_META_INIT_EVT";

        private QueueCode() {
        }
    }
}
