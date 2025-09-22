DROP TABLE IF EXISTS pending CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS bike CASCADE;
DROP TABLE IF EXISTS court CASCADE;

CREATE TABLE court (
                       acess_code VARCHAR(150) PRIMARY KEY,
                       name VARCHAR(100) NOT NULL UNIQUE,
                       address VARCHAR(255),
                       max_capacity INTEGER NOT NULL CHECK (max_capacity >= 1),
                       current_bikes INTEGER DEFAULT 0
);

CREATE TABLE bike (
                      placa VARCHAR(255) PRIMARY KEY,
                      id_chassi BIGINT NOT NULL,
                      localizacao VARCHAR(50) NOT NULL,
                      status VARCHAR(50) NOT NULL,
                      modelo VARCHAR(50) NOT NULL,
                      court_acess_code VARCHAR(150) NOT NULL,

                      CONSTRAINT fk_bike_court
                          FOREIGN KEY (court_acess_code)
                              REFERENCES court(acess_code)
                              ON DELETE CASCADE
);

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       name VARCHAR(255),
                       court_acess_code VARCHAR(150) NOT NULL,
                       role VARCHAR(50) NOT NULL,

                       CONSTRAINT fk_user_court
                           FOREIGN KEY (court_acess_code)
                               REFERENCES court(acess_code)
                               ON DELETE CASCADE
);

CREATE TABLE pending (
                         id BIGSERIAL PRIMARY KEY,
                         number BIGINT NOT NULL CHECK (number >= 1 AND number <= 3),
                         description VARCHAR(400),
                         status VARCHAR(50) NOT NULL,
                         bike_id VARCHAR(255) NOT NULL,

                         CONSTRAINT fk_pending_bike
                             FOREIGN KEY (bike_id)
                                 REFERENCES bike(placa)
                                 ON DELETE CASCADE
);