use gym;

create table if not exists subscription_type  (
	name varchar(50) primary key,
    start_month int not null,
    end_month int not null
);

create table if not exists subscription  (
    person_id int not null,
    type_name varchar(50) not null,
    start_date datetime not null,
    end_date datetime not null,
    primary key(person_id, type_name),
    foreign key(type_name) references subscription_type(name),
	foreign key(person_id) references person(id)
);

alter table person add column custom_subscription_start_date datetime;
alter table person add column custom_subscription_end_date datetime;

insert into subscription_type values ('Annuale', 1, 12), ('Gennaio-Luglio', 1, 7), ('Settembre-Dicembre', 9, 12);

# TODO move person.subscription_date in subscptions as 'Annuale' type entry