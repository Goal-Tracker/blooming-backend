create table stamp
(
    id                      bigint       not null auto_increment,
    goal_id                 bigint       not null,
    user_id                 bigint       not null,
    stamp_day               bigint       not null,
    message                 text         not null,
    stamp_image_url         varchar(255) not null,
    is_deleted              boolean      not null,
    created_date_time       timestamp(6) not null,
    last_modified_date_time timestamp(6) not null,
    primary key (id)
);
