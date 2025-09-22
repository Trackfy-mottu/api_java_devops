CREATE TABLE USERS (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL CHECK (LENGTH(password) >= 1),
    name VARCHAR(255) CHECK (LENGTH(name) >= 1),
    role VARCHAR(50) NOT NULL,

    CONSTRAINT chk_username_email CHECK (username ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$')
);

CREATE TABLE bike (
    placa VARCHAR(20) PRIMARY KEY,
    id_chassi BIGINT NOT NULL UNIQUE,
    localizacao VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    patio VARCHAR(100),
    id_usuario BIGINT CHECK (id_usuario > 0),

    CONSTRAINT fk_bike_usuario FOREIGN KEY (id_usuario) REFERENCES USERS(id)
);

CREATE TABLE pending (
    id BIGSERIAL PRIMARY KEY,
    number BIGINT NOT NULL CHECK (number >= 1 AND number <= 3),
    description VARCHAR(400),
    status VARCHAR(50) NOT NULL,
    bike_id VARCHAR(20) NOT NULL,

    CONSTRAINT fk_pending_bike FOREIGN KEY (bike_id) REFERENCES bike(placa)
);