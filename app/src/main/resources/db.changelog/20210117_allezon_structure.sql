DROP TABLE IF EXISTS "user";
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS test1;

CREATE TABLE "role"
(
    id   INT,
    role VARCHAR NOT NULL UNIQUE,

    PRIMARY KEY (id)
);

INSERT INTO "role"
VALUES (1, 'admin'),
       (2, 'user');

CREATE TABLE "user"
(
    id       INT,
    role_id  INT     NOT NULL,

    email    VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT role_fk
        FOREIGN KEY (role_id)
            REFERENCES "role" (id)
);

INSERT INTO "user"
VALUES (1, 1, 'admin@gmail.com', 'admin');

CREATE TABLE section
(
    id   INT,
    name VARCHAR NOT NULL UNIQUE,

    PRIMARY KEY (id)
);

CREATE TABLE category
(
    id         INT,
    section_id INT     NOT NULL,

    name       VARCHAR NOT NULL UNIQUE,

    PRIMARY KEY (id),
    CONSTRAINT section_fk
        FOREIGN KEY (section_id)
            REFERENCES section (id)
);

CREATE TABLE auction
(
    id          INT,
    author      INT     NOT NULL,
    category_id INT     NOT NULL,

    title       VARCHAR NOT NULL,
    description VARCHAR NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT user_fk
        FOREIGN KEY (author)
            REFERENCES "user" (id),
    CONSTRAINT category_fk
        FOREIGN KEY (category_id)
            REFERENCES category (id)
);

CREATE TABLE auction_image
(
    id          INT,
    auction_id  INT     NOT NULL,

    path        VARCHAR NOT NULL,
    description VARCHAR NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT auction_fk
        FOREIGN KEY (auction_id)
            REFERENCES auction (id)
);

CREATE TABLE parameter
(
    id   INT,
    name VARCHAR NOT NULL,

    PRIMARY KEY (id)
);

CREATE TABLE auction_parameter
(
    id           INT,
    auction_id   INT     NOT NULL,
    parameter_id INT     NOT NULL,

    value        VARCHAR NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT auction_fk
        FOREIGN KEY (auction_id)
            REFERENCES auction (id),
    CONSTRAINT parameter_fk
        FOREIGN KEY (parameter_id)
            REFERENCES parameter (id)
);