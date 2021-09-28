local limitKey = KEYS[1];
-- 仓库信息 redis key
local warehouseCacheKey = KEYS[2];
-- 仓库编码
local warehouseCode = ARGV[1];
-- 自提量
local insert = tonumber(ARGV[2])

local limitHashValue = redis.call('hget', limitKey, warehouseCode);
-- 保存仓库自提接单量
if  not limitHashValue  then
    local pickup = {};
    pickup["pickUpQuantity"] = insert;
    pickup["limitFlag"] = false;
    local str = cjson.encode(pickup);
    redis.call('hset', limitKey,warehouseCode, str);
    return 1;
end
-- 更新仓库已自提接单量
if limitHashValue  then
    -- 获取仓库信息
    local warehouse = redis.call("hget", warehouseCacheKey, warehouseCode);
    local warehouseObject = cjson.decode(warehouse);
    -- 配送限制接单量
    local pickUpLimit = warehouseObject["pickUpQuantity"];

    -- 仓库自提信息
    local pickup = redis.call("hget", limitKey, warehouseCode);
    local pickupObject = cjson.decode(pickup);
    -- 已自提量
    local pickupQuantity = pickupObject["pickUpQuantity"];

    local number = tonumber(pickupQuantity) + insert;

    --未设置自提限制量
    if not pickUpLimit then
        pickupObject["pickUpQuantity"] = number;
        pickupObject["limitFlag"] = false;
        redis.call("hset", limitKey, warehouseCode, cjson.encode(pickupObject));
        return 1;
    end
    -- 更新
    if number < pickUpLimit then
        pickupObject["pickUpQuantity"] = number;
        pickupObject["limitFlag"] = false;
        redis.call("hset", limitKey, warehouseCode, cjson.encode(pickupObject));
        return 1;
    end

    -- 达到自提限制量
    if number == pickUpLimit then
        pickupObject["pickUpQuantity"] = pickUpLimit;
        pickupObject["limitFlag"] = true;
        redis.call("hset", limitKey, warehouseCode, cjson.encode(pickupObject));
        return 1;
    end

    -- 大于自提限制量
    if number > pickUpLimit then
        pickupObject["limitFlag"] = true;
        redis.call("hset", limitKey, warehouseCode, cjson.encode(pickupObject));
        return -1;
    end

end

