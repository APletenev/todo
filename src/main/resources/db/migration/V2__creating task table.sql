CREATE TABLE task
(
    task_id   bigserial,
    task_name text      NOT NULL,
    task_desc text,
    task_date date,
    task_tag  bigserial NOT NULL
        CONSTRAINT task_tag
            REFERENCES tag (tag_id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX task_task_id_uindex
    ON task (task_id);

ALTER TABLE task
    ADD CONSTRAINT task_pk
        PRIMARY KEY (task_id);