# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table associations (
  id                            serial not null,
  association_type              integer,
  users_cip                     varchar(255),
  members_id                    integer,
  supervisors_id                integer,
  constraint ck_associations_association_type check (association_type in (0,1,2)),
  constraint uq_associations_members_id unique (members_id),
  constraint uq_associations_supervisors_id unique (supervisors_id),
  constraint pk_associations primary key (id)
);

create table backlog_entries (
  id                            serial not null,
  name                          varchar(255),
  description                   varchar(255),
  state                         integer,
  number                        integer,
  activate                      boolean,
  first_estimate                integer,
  parent_id                     integer,
  priority                      integer,
  total_time                    integer,
  projects_id                   integer,
  constraint ck_backlog_entries_first_estimate check (first_estimate in (0,1,2,3,4,5)),
  constraint ck_backlog_entries_priority check (priority in (0,1,2,3,4)),
  constraint pk_backlog_entries primary key (id)
);

create table backlog_entries_sprint (
  backlog_entries_id            integer not null,
  sprint_id                     integer not null,
  constraint pk_backlog_entries_sprint primary key (backlog_entries_id,sprint_id)
);

create table comments (
  id                            serial not null,
  title                         varchar(255),
  content                       varchar(255),
  date                          timestamp,
  sprint_tasks_id               integer,
  members_id                    integer,
  constraint pk_comments primary key (id)
);

create table groups (
  id                            serial not null,
  name                          varchar(255),
  email                         varchar(255),
  projects_id                   integer,
  constraint uq_groups_projects_id unique (projects_id),
  constraint pk_groups primary key (id)
);

create table history (
  id                            serial not null,
  description                   varchar(255),
  date                          timestamp,
  projects_id                   integer,
  members_id                    integer,
  constraint pk_history primary key (id)
);

create table meeting_members (
  id                            serial not null,
  member_cip                    varchar(255),
  active                        boolean,
  meetings_id                   integer,
  constraint pk_meeting_members primary key (id)
);

create table meetings (
  id                            serial not null,
  name                          varchar(255),
  date                          timestamp,
  conclusion                    varchar(255),
  time_worked                   integer,
  day_order                     varchar(255),
  first_estimate                integer,
  activate                      boolean,
  projects_id                   integer,
  sprint_id                     integer,
  constraint pk_meetings primary key (id)
);

create table members (
  id                            serial not null,
  news                          boolean,
  active                        boolean,
  associations_id               integer,
  groups_id                     integer,
  member_type                   integer,
  last_history_seen             integer,
  constraint ck_members_member_type check (member_type in (0,1,2,3)),
  constraint uq_members_associations_id unique (associations_id),
  constraint pk_members primary key (id)
);

create table members_tasks_doing (
  id                            serial not null,
  time_spent                    integer,
  day                           timestamp,
  sprint_tasks_id               integer,
  members_id                    integer,
  constraint pk_members_tasks_doing primary key (id)
);

create table members_tasks_review (
  id                            serial not null,
  time_spent                    integer,
  day                           timestamp,
  sprint_tasks_id               integer,
  members_id                    integer,
  constraint pk_members_tasks_review primary key (id)
);

create table projects (
  id                            serial not null,
  description                   varchar(255),
  name                          varchar(255),
  state                         boolean,
  groups_id                     integer,
  constraint uq_projects_groups_id unique (groups_id),
  constraint pk_projects primary key (id)
);

create table projects_supervisors (
  projects_id                   integer not null,
  supervisors_id                integer not null,
  constraint pk_projects_supervisors primary key (projects_id,supervisors_id)
);

create table releases (
  id                            serial not null,
  name                          varchar(255),
  release_date                  timestamp,
  comment                       varchar(255),
  running                       boolean,
  active                        boolean,
  projects_id                   integer,
  constraint pk_releases primary key (id)
);

create table sprint (
  id                            serial not null,
  name                          varchar(255),
  start_date                    timestamp,
  end_date                      timestamp,
  description                   varchar(255),
  number                        integer,
  state                         integer,
  releases_id                   integer,
  constraint ck_sprint_state check (state in (0,1,2,3)),
  constraint pk_sprint primary key (id)
);

