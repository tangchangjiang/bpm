local limitKey = KEYS[1];
-- 仓库信息 redis key
local warehouseCacheKey = KEYS[2];
-- 仓库编码
local warehouseCode = ARGV[1];
-- 派送量
local insert = tonumber(ARGV[2])


local limitHashValue = redis.call('hget', limitKey, warehouseCode);
-- 保存仓库快递配送接单量
if not limitHashValue  then
    local express = {};
    express["expressedQuantity"]= insert;
    express["limitFlag"] = false;
    local str = cjson.encode(express);
    redis.call('hset', limitKey, warehouseCode, str);
    return 1;
end
-- 更新仓库已快递配送接单量
if limitHashValue ~= nil then
    -- 获取仓库信息
    local warehouse = redis.call("hget", warehouseCacheKey, warehouseCode);
    local warehouseObject = cjson.decode(warehouse);
    -- 配送限制接单量
    local expressLimit = warehouseObject["expressedQuantity"];

    -- 仓库配送信息
    local express = redis.call("hget", limitKey, warehouseCode);
    local expressObject = cjson.decode(express);
    -- 已配送量
    local expressedQuantity = expressObject["expressedQuantity"];

    local number = tonumber(expressedQuantity) + insert;
    -- 更新
    if number < expressLimit then
        expressObject["expressedQuantity"] = number;
        expressObject["limitFlag"] = false;
        redis.call("hset", limitKey, warehouseCode, cjson.encode(expressObject));
        return 1;
    end

    -- 达到配置限制量
    if number == expressLimit then
        expressObject["expressedQuantity"] = expressLimit;
        expressObject["limitFlag"] = true;
        redis.call("hset", limitKey, warehouseCode, cjson.encode(expressObject));
        return 1;
    end
    -- 大于到配送限制量
    if number > expressLimit then
        expressObject["limitFlag"] = true;
        redis.call("hset", limitKey, warehouseCode, cjson.encode(expressObject));
        return -1;
    end
end






