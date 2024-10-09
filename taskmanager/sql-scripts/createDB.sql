drop schema if exists `task_manager`;
create schema `task_manager`;

use `task_manager`;

drop table if exists `users`;

create table `users` (
	`id` int not null auto_increment,
    `username` varchar(45) default null,
    `first_name` varchar(45) default null,
    `last_name` varchar(45) default null,
    `email` varchar(255) default null,
    `password` varchar(255) default null,
    `enabled` boolean default true,
    unique key `USERNAME_UNIQUE` (`username`),
    primary key (`id`)
    ) engine=INNODB auto_increment=1 default charset=latin1;

    drop table if exists `task`;

    create table `task` (
    `id` int not null auto_increment,
    `task_name` varchar(255) default null,
    `start_date` date default null,
    `due_date` date default null,
    `user_id` int not null,
    `task_status` boolean default false,
    primary key (`id`),

    key FK_TASK_idx (`id`),

    constraint `FK_TASK`
    foreign key (`user_id`)
    references `users`(`id`)

    on delete no action on update no action

    ) engine=innodb auto_increment=1 charset=latin1;

    set FOREIGN_KEY_CHECKS=1;




    drop table if exists `roles`;
    create table `roles` (
    `id` int not null auto_increment,
    `name` varchar(50) default null,
    primary key(`id`)
    ) engine=InnoDB	auto_increment=1 default charset=latin1;

    insert into `roles` (name)
    values
    ('ROLE_USER'),('ROLE_ADMIN');

    set foreign_key_checks = 0;

    drop table if exists `user_roles`;

    create table `user_roles` (
    `user_id` int(11) not null,
    `role_id` int(11) not null,
    primary key (`user_id`,`role_id`),

    key `FK_ROLE_idx` (`role_id`),

    constraint `FK_ROLE` foreign key (`role_id`)
    references `roles`(`id`)
    on delete no action on update no action,

    constraint `FK_USER` foreign key (`user_id`)
    references `users`(`id`)
    on delete no action on update no action
    ) engine=innodb default charset=latin1;

    set FOREIGN_KEY_CHECKS = 1;

    -- Create a new admin user
    insert into `users` (username, first_name, last_name, email, password, enabled)
    values ('admin', 'Matt', 'Hays', 'mhays@gmail.com', '{bcrypt}$2a$10$DGiTvZk5rdtEelwoc6SiaufYwrsciO.UawwkNY09Y220iAhsHqPqK', true);

    -- Get the ID of the newly created user
    set @user_id = LAST_INSERT_ID();

    -- Get the ID of the admin role
    set @role_id = (select id from `roles` where name = 'ROLE_ADMIN');

    -- Assign the admin role to the new user
    insert into `user_roles` (user_id, role_id)
    values (@user_id, @role_id);