create table sprint_backlog_entries (
  sprint_id                     integer not null,
  backlog_entries_id            integer not null,
  constraint pk_sprint_backlog_entries primary key (sprint_id,backlog_entries_id)
);

create table sprint_tasks (
  id                            serial not null,
  name                          varchar(255),
  description                   varchar(255),
  estimate                      integer,
  remaining_time                integer,
  remaining_time_review         integer,
  estimate_review               integer,
  start_date                    timestamp,
  end_date                      timestamp,
  backlog_entries_id            integer,
  sprint_id                     integer,
  state                         integer,
  constraint ck_sprint_tasks_state check (state in (0,1,2,3)),
  constraint pk_sprint_tasks primary key (id)
);

create table supervisors (
  id                            serial not null,
  associations_id               integer,
  constraint uq_supervisors_associations_id unique (associations_id),
  constraint pk_supervisors primary key (id)
);

create table users (
  cip                           varchar(255) not null,
  image                         bytea,
  first_name                    varchar(255),
  last_name                     varchar(255),
  hash                          varchar(255),
  active                        boolean,
  constraint pk_users primary key (cip)
);

alter table associations add constraint fk_associations_users_cip foreign key (users_cip) references users (cip) on delete restrict on update restrict;
create index ix_associations_users_cip on associations (users_cip);

alter table associations add constraint fk_associations_members_id foreign key (members_id) references members (id) on delete restrict on update restrict;

alter table associations add constraint fk_associations_supervisors_id foreign key (supervisors_id) references supervisors (id) on delete restrict on update restrict;

alter table backlog_entries add constraint fk_backlog_entries_projects_id foreign key (projects_id) references projects (id) on delete restrict on update restrict;
create index ix_backlog_entries_projects_id on backlog_entries (projects_id);

alter table backlog_entries_sprint add constraint fk_backlog_entries_sprint_backlog_entries foreign key (backlog_entries_id) references backlog_entries (id) on delete restrict on update restrict;
create index ix_backlog_entries_sprint_backlog_entries on backlog_entries_sprint (backlog_entries_id);

alter table backlog_entries_sprint add constraint fk_backlog_entries_sprint_sprint foreign key (sprint_id) references sprint (id) on delete restrict on update restrict;
create index ix_backlog_entries_sprint_sprint on backlog_entries_sprint (sprint_id);

alter table comments add constraint fk_comments_sprint_tasks_id foreign key (sprint_tasks_id) references sprint_tasks (id) on delete restrict on update restrict;
create index ix_comments_sprint_tasks_id on comments (sprint_tasks_id);

alter table comments add constraint fk_comments_members_id foreign key (members_id) references members (id) on delete restrict on update restrict;
create index ix_comments_members_id on comments (members_id);

alter table groups add constraint fk_groups_projects_id foreign key (projects_id) references projects (id) on delete restrict on update restrict;

alter table history add constraint fk_history_projects_id foreign key (projects_id) references projects (id) on delete restrict on update restrict;
create index ix_history_projects_id on history (projects_id);

alter table history add constraint fk_history_members_id foreign key (members_id) references members (id) on delete restrict on update restrict;
create index ix_history_members_id on history (members_id);

alter table meeting_members add constraint fk_meeting_members_meetings_id foreign key (meetings_id) references meetings (id) on delete restrict on update restrict;
create index ix_meeting_members_meetings_id on meeting_members (meetings_id);

alter table meetings add constraint fk_meetings_projects_id foreign key (projects_id) references projects (id) on delete restrict on update restrict;
create index ix_meetings_projects_id on meetings (projects_id);

alter table meetings add constraint fk_meetings_sprint_id foreign key (sprint_id) references sprint (id) on delete restrict on update restrict;
create index ix_meetings_sprint_id on meetings (sprint_id);

alter table members add constraint fk_members_associations_id foreign key (associations_id) references associations (id) on delete restrict on update restrict;

