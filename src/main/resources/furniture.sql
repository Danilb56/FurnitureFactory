create table if not exists furniture
(
    id              int8 primary key,
    type            varchar(20) not null,
    article         int8        not null,
    price           int8        not null,
    furnitureLineId int8        not null
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
