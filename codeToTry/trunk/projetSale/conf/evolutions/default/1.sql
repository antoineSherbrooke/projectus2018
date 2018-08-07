# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table article (
  id                            serial not null,
  title                         varchar(255),
  constraint pk_article primary key (id)
);

create table lost_password (
  token                         varchar(255) not null,
  pseudo                        varchar(255),
  date_of_lapse                 timestamptz,
  constraint pk_lost_password primary key (token)
);

create table users (
  pseudo                        varchar(255) not null,
  password                      varchar(255),
  first_name                    varchar(255),
  last_name                     varchar(255),
  mail                          varchar(255),
  constraint pk_users primary key (pseudo)
);


# --- !Downs

drop table if exists article cascade;

drop table if exists lost_password cascade;

drop table if exists users cascade;

