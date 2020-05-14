use gym;

start transaction;

    create table if not exists subscription  (
        id int unsigned NOT NULL AUTO_INCREMENT,
        person_id int not null,
        type_name varchar(50) not null,
        start_date date not null,
        end_date date not null,
        reference_year int not null,
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

    insert into subscription (person_id, type_name, start_date, end_date, reference_year)
    (select id, 'annuale', CONCAT(year(subscription_date), '-01-01'), CONCAT(year(subscription_date), '-12-31'), year(subscription_date)
    from person where subscription_date is not null);

    ALTER TABLE person DROP COLUMN subscription_date;

commit;