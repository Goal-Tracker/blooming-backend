create table users
(
    id                      bigint       not null auto_increment,
    oauth_id                varchar(255) not null unique,
    oauth_type              varchar(255) not null,
    email                   varchar(50)  not null,
    name                    varchar(50)  not null unique,
    profile_image_url       varchar(255) not null,
    theme_color             varchar(255) not null,
    status_message          text         not null,
    has_new_alarm           boolean      not null,
    is_deleted              boolean      not null,
    created_date_time       timestamp(6) not null,
    last_modified_date_time timestamp(6) not null,
    primary key (id)
);

create table black_list_token
(
    id    bigint not null auto_increment,
    token varchar(255),
    primary key (id)
);

create table device_token
(
    id        bigint       not null auto_increment,
    user_id   bigint       not null,
    token     varchar(255) not null unique,
    is_active boolean      not null,
    primary key (id)
);

create table notification
(
    id                      bigint       not null auto_increment,
    receiver_id             bigint       not null,
    title                   varchar(255) not null,
    content                 varchar(255) not null,
    type                    varchar(255) not null,
    request_id              bigint,
    created_date_time       timestamp(6) not null,
    last_modified_date_time timestamp(6) not null,
    primary key (id)
);

create table friend
(
    id                      bigint       not null auto_increment,
    request_user_id         bigint       not null,
    requested_user_id       bigint       not null,
    is_friends              boolean      not null,
    created_date_time       timestamp(6) not null,
    last_modified_date_time timestamp(6) not null,
    primary key (id)
);

create table goal
(
    id                      bigint       not null auto_increment,
    name                    varchar(50)  not null,
    memo                    text         not null,
    manager_id              bigint       not null,
    start_date              date         not null,
    end_date                date         not null,
    days                    bigint       not null,
    deleted                 boolean      not null,
    created_date_time       timestamp(6) not null,
    last_modified_date_time timestamp(6) not null,
    primary key (id)
);

create table goal_team
(
    id                      bigint       not null auto_increment,
    user_id                 bigint       not null,
    goal_id                 bigint       not null,
    accepted                boolean      not null,
    deleted                 boolean      not null,
    created_date_time       timestamp(6) not null,
    last_modified_date_time timestamp(6) not null,
    primary key (id)
);

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

create table user_report
(
    id                      bigint       not null auto_increment,
    reporter_id             bigint       not null,
    reportee_id             bigint       not null,
    content                 text         not null,
    created_date_time       timestamp(6) not null,
    last_modified_date_time timestamp(6) not null,
    primary key (id)
);

create table goal_report
(
    id                      bigint       not null auto_increment,
    reporter_id             bigint       not null,
    goal_id                 bigint       not null,
    content                 text         not null,
    created_date_time       timestamp(6) not null,
    last_modified_date_time timestamp(6) not null,
    primary key (id)
);

create table stamp_report
(
    id                      bigint       not null auto_increment,
    reporter_id             bigint       not null,
    stamp_id                bigint       not null,
    content                 text         not null,
    created_date_time       timestamp(6) not null,
    last_modified_date_time timestamp(6) not null,
    primary key (id)
);

alter table friend
    add constraint fk_friend_request_user foreign key (request_user_id) references users (id);
alter table friend
    add constraint fk_friend_requested_user foreign key (requested_user_id) references users (id);
alter table goal_report
    add constraint fk_goal_report_goal foreign key (goal_id) references goal (id);
alter table goal_report
    add constraint fk_goal_report_reporter foreign key (reporter_id) references users (id);
alter table goal_team
    add constraint fk_goal_team_user foreign key (goal_id) references goal (id);
alter table goal_team
    add constraint fk_goal_team_goal foreign key (user_id) references users (id);
alter table notification
    add constraint fk_notification_receiver foreign key (receiver_id) references users (id);
alter table stamp
    add constraint fk_stamp_goal foreign key (goal_id) references goal (id);
alter table stamp
    add constraint fk_stamp_user foreign key (user_id) references users (id);
alter table stamp_report
    add constraint fk_stamp_report_reporter foreign key (reporter_id) references users (id);
alter table stamp_report
    add constraint fk_stamp_report_stamp foreign key (stamp_id) references stamp (id);
alter table user_report
    add constraint fk_user_report_reportee foreign key (reportee_id) references users (id);
alter table user_report
    add constraint fk_user_report_reporter foreign key (reporter_id) references users (id);
