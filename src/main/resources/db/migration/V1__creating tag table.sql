CREATE TABLE tag
(
    tag_id bigserial CONSTRAINT tag_pk PRIMARY KEY,
    tag_name text NOT NULL
);

CREATE UNIQUE INDEX tag_tag_id_uindex ON tag (tag_id);

CREATE UNIQUE INDEX tag_tag_name_uindex ON tag (tag_name);