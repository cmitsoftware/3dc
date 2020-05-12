use gym;

create table if not exists subscription  (
	id int unsigned NOT NULL AUTO_INCREMENT,
    person_id int not null,
    type_name varchar(50) not null,
    start_date date not null,
    end_date date not null,
    primary key(id),
	foreign key(person_id) references person(id),
    UNIQUE KEY(person_id, type_name)
);

alter table person add column custom_subscription_start_date date;
alter table person add column custom_subscription_end_date date;

insert into configurations values
('subscription_type_0', 'name=annuale;start_month=1;end_month=12'),
('subscription_type_1', 'name=genaio-luglio;start_month=1;end_month=7'),
('subscription_type_2', 'name=settembre-dicembre;start_month=9;end_month=12');

# TODO move person.subscription_date in subscptions as 'Annuale' type entry