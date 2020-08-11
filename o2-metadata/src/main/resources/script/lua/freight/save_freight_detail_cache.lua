---
--- 保存运费模板明细、运费模板价格缓存
--- Created by perry.
--- DateTime: 2019-06-19 09:54
---
local freightDetailKey = KEYS[1];
local freightDetailMap = cjson.decode(ARGV[1]);
local freightPriceMap = cjson.decode(ARGV[2]);

for tmpId,tmpJson in pairs(freightDetailMap) do
    local oldJson = redis.call('hget', freightDetailKey, tmpId);
    if(oldJson)
    then
        local oldData = cjson.decode(oldJson);
        local regionCode = (oldData["regionCode"] and {oldData["regionCode"]} or {'null'})[1];
        local oldPriceKey = 'o2md:freight:'..oldData["templateCode"]..':reg:'..regionCode;
        redis.call('del', oldPriceKey);
    end

    redis.call('hset', freightDetailKey, tmpId, tmpJson);
end

for priceKey,priceJson in pairs(freightPriceMap) do
    redis.call('set', priceKey, priceJson);
end