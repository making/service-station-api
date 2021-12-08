CREATE TABLE IF NOT EXISTS servicelocation
(
    id       varchar(255) not null,
    address  varchar(255) not null,
    location point        not null,
    primary key (id)
);

CREATE INDEX IF NOT EXISTS location_idx ON servicelocation USING gist (location);