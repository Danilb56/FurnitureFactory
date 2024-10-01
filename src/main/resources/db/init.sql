create table if not exists furniture
(
    id                int8 primary key,
    type              varchar(20) not null,
    article           int8        not null,
    price             int8        not null,
    furniture_line_id int8        not null
);

create table if not exists furniture_line
(
    id   int8 primary key,
    name varchar(30) not null
);

create table if not exists component
(
    code  int8 primary key,
    price int8        not null,
    type  varchar(20) not null
);

create table if not exists furniture_component_link
(
    furniture_id int8,
    component_id int8,
    count        int
);

create table if not exists user
(
    id       int8,
    login    varchar(20) not null unique,
    password varchar(20) not null,
    role     varchar(20) not null
);
