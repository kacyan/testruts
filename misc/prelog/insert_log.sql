delete from log;
-- 133.253.7.189 UuBWiIX9Q2MAADSjLWc S090009 12957 [23/Jan/2014:08:38:48 +0900] "GET /portal/ HTTP/1.1" 200 4234 "-" "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; GTB7.4; SLCC1; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30618)"
insert into log values(
	'133.253.7.189',
	'UuBWiIX9Q2MAADSjLWc',
	'S090009',
	12957,
	'2014-01-23 17:38:48',
	'GET /portal/ HTTP/1.1',
	200,
	4234,
	'-',
	'Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; GTB7.4; SLCC1; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30618)',
	'pre-wso1'
);
