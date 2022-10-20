package org.o2.business.process.management.app.introduce;

import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.o2.business.process.management.domain.entity.BusinessProcess;
import org.o2.business.process.management.infra.convert.ViewJsonConvert;
import org.o2.core.helper.JsonHelper;
import org.o2.process.domain.engine.BpmnModel;
import org.o2.process.domain.engine.process.preruntime.validator.BpmnModelValidator;

/**
 * @author tangcj
 * @version V1.0
 * @date 2022/10/19 17:58
 */
@ImportValidators({
        @ImportValidator(templateCode = "O2BPM_BUSINESS_PROCESS", sheetIndex = 0)
})
public class BusinessProcessValidateServiceImpl extends ValidatorHandler {

    @Override
    public boolean validate(String data) {
        BusinessProcess bp = JsonHelper.stringToObject(data, BusinessProcess.class);

        if(StringUtils.isNotBlank(bp.getProcessJson())){
            BpmnModel bpmnModel = ViewJsonConvert.processJsonConvert(bp.getProcessJson());
            BpmnModelValidator.validate(bpmnModel);
        }

        return true;
    }
}
