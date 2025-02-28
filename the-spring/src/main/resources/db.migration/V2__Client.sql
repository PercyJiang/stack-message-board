create table
	if not exists client (
		id uuid default gen_random_uuid (),
		username varchar(20) unique,
		password varchar(20),
		created timestamp
		with
			time zone default localtimestamp,
			primary key (id)
	);