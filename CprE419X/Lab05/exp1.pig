input_lines = LOAD '/home/breber/census' AS (usps:chararray, geoid:int, pop10:int, hu10:int, aland:int, arest:chararray);

-- group by state
grouped = GROUP input_lines BY usps;

-- count the entries in each group
max_land = FOREACH grouped GENERATE group, SUM(input_lines.aland);

-- order them by size
sorted = ORDER max_land by $1 DESC;

-- get the top two
top_two = LIMIT sorted 2;

-- output the top two
STORE top_two INTO '/home/breber/exp1/output';
