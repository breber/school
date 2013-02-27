ip_trace = LOAD '/datasets/Lab5/ip_trace' 
	USING PigStorage(' ')
	AS (time:chararray, connectionid:chararray, sourceip:chararray, arrow:chararray, destip:chararray, protocol:chararray, arest:chararray);

raw_block = LOAD '/datasets/Lab5/raw_block' 
	USING PigStorage(' ')
	AS (bconnectionid:chararray, action:chararray);

-- Join the two datasets
iptrace_blocked = JOIN ip_trace by connectionid, raw_block by bconnectionid;

-- Filter by only connections that were blocked
blocked_connections = FILTER iptrace_blocked by raw_block::action == 'Blocked';

-- Only store important fields
blocked_important = FOREACH blocked_connections GENERATE ip_trace::time, ip_trace::connectionid, ip_trace::sourceip, ip_trace::destip, raw_block::action;

-- desired output <Time> <ConnectionId> <SourceIp> <DestIp> "Blocked"
STORE blocked_important INTO '/user/breber/Lab5/exp3/firewall';


--STORE output INTO '/user/breber/Lab5/exp3/output';
