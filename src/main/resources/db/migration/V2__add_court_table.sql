CREATE TABLE court (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL UNIQUE,
                       address VARCHAR(255),
                       max_capacity INTEGER NOT NULL CHECK (max_capacity >= 1),
                       current_bikes INTEGER DEFAULT 0 CHECK (current_bikes >= 0),
                       acess_code INTEGER NOT NULL
);