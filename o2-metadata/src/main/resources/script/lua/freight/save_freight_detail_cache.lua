---
--- 保存运费模板明细、运费模板价格缓存
--- Created by perry.
--- DateTime: 2019-06-19 09:54
---
local freightInforKey = KEYS[1];
local freightDetailMap = cjson.decode(ARGV[1]);

for tmpId,tmpJson in pairs(freightDetailMap) do
    local oldJson = redis.call('hget', freightInforKey, tmpId);
    if(oldJson)
    then
        local oldData = cjson.decode(oldJson);
        local regionCode = (oldData["defaultFlag"] == '1') and oldData["regionCode"]  or 'DEFAULT';
        local oldPriceKey = 'o2om:freight:'..oldData["tenantId"]..':{'..oldData["templateCode"]..'}:'..regionCode;
        redis.call('del', oldPriceKey);
    end
    redis.call('hset', freightInforKey, tmpId, tmpJson);
end

local freightHeadMap ;
if (ARGV[2] ==nil or ARGV[2] =='')
    then
    freightHeadMap = cjson.decode(ARGV[2]);

    for headKey,headJson in pairs(freightHeadMap) do
         redis.call('hset', freightInforKey, headKey, headJson);
    end
end