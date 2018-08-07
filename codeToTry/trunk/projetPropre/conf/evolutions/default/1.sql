# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table administrator (
  id_admin                      serial not null,
  user_admin_cip                varchar(255),
  constraint pk_administrator primary key (id_admin)
);

create table article (
  id                            serial not null,
  title                         varchar(255) not null,
  constraint pk_article primary key (id)
);

create table lost_password (
  token                         varchar(255) not null,
  cip                           varchar(255) not null,
  date_of_lapse                 timestamptz not null,
  constraint pk_lost_password primary key (token)
);

create table owner (
  id_owner                      serial not null,
  user_owner_cip                varchar(255),
  constraint pk_owner primary key (id_owner)
);

create table owner_project (
  owner_id_owner                integer not null,
  project_name                  varchar(255) not null,
  constraint pk_owner_project primary key (owner_id_owner,project_name)
);

create table project (
  name                          varchar(255) not null,
  resume_fr                     text,
  resume_en                     text,
  constraint pk_project primary key (name)
);

create table project_owner (
  project_name                  varchar(255) not null,
  owner_id_owner                integer not null,
  constraint pk_project_owner primary key (project_name,owner_id_owner)
);

create table project_user_participation (
  id                            serial not null,
  user_paticipationcip          varchar(255) not null,
  project_paticipationname      varchar(255) not null,
  activity                      integer not null,
  constraint pk_project_user_participation primary key (id)
);

create table status (
  name                          varchar(255) not null,
  constraint pk_status primary key (name)
);

create table users (
  cip                           varchar(255) not null,
  password                      varchar(255) not null,
  first_name                    varchar(255) not null,
  last_name                     varchar(255) not null,
  mail                          varchar(255) not null,
  resume_fr                     text,
  resume_en                     text,
  user_status_name              varchar(255),
  constraint uq_users_mail unique (mail),
  constraint pk_users primary key (cip)
);

create table users_project_user_participation (
  users_cip                     varchar(255) not null,
  project_user_participation_id integer not null,
  constraint pk_users_project_user_participation primary key (users_cip,project_user_participation_id)
);

alter table administrator add constraint fk_administrator_user_admin_cip foreign key (user_admin_cip) references users (cip) on delete restrict on update restrict;
create index ix_administrator_user_admin_cip on administrator (user_admin_cip);

alter table owner add constraint fk_owner_user_owner_cip foreign key (user_owner_cip) references users (cip) on delete restrict on update restrict;
create index ix_owner_user_owner_cip on owner (user_owner_cip);

alter table owner_project add constraint fk_owner_project_owner foreign key (owner_id_owner) references owner (id_owner) on delete restrict on update restrict;
create index ix_owner_project_owner on owner_project (owner_id_owner);

alter table owner_project add constraint fk_owner_project_project foreign key (project_name) references project (name) on delete restrict on update restrict;
create index ix_owner_project_project on owner_project (project_name);

alter table project_owner add constraint fk_project_owner_project foreign key (project_name) references project (name) on delete restrict on update restrict;
create index ix_project_owner_project on project_owner (project_name);

alter table project_owner add constraint fk_project_owner_owner foreign key (owner_id_owner) references owner (id_owner) on delete restrict on update restrict;
create index ix_project_owner_owner on project_owner (owner_id_owner);

alter table users add constraint fk_users_user_status_name foreign key (user_status_name) references status (name) on delete restrict on update restrict;
create index ix_users_user_status_name on users (user_status_name);

alter table users_project_user_participation add constraint fk_users_project_user_participation_users foreign key (users_cip) references users (cip) on delete restrict on update restrict;
create index ix_users_project_user_participation_users on users_project_user_participation (users_cip);

alter table users_project_user_participation add constraint fk_users_project_user_participation_project_user_particip_2 foreign key (project_user_participation_id) references project_user_participation (id) on delete restrict on update restrict;
create index ix_users_project_user_participation_project_user_particip_2 on users_project_user_participation (project_user_participation_id);


# --- !Downs

alter table if exists administrator drop constraint if exists fk_administrator_user_admin_cip;
drop index if exists ix_administrator_user_admin_cip;

alter table if exists owner drop constraint if exists fk_owner_user_owner_cip;
drop index if exists ix_owner_user_owner_cip;

alter table if exists owner_project drop constraint if exists fk_owner_project_owner;
drop index if exists ix_owner_project_owner;

alter table if exists owner_project drop constraint if exists fk_owner_project_project;
drop index if exists ix_owner_project_project;

alter table if exists project_owner drop constraint if exists fk_project_owner_project;
drop index if exists ix_project_owner_project;

alter table if exists project_owner drop constraint if exists fk_project_owner_owner;
drop index if exists ix_project_owner_owner;

alter table if exists users drop constraint if exists fk_users_user_status_name;
drop index if exists ix_users_user_status_name;

alter table if exists users_project_user_participation drop constraint if exists fk_users_project_user_participation_users;
drop index if exists ix_users_project_user_participation_users;

alter table if exists users_project_user_participation drop constraint if exists fk_users_project_user_participation_project_user_particip_2;
drop index if exists ix_users_project_user_participation_project_user_particip_2;

drop table if exists administrator cascade;

drop table if exists article cascade;

drop table if exists lost_password cascade;

drop table if exists owner cascade;

drop table if exists owner_project cascade;

drop table if exists project cascade;

drop table if exists project_owner cascade;

drop table if exists project_user_participation cascade;

drop table if exists status cascade;

drop table if exists users cascade;

drop table if exists users_project_user_participation cascade;

