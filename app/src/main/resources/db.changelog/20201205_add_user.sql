DROP TABLE test1;

CREATE TABLE user_role(
  	id SERIAL PRIMARY KEY,
    role VARCHAR UNIQUE NOT NULL
);

INSERT INTO user_role (role) VALUES ('admin');
INSERT INTO user_role (role) VALUES ('teacher');
INSERT INTO user_role (role) VALUES ('student');

CREATE TABLE  "user"(
    id SERIAL PRIMARY KEY,
    username VARCHAR UNIQUE NOT NULL,
    password VARCHAR NOT NULL,
    id_role INT NOT NULL,

    CONSTRAINT fk_idRole
        FOREIGN KEY (id_role)
            REFERENCES user_role(id)
);
