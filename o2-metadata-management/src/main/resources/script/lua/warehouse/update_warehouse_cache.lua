local warehouseKey = KEYS[1];
local expressKey = KEYS[2];
local pickUpKey = KEYS[3];
local configMap = cjson.decode(ARGV[1]);

for configKey, configValue in pairs(configMap) do
    --仓库信息
    local warehouseObject = cjson.decode(configValue);
    --仓库设置派送量
    local warehouseExpressedQuantity = tonumber(warehouseObject['expressedQuantity']);
    --已派送
    local expressHashValue = redis.call('hget', expressKey, configKey);
    if  expressHashValue then
        local expressObject = cjson.decode(expressHashValue);
        local expressedQuantity = tonumber(expressObject['expressedQuantity']);
        -- 仓库设置派送量 大于 已派送
        if warehouseExpressedQuantity > expressedQuantity then
            expressObject['limitFlag'] = false;
            redis.call("hset", expressKey, configKey, cjson.encode(expressObject));
        end
        if warehouseExpressedQuantity <= expressedQuantity then
            expressObject['limitFlag'] = true;
            redis.call("hset", expressKey, configKey, cjson.encode(expressObject));
        end
    end

    --自提量
    local  warehousePickUpQuantity = tonumber(warehouseObject['pickUpQuantity']);
    --已自提
    local pickUpHashValue = redis.call('hget', pickUpKey, configKey);
    if  pickUpHashValue then
        local pickUpObject = cjson.decode(pickUpHashValue);
        local pickUpQuantity = tonumber(pickUpObject['pickUpQuantity']);
        -- 仓库设置自提量 大于 已自提
        if warehousePickUpQuantity > pickUpQuantity then
            pickUpObject['limitFlag'] = false;
            redis.call("hset", pickUpKey, configKey, cjson.encode(pickUpObject));
        end
        if warehousePickUpQuantity <= pickUpQuantity then
            pickUpObject['limitFlag'] = true;
            redis.call("hset", pickUpKey, configKey, cjson.encode(pickUpObject));
        end
    end
    redis.call('hset', warehouseKey, configKey, configValue);
end
