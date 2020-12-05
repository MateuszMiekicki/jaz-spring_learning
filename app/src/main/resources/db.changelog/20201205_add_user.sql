DROP TABLE test1;

CREATE SCHEMA third_task

CREATE TABLE third_task.role(
  	id SERIAL PRIMARY KEY,
    username VARCHAR UNIQUE NOT NULL,
    password VARCHAR NOT NULL,
    role VARCHAR NOT NULL
);

CREATE TABLE third_task.user(
    id SERIAL PRIMARY KEY,
    role_id BIGINT NOT NULL,

    CONSTRAINT fk_role_id FOREIGN KEY (role_id) REFERENCES third_task.role(id)
);

CREATE TABLE third_task.account(
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES third_task.user(id)
);
