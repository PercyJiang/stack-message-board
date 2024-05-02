create table
	if not exists message (
		id uuid default gen_random_uuid (),
		source varchar(20),
		content text,
		created timestamp
		with
			time zone default localtimestamp,
			primary key (id)
	);