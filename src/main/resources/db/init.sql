create table if not exists furniture_line
(
    id   int8 primary key,
    name varchar(30) not null
);


create table if not exists furniture
(
    id                int8 primary key,
    type              varchar(20) not null,
    article           int8        not null,
    price             int8        not null,
    furniture_line_id int8        not null,
    foreign key (furniture_line_id) references furniture_line (id) on delete no action
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
    count        int,
    foreign key (furniture_id) references furniture (id) on delete no action,
    foreign key (component_id) references component (code) on delete no action
);

create table if not exists user
(
    id       int8 primary key,
    login    varchar(20) not null unique,
    password varchar(20) not null,
    role     varchar(20) not null
);

create table if not exists shop
(
    id       int8 primary key,
    address  varchar(100) not null unique,
    fax      varchar(10)  not null unique,
    owner_id int8         not null,
    foreign key (owner_id) references user (id) on delete no action
);

create table if not exists orders
(
    id      int8 primary key,
    date    varchar(15) not null,
    shop_id int8        not null,
    foreign key (shop_id) references shop (id) on delete no action
);

create table if not exists order_furniture_link
(
    order_id     int8 not null,
    furniture_id int8 not null,
    count        int  not null check (count > 0),
    foreign key (order_id) references orders (id) on delete no action,
    foreign key (furniture_id) references furniture (id) on delete no action
);