alter table members add constraint fk_members_groups_id foreign key (groups_id) references groups (id) on delete restrict on update restrict;
create index ix_members_groups_id on members (groups_id);

alter table members_tasks_doing add constraint fk_members_tasks_doing_sprint_tasks_id foreign key (sprint_tasks_id) references sprint_tasks (id) on delete restrict on update restrict;
create index ix_members_tasks_doing_sprint_tasks_id on members_tasks_doing (sprint_tasks_id);

alter table members_tasks_doing add constraint fk_members_tasks_doing_members_id foreign key (members_id) references members (id) on delete restrict on update restrict;
create index ix_members_tasks_doing_members_id on members_tasks_doing (members_id);

alter table members_tasks_review add constraint fk_members_tasks_review_sprint_tasks_id foreign key (sprint_tasks_id) references sprint_tasks (id) on delete restrict on update restrict;
create index ix_members_tasks_review_sprint_tasks_id on members_tasks_review (sprint_tasks_id);

alter table members_tasks_review add constraint fk_members_tasks_review_members_id foreign key (members_id) references members (id) on delete restrict on update restrict;
create index ix_members_tasks_review_members_id on members_tasks_review (members_id);

alter table projects add constraint fk_projects_groups_id foreign key (groups_id) references groups (id) on delete restrict on update restrict;

alter table projects_supervisors add constraint fk_projects_supervisors_projects foreign key (projects_id) references projects (id) on delete restrict on update restrict;
create index ix_projects_supervisors_projects on projects_supervisors (projects_id);

alter table projects_supervisors add constraint fk_projects_supervisors_supervisors foreign key (supervisors_id) references supervisors (id) on delete restrict on update restrict;
create index ix_projects_supervisors_supervisors on projects_supervisors (supervisors_id);

alter table releases add constraint fk_releases_projects_id foreign key (projects_id) references projects (id) on delete restrict on update restrict;
create index ix_releases_projects_id on releases (projects_id);

alter table sprint add constraint fk_sprint_releases_id foreign key (releases_id) references releases (id) on delete restrict on update restrict;
create index ix_sprint_releases_id on sprint (releases_id);

alter table sprint_backlog_entries add constraint fk_sprint_backlog_entries_sprint foreign key (sprint_id) references sprint (id) on delete restrict on update restrict;
create index ix_sprint_backlog_entries_sprint on sprint_backlog_entries (sprint_id);

alter table sprint_backlog_entries add constraint fk_sprint_backlog_entries_backlog_entries foreign key (backlog_entries_id) references backlog_entries (id) on delete restrict on update restrict;
create index ix_sprint_backlog_entries_backlog_entries on sprint_backlog_entries (backlog_entries_id);

alter table sprint_tasks add constraint fk_sprint_tasks_backlog_entries_id foreign key (backlog_entries_id) references backlog_entries (id) on delete restrict on update restrict;
create index ix_sprint_tasks_backlog_entries_id on sprint_tasks (backlog_entries_id);

alter table sprint_tasks add constraint fk_sprint_tasks_sprint_id foreign key (sprint_id) references sprint (id) on delete restrict on update restrict;
create index ix_sprint_tasks_sprint_id on sprint_tasks (sprint_id);

alter table supervisors add constraint fk_supervisors_associations_id foreign key (associations_id) references associations (id) on delete restrict on update restrict;


# --- !Downs

alter table if exists associations drop constraint if exists fk_associations_users_cip;
drop index if exists ix_associations_users_cip;

alter table if exists associations drop constraint if exists fk_associations_members_id;

alter table if exists associations drop constraint if exists fk_associations_supervisors_id;

alter table if exists backlog_entries drop constraint if exists fk_backlog_entries_projects_id;
drop index if exists ix_backlog_entries_projects_id;

alter table if exists backlog_entries_sprint drop constraint if exists fk_backlog_entries_sprint_backlog_entries;
drop index if exists ix_backlog_entries_sprint_backlog_entries;

