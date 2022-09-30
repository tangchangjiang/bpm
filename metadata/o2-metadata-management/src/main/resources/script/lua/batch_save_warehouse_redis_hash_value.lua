local objMap = cjson.decode(ARGV[1]);

for hashKey, filedMap in pairs(objMap) do
    for fieldKey, fieldValue in pairs(filedMap) do
        if (fieldKey == "express_limit_value" or fieldKey == "pick_up_limit_value") then
            local result = redis.call("hget", hashKey, fieldKey)
            if (nil ~= tonumber(result) and tonumber(result) > 0) then
                fieldValue = result
            end
        end
        redis.call("hset", hashKey, fieldKey, fieldValue);
    end
end