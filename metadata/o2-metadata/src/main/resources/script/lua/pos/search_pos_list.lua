local posIndexKey = KEYS[1];
local posDetailKey = KEYS[2];

local result = {}
local posIndexValue = redis.call("smembers", posIndexKey);
for i, v in ipairs(posIndexValue) do
    local detail = redis.call("hget", posDetailKey, v);
    if detail then
        result[i] = detail;
    end
end
return result;