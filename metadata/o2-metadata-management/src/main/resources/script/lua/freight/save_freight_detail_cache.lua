---
--- 保存运费模板明细、运费模板价格缓存
--- Created by perry.
--- DateTime: 2019-06-19 09:54
---
local freightInforKey = KEYS[1];
local freightDetailMap = cjson.decode(ARGV[1]);

for tmpId,tmpJson in pairs(freightDetailMap) do
    redis.call('hset', freightInforKey, tmpId, tmpJson);
end