alter table if exists backlog_entries_sprint drop constraint if exists fk_backlog_entries_sprint_sprint;
drop index if exists ix_backlog_entries_sprint_sprint;

alter table if exists comments drop constraint if exists fk_comments_sprint_tasks_id;
drop index if exists ix_comments_sprint_tasks_id;

alter table if exists comments drop constraint if exists fk_comments_members_id;
drop index if exists ix_comments_members_id;

alter table if exists groups drop constraint if exists fk_groups_projects_id;

alter table if exists history drop constraint if exists fk_history_projects_id;
drop index if exists ix_history_projects_id;

alter table if exists history drop constraint if exists fk_history_members_id;
drop index if exists ix_history_members_id;

alter table if exists meeting_members drop constraint if exists fk_meeting_members_meetings_id;
drop index if exists ix_meeting_members_meetings_id;

alter table if exists meetings drop constraint if exists fk_meetings_projects_id;
drop index if exists ix_meetings_projects_id;

alter table if exists meetings drop constraint if exists fk_meetings_sprint_id;
drop index if exists ix_meetings_sprint_id;

alter table if exists members drop constraint if exists fk_members_associations_id;

alter table if exists members drop constraint if exists fk_members_groups_id;
drop index if exists ix_members_groups_id;

alter table if exists members_tasks_doing drop constraint if exists fk_members_tasks_doing_sprint_tasks_id;
drop index if exists ix_members_tasks_doing_sprint_tasks_id;

alter table if exists members_tasks_doing drop constraint if exists fk_members_tasks_doing_members_id;
drop index if exists ix_members_tasks_doing_members_id;

alter table if exists members_tasks_review drop constraint if exists fk_members_tasks_review_sprint_tasks_id;
drop index if exists ix_members_tasks_review_sprint_tasks_id;

alter table if exists members_tasks_review drop constraint if exists fk_members_tasks_review_members_id;
drop index if exists ix_members_tasks_review_members_id;

alter table if exists projects drop constraint if exists fk_projects_groups_id;

alter table if exists projects_supervisors drop constraint if exists fk_projects_supervisors_projects;
drop index if exists ix_projects_supervisors_projects;

alter table if exists projects_supervisors drop constraint if exists fk_projects_supervisors_supervisors;
drop index if exists ix_projects_supervisors_supervisors;

alter table if exists releases drop constraint if exists fk_releases_projects_id;
drop index if exists ix_releases_projects_id;

alter table if exists sprint drop constraint if exists fk_sprint_releases_id;
drop index if exists ix_sprint_releases_id;

alter table if exists sprint_backlog_entries drop constraint if exists fk_sprint_backlog_entries_sprint;
drop index if exists ix_sprint_backlog_entries_sprint;

alter table if exists sprint_backlog_entries drop constraint if exists fk_sprint_backlog_entries_backlog_entries;
drop index if exists ix_sprint_backlog_entries_backlog_entries;

alter table if exists sprint_tasks drop constraint if exists fk_sprint_tasks_backlog_entries_id;
drop index if exists ix_sprint_tasks_backlog_entries_id;

alter table if exists sprint_tasks drop constraint if exists fk_sprint_tasks_sprint_id;
drop index if exists ix_sprint_tasks_sprint_id;

alter table if exists supervisors drop constraint if exists fk_supervisors_associations_id;

drop table if exists associations cascade;

drop table if exists backlog_entries cascade;

drop table if exists backlog_entries_sprint cascade;

drop table if exists comments cascade;

drop table if exists groups cascade;

drop table if exists history cascade;

drop table if exists meeting_members cascade;

drop table if exists meetings cascade;

drop table if exists members cascade;

drop table if exists members_tasks_doing cascade;

drop table if exists members_tasks_review cascade;

drop table if exists projects cascade;

drop table if exists projects_supervisors cascade;

drop table if exists releases cascade;

drop table if exists sprint cascade;

drop table if exists sprint_backlog_entries cascade;

drop table if exists sprint_tasks cascade;

drop table if exists supervisors cascade;

drop table if exists users cascade;

