drop schema if exists postal_service_spring cascade;

create schema postal_service_spring;

set search_path=postal_service_spring;

create table clients
(
    id				bigserial primary key,
    surname			varchar(30) not null,
    first_name		varchar(20) not null,
	patronymic		varchar(30),
    email           varchar(60),
    phone_number    bigint unique not null
)
;

create table departments
(
	id				serial primary key,
	description		varchar unique not null
)
;

create table parcel_statuses
(
	id			smallserial primary key,
	status		varchar(30) unique not null
)
;

create table deliveries
(
	id						bigserial primary key,
	id_client				bigint not null references clients (id) on delete cascade,
	id_department_sender	int not null references departments (id) on delete cascade,
	id_department_recipient	int not null references departments (id) on delete cascade,
	recipient_phone			bigint not null,
    recipient_surname		varchar(30) not null,
    recipient_first_name	varchar(20) not null,
	recipient_patronymic	varchar(30),
	id_parcel_status		smallint not null references parcel_statuses (id) on delete cascade,
	date_time_creation		timestamp not null,
	date_time_status_change timestamp
)
;

create table notification_statuses
(
	id			smallserial primary key,
	status		varchar(30) unique not null
)
;

create table notifications
(
	id			bigserial primary key,
	message		text not null,
	id_status	smallint not null references notification_statuses (id) on delete cascade
)
;
