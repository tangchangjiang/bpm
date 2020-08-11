---
--- 删除运费模板明细、运费模板价格缓存
--- Created by perry.
--- DateTime: 2019-06-19 14:42
---
local freightInforDeleteKey = KEYS[1];
local freightDetailList = cjson.decode(ARGV[1]);

for i,tmpId in pairs(freightDetailList) do
    local oldJson = redis.call('hget', freightInforDeleteKey, tmpId);
    if(oldJson)
    then
        local oldData = cjson.decode(oldJson);
        local regionCode = (oldData["defaultFlag"] == '1') and oldData["regionCode"]  or 'DEFAULT';
        local oldPriceKey = 'o2om:freight:'..oldData["tenantId"]..':'..oldData["templateCode"]..':'..regionCode;
        redis.call('del', oldPriceKey);

        if oldData["isDefault"] == 1 then
            local defautPriceKey = 'o2om:freight:'..oldData["tenantId"]..':'..oldData["templateCode"]..':DEFAULT';
            redis.call('del', defautPriceKey);
        end
    end

    redis.call('hdel', freightInforDeleteKey, tmpId);
end