drop table log;
create table log(
	host	text,
	wso_session_id	text,
	userid	text,
	port	int,
	access_time	date,
	request	text,
	status	int,
	bytes	int,
	referer	text,
	user_agent	text,
	server	text
);
