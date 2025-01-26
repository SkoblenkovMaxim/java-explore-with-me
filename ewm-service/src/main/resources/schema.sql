DROP TABLE IF EXISTS events CASCADE ;
DROP TABLE IF EXISTS compilation CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
--DROP TABLE IF EXISTS location CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    user_id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    user_name  VARCHAR(255),
    user_email VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS categories
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(255) UNIQUE,
    CONSTRAINT pk_category PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events
(
    event_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation TEXT NOT NULL,
    category_id BIGINT NOT NULL,
    confirmed_requests Integer,
    created_on TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    description TEXT NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id BIGINT NOT NULL,
    --location BIGINT NOT NULL,
    lat DOUBLE PRECISION NOT NULL,
    lon DOUBLE PRECISION NOT NULL,
    paid BOOLEAN NOT NULL,
    participant_limit Integer NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN NOT NULL,
    state VARCHAR(50) NOT NULL,
    title VARCHAR(120) NOT NULL,
    FOREIGN KEY (category_id) REFERENCES categories (id) ON delete CASCADE,
    FOREIGN KEY (initiator_id) REFERENCES users (user_id) ON delete CASCADE
);

CREATE TABLE IF NOT EXISTS compilation
(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    title VARCHAR(255),
    pinned BOOLEAN,
    events VARCHAR(255)
);

--CREATE TABLE IF NOT EXISTS location
--(
--    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
--    lat FLOAT,
--   low FLOAT
--)