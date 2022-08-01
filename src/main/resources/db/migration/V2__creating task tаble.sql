create table task
(
    task_id   bigserial,
    task_name text      not null,
    task_desc text,
    task_date date,
    task_tag  bigserial not null
        constraint task_tag
            references tag (tag_id) ON DELETE CASCADE
);

create unique index task_task_id_uindex
    on task (task_id);

create unique index task_task_tag_uindex
    on task (task_tag);

alter table task
    add constraint task_pk
        primary key (task_id);