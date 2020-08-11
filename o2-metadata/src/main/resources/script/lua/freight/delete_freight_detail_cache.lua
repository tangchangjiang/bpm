---
--- 删除运费模板明细、运费模板价格缓存
--- Created by perry.
--- DateTime: 2019-06-19 14:42
---
local freightDetailKey = KEYS[1];
local freightDetailList = cjson.decode(ARGV[1]);

for i,tmpId in pairs(freightDetailList) do
    local oldJson = redis.call('hget', freightDetailKey, tmpId);
    if(oldJson)
    then
        local oldData = cjson.decode(oldJson);
        local regionCode = (oldData["regionCode"] and {oldData["regionCode"]} or {'null'})[1];
        local oldPriceKey = 'o2md:freight:'..oldData["templateCode"]':reg:'..regionCode;
        redis.call('del', oldPriceKey);

        if oldData["isDefault"] == 1 then
            local defautPriceKey = 'o2md:freight:'..oldData["templateCode"]..':reg:null';
            redis.call('del', defautPriceKey);
        end
    end

    redis.call('hdel', freightDetailKey, tmpId);
end