---
--- 删除运费模板明细、运费模板价格缓存
--- Created by perry.
--- DateTime: 2019-06-19 14:42
---
local freightInforDeleteKey = KEYS[1];
local freightDetailList = cjson.decode(ARGV[1]);

for i,tmpId in pairs(freightDetailList) do
    redis.call('hdel', freightInforDeleteKey, tmpId);
end