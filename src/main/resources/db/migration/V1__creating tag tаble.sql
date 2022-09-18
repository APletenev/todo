create table tag
(
    tag_id   bigserial
        constraint tag_pk
            primary key,
    tag_name text not null
);

create unique index tag_tag_id_uindex
    on tag (tag_id);

create unique index tag_tag_name_uindex
    on tag (tag_name);

