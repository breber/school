input_lines = LOAD '/datasets/Lab5/network_trace' 
	USING PigStorage(' ')
	AS (time:chararray, ip:chararray, sourceip:chararray, arrow:chararray, destip:chararray, protocol:chararray, arest:chararray);

-- eliminate anything that isn't TCP
tcp_records = FILTER input_lines by $5 == 'tcp';

-- strip the port number out of the strings
stripped_port = FOREACH tcp_records GENERATE REGEX_EXTRACT($2, '(.*[.].*[.].*[.].*)[.].*', 1), REGEX_EXTRACT($4, '(.*[.].*[.].*[.].*)[.].*', 1);

-- group by source IP
grouped = GROUP stripped_port BY $0;

-- get the number of unique destinations
dis = FOREACH grouped {
	rslt = DISTINCT $1;
	GENERATE $0, COUNT(rslt);
}

-- sort them by number of unique destinations
sorted = ORDER dis by $1 DESC;

-- get the top two
top_ten = LIMIT sorted 10;

-- output the top two
STORE top_ten INTO '/user/breber/Lab5/exp2/output';
