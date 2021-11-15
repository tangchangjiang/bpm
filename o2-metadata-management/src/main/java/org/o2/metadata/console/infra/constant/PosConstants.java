package org.o2.metadata.console.infra.constant;

/**
 *
 * 服务点常量
 *
 * @author yipeng.zhu@hand-china.com 2021-11-08
 **/
public interface PosConstants {

    interface ErrorCode {
        String ERROR_POS_NAME_DUPLICATE= "o2md.error.pos_name.duplicate";
        String ERROR_POS_CODE_DUPLICATE= "o2md.error.pos_code.duplicate";
        String ERROR_POS_CODE_NOT_UPDATE = "o2md.error.pos_code.forbidden.update";
    }
}
