-- ============================================================
-- Tu Evento - Script de base de datos (diseno) / Database script (design)
-- ES: Union de los 13 scripts de diseno por modulo de 04_Diseno_Software/SQL/,
--     copiados sin cambios. Cada modulo es independiente y algunos repiten
--     tablas (p. ej. user), por lo que este archivo no se ejecuta de una vez.
--     El esquema implementado esta en los changesets de Liquibase del backend.
-- EN: The 13 per-module design scripts from 04_Diseno_Software/SQL/, copied
--     unchanged. Each module is standalone and some repeat tables (e.g. user),
--     so this file is not meant to run in one go. The implemented schema is in
--     the backend Liquibase changesets.
-- ============================================================

-- ------------------------------------------------------------
-- Security module.sql (sha256 0c1a0ef72bf631d863b34af0c75b09e08f2e1cf0220a74f5dcfb2d692c644328)
-- ------------------------------------------------------------
-- =========================
-- ENUMS
-- =========================

CREATE TYPE organizer_petition_status AS ENUM (
    'Pending',
    'Approved',
    'Rejected'
);

-- =========================
-- USER STATUS
-- =========================

CREATE TABLE user_status (
    user_status_id SERIAL PRIMARY KEY,
    code VARCHAR(30) NOT NULL,
    name VARCHAR(60) NOT NULL,
    description VARCHAR(150) NOT NULL
);

-- =========================
-- ROLE
-- =========================

CREATE TABLE role (
    role_id SERIAL PRIMARY KEY,
    code VARCHAR(30) NOT NULL,
    name VARCHAR(60) NOT NULL,
    description VARCHAR(150) NOT NULL
);

-- =========================
-- PERMISSION
-- =========================

CREATE TABLE permission (
    permission_id SERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(80) NOT NULL,
    description VARCHAR(150) NOT NULL
);

-- =========================
-- ROLE PERMISSION
-- =========================

CREATE TABLE role_permission (
    role_permission_id SERIAL PRIMARY KEY,
    permission_id INT NOT NULL,
    role_id INT NOT NULL,

    FOREIGN KEY(permission_id)
        REFERENCES permission(permission_id),

    FOREIGN KEY(role_id)
        REFERENCES role(role_id)
);

-- =========================
-- USER
-- =========================

CREATE TABLE "user" (
    user_id SERIAL PRIMARY KEY,
    user_status_id INT,
    role_id INT,

    alias VARCHAR(50) NOT NULL UNIQUE,
    activated BOOLEAN DEFAULT FALSE,

    FOREIGN KEY(user_status_id)
        REFERENCES user_status(user_status_id),

    FOREIGN KEY(role_id)
        REFERENCES role(role_id)
);

-- =========================
-- USER STATUS HISTORY
-- =========================

CREATE TABLE user_status_history (
    user_status_history_id SERIAL PRIMARY KEY,

    user_id INT,
    user_status_id INT,

    reason VARCHAR(150) NOT NULL,

    FOREIGN KEY(user_id)
        REFERENCES "user"(user_id),

    FOREIGN KEY(user_status_id)
        REFERENCES user_status(user_status_id)
);

-- =========================
-- OAUTH ACCOUNT
-- =========================

CREATE TABLE oauth_account (
    oauth_account_id SERIAL PRIMARY KEY,

    user_id INT NOT NULL,

    provider VARCHAR(40) NOT NULL,
    provider_user_id VARCHAR(100),

    email VARCHAR(255),
    linked_at TIMESTAMP,

    FOREIGN KEY(user_id)
        REFERENCES "user"(user_id),

    UNIQUE(user_id, provider),
    UNIQUE(provider, provider_user_id)
);

-- =========================
-- AUTH SESSION
-- =========================

CREATE TABLE auth_session (
    auth_session_id SERIAL PRIMARY KEY,

    user_id INT,

    access_token VARCHAR(512),

    issued_at TIMESTAMP,
    expires_at TIMESTAMP,

    ip_address VARCHAR(45),
    user_agent VARCHAR(200),

    revoked BOOLEAN DEFAULT FALSE,
    revoked_at TIMESTAMP,

    FOREIGN KEY(user_id)
        REFERENCES "user"(user_id)
);

-- =========================
-- REFRESH TOKEN
-- =========================

CREATE TABLE refresh_token (
    refresh_token_id SERIAL PRIMARY KEY,

    user_id INT,
    auth_session_id INT,

    token VARCHAR(512),

    issued_at TIMESTAMP,
    expires_at TIMESTAMP,

    revoked BOOLEAN DEFAULT FALSE,
    revoked_at TIMESTAMP,

    FOREIGN KEY(user_id)
        REFERENCES "user"(user_id),

    FOREIGN KEY(auth_session_id)
        REFERENCES auth_session(auth_session_id)
);

-- =========================
-- ACCOUNT ACTIVATION
-- =========================

CREATE TABLE account_activation (
    user_id INT PRIMARY KEY,

    activation_code VARCHAR(6),

    activated BOOLEAN DEFAULT FALSE,
    expires_at TIMESTAMP,
    created_at TIMESTAMP,

    FOREIGN KEY(user_id)
        REFERENCES "user"(user_id)
);

-- =========================
-- ACCOUNT LOCKOUT
-- =========================

CREATE TABLE account_lockout (
    account_lockout_id SERIAL PRIMARY KEY,

    user_id INT,

    failed_attempts INT,
    window_start TIMESTAMP,
    locked_until TIMESTAMP,

    FOREIGN KEY(user_id)
        REFERENCES "user"(user_id)
);

-- =========================
-- ORGANIZER PETITION
-- =========================

CREATE TABLE organizer_petition (
    organizer_petition_id SERIAL PRIMARY KEY,

    user_id INT NOT NULL,

    document BYTEA NOT NULL,
    application_date TIMESTAMP,

    status organizer_petition_status,

    FOREIGN KEY(user_id)
        REFERENCES "user"(user_id)
);

CREATE UNIQUE INDEX unique_pending_petition
ON organizer_petition(user_id)
WHERE status = 'Pending';

-- =========================
-- RECOVER PASSWORD
-- =========================

CREATE TABLE recover_password (
    recover_password_id SERIAL PRIMARY KEY,

    user_id INT,

    code VARCHAR(6),
    code_status BOOLEAN DEFAULT FALSE,

    expires_at TIMESTAMP,
    created_at TIMESTAMP,

    last_password_hash VARCHAR(255),

    FOREIGN KEY(user_id)
        REFERENCES "user"(user_id)
);

-- =========================
-- PASSWORD HISTORY
-- =========================

CREATE TABLE password_history (
    password_history_id SERIAL PRIMARY KEY,

    user_id INT,

    password_hash VARCHAR(255),
    changed_at TIMESTAMP,

    FOREIGN KEY(user_id)
        REFERENCES "user"(user_id)
);

-- =========================
-- LOGIN CREDENTIALS
-- =========================

CREATE TABLE login_credentials (
    login_credentials_id SERIAL PRIMARY KEY,

    user_id INT NOT NULL,

    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,

    last_login_at TIMESTAMP,

    FOREIGN KEY(user_id)
        REFERENCES "user"(user_id)
);

-- ------------------------------------------------------------
-- Profile module.sql (sha256 ade2d2c8681e0b206d86d24f0503ee7350c6637c141ba05bcc8f3e17ea34f672)
-- ------------------------------------------------------------
-------------------------
-- ENUMS
-------------------------

CREATE TYPE event_status_enum AS ENUM ('Draft','Published','Cancelled','Completed');

CREATE TYPE payment_status_enum AS ENUM ('pending','approved','rejected','error','refunded');

CREATE TYPE wallet_transaction_type_enum AS ENUM ('recharge','payment','refund');

CREATE TYPE wallet_transaction_status_enum AS ENUM ('pending','approved','rejected','error');

CREATE TYPE ticket_status_enum AS ENUM ('pending','approved','rejected','refunded');

-------------------------
-- LANGUAGE
-------------------------

CREATE TABLE language (
language_id INT PRIMARY KEY,
code VARCHAR(10) NOT NULL,
name VARCHAR(50) NOT NULL,
is_active BOOLEAN DEFAULT TRUE,
created_at TIMESTAMP,
created_by INT,
updated_at TIMESTAMP,
updated_by INT
);

-------------------------
-- THEME
-------------------------

CREATE TABLE theme (
theme_id INT PRIMARY KEY,
name VARCHAR(100) NOT NULL,
description VARCHAR(255) NOT NULL,
default_palette JSONB,
created_at TIMESTAMP,
created_by INT
);

-------------------------
-- USER
-------------------------

CREATE TABLE "user" (
user_id INT PRIMARY KEY,
user_status_id INT,
role_id INT,
alias VARCHAR(50) NOT NULL UNIQUE,
activated BOOLEAN DEFAULT FALSE
);

-------------------------
-- PREFERENCES
-------------------------

CREATE TABLE preferences (
preference_id INT PRIMARY KEY,
user_id INT NOT NULL,
language_id INT NOT NULL,
theme_id INT NOT NULL,
notifications BOOLEAN DEFAULT FALSE,

FOREIGN KEY (user_id) REFERENCES "user"(user_id),
FOREIGN KEY (language_id) REFERENCES language(language_id),
FOREIGN KEY (theme_id) REFERENCES theme(theme_id)
);

-------------------------
-- ACTIVITY LOG
-------------------------

CREATE TABLE activity_log (
log_id INT PRIMARY KEY,
user_id INT NOT NULL,
module VARCHAR(50),
action VARCHAR(100),
reference_id INT,
occurred_at TIMESTAMP,

FOREIGN KEY (user_id) REFERENCES "user"(user_id)
);

-------------------------
-- WALLET
-------------------------

CREATE TABLE wallet (
wallet_id INT PRIMARY KEY,
user_id INT NOT NULL,
balance DECIMAL(10,2) DEFAULT 0,
currency VARCHAR(3) DEFAULT 'COP',

FOREIGN KEY (user_id) REFERENCES "user"(user_id)
);

-------------------------
-- WALLET TRANSACTION
-------------------------

CREATE TABLE wallet_transaction (
transaction_id INT PRIMARY KEY,
wallet_id INT NOT NULL,
type wallet_transaction_type_enum,
amount DECIMAL(10,2),
status wallet_transaction_status_enum,
created_at TIMESTAMP,

FOREIGN KEY (wallet_id) REFERENCES wallet(wallet_id)
);

-------------------------
-- WALLET SUMMARY
-------------------------

CREATE TABLE wallet_summary (
summary_id INT PRIMARY KEY,
user_id INT NOT NULL,
wallet_id INT NOT NULL,
transaction_id INT NOT NULL,
type wallet_transaction_type_enum,
amount DECIMAL(10,2) NOT NULL,
occurred_at TIMESTAMP,

FOREIGN KEY (user_id) REFERENCES "user"(user_id),
FOREIGN KEY (wallet_id) REFERENCES wallet(wallet_id),
FOREIGN KEY (transaction_id) REFERENCES wallet_transaction(transaction_id)
);

-------------------------
-- EVENT
-------------------------

CREATE TABLE event (
event_id INT PRIMARY KEY,
user_id INT NOT NULL,
site_id INT NOT NULL,
event_name VARCHAR(100) NOT NULL,
description VARCHAR(255) NOT NULL,
start_date DATE NOT NULL,
finish_date DATE NOT NULL,
status event_status_enum,
is_public BOOLEAN DEFAULT TRUE,
available_seats INT NOT NULL,

FOREIGN KEY (user_id) REFERENCES "user"(user_id),

CHECK (finish_date > start_date)
);

-------------------------
-- CITY
-------------------------

CREATE TABLE city (
city_id INT PRIMARY KEY,
department_id INT NOT NULL,
name VARCHAR(100) NOT NULL,
code VARCHAR(10)
);

-------------------------
-- PROFILE
-------------------------

CREATE TABLE profile (
profile_id INT PRIMARY KEY,
user_id INT NOT NULL,
city_id INT NOT NULL,
full_name VARCHAR(100) NOT NULL,
photo_url VARCHAR(255) NOT NULL,
bio VARCHAR(255),

FOREIGN KEY (user_id) REFERENCES "user"(user_id),
FOREIGN KEY (city_id) REFERENCES city(city_id)
);

-------------------------
-- PROFILE LOG
-------------------------

CREATE TABLE profile_log (
log_id INT PRIMARY KEY,
profile_id INT NOT NULL,
action VARCHAR(50),
old_value VARCHAR(255),
new_value VARCHAR(255),
occurred_at TIMESTAMP,

FOREIGN KEY (profile_id) REFERENCES profile(profile_id)
);

-------------------------
-- REVIEW
-------------------------

CREATE TABLE review (
review_id INT PRIMARY KEY,
user_id INT NOT NULL,
event_id INT NOT NULL,
rating INT CHECK (rating BETWEEN 1 AND 5),
comment VARCHAR(255) NOT NULL,
created_at TIMESTAMP,
updated_at TIMESTAMP,

FOREIGN KEY (user_id) REFERENCES "user"(user_id),
FOREIGN KEY (event_id) REFERENCES event(event_id)
);

-------------------------
-- ORDER 
-------------------------

CREATE TABLE "order" (
order_id INT PRIMARY KEY,
user_id INT NOT NULL,
event_id INT NOT NULL,
total_amount DECIMAL(10,2) NOT NULL,
currency VARCHAR(3) DEFAULT 'COP',
status payment_status_enum,
created_at TIMESTAMP,

FOREIGN KEY (user_id) REFERENCES "user"(user_id),
FOREIGN KEY (event_id) REFERENCES event(event_id)
);

-------------------------
-- PAYMENT
-------------------------

CREATE TABLE payment (
payment_id INT PRIMARY KEY,
order_id INT NOT NULL,
gateway VARCHAR(50),
gateway_transaction_id VARCHAR(100),
status payment_status_enum,
amount DECIMAL(10,2) NOT NULL,
payment_method VARCHAR(50),
processed_at TIMESTAMP,

FOREIGN KEY (order_id) REFERENCES "order"(order_id)
);

-------------------------
-- TICKET
-------------------------

CREATE TABLE ticket (
ticket_id INT PRIMARY KEY,
event_id INT NOT NULL,
user_id INT NOT NULL,
order_id INT NOT NULL,
code VARCHAR(20) NOT NULL,
qr_code VARCHAR(255),
status ticket_status_enum,
expiration_date DATE,
total_price DECIMAL(10,2) NOT NULL,

FOREIGN KEY (event_id) REFERENCES event(event_id),
FOREIGN KEY (user_id) REFERENCES "user"(user_id),
FOREIGN KEY (order_id) REFERENCES "order"(order_id)
);

-------------------------
-- PURCHASE HISTORY
-------------------------

CREATE TABLE purchase_history (
purchase_id INT PRIMARY KEY,
user_id INT NOT NULL,
ticket_id INT NOT NULL,
payment_id INT NOT NULL,
amount DECIMAL(10,2) NOT NULL,
status payment_status_enum,
purchased_at TIMESTAMP,

FOREIGN KEY (user_id) REFERENCES "user"(user_id),
FOREIGN KEY (ticket_id) REFERENCES ticket(ticket_id),
FOREIGN KEY (payment_id) REFERENCES payment(payment_id)
);

-- ------------------------------------------------------------
-- Geolocation module.sql (sha256 3724b9dde7cd57c034d0b06b384fca604dd628fbfd37cf26d503040f7b490cfe)
-- ------------------------------------------------------------
-- Tabla department
CREATE TABLE department (
    department_id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(10)
);

-- Tabla city
CREATE TABLE city (
    city_id INT PRIMARY KEY,
    department_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(10),
    
    CONSTRAINT fk_city_department
        FOREIGN KEY (department_id)
        REFERENCES department(department_id)
);

-- Tabla site
CREATE TABLE site (
    site_id INT PRIMARY KEY,
    city_id INT NOT NULL,
    name VARCHAR(150) NOT NULL,
    address VARCHAR(200) NOT NULL,
    capacity INT,
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6),

    CONSTRAINT fk_site_city
        FOREIGN KEY (city_id)
        REFERENCES city(city_id)
);

-- Tabla profile
CREATE TABLE profile (
    profile_id INT PRIMARY KEY,
    user_id INT NOT NULL,
    city_id INT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    photo_url VARCHAR(255) NOT NULL,
    bio VARCHAR(255),

    CONSTRAINT fk_profile_city
        FOREIGN KEY (city_id)
        REFERENCES city(city_id)
);

-- Tabla event
CREATE TABLE event (
    event_id INT PRIMARY KEY,
    user_id INT NOT NULL,
    site_id INT NOT NULL,
    event_name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    finish_date DATE NOT NULL,
    status ENUM('Draft','Published','Cancelled','Completed'),
    is_public BOOLEAN DEFAULT TRUE,
    available_seats INT NOT NULL,

    CONSTRAINT fk_event_site
        FOREIGN KEY (site_id)
        REFERENCES site(site_id),

    CONSTRAINT chk_event_dates
        CHECK (finish_date > start_date),

    CONSTRAINT unique_event
        UNIQUE (event_name, start_date, site_id)
);

-- ------------------------------------------------------------
-- Category module.sql (sha256 e79bad91faa092f47ddb3db50ac35cd4dc1f97fb2a67e8f49784a15df6200225)
-- ------------------------------------------------------------
-- =========================
-- TABLA CATEGORY
-- =========================
CREATE TABLE category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(100) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    visible BOOLEAN DEFAULT TRUE,
    dad_id INT,

    CONSTRAINT fk_category_parent
        FOREIGN KEY (dad_id)
        REFERENCES category(category_id)
        ON DELETE SET NULL
        ON UPDATE CASCADE
);

-- =========================
-- TABLA EVENT
-- =========================
CREATE TABLE event (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    site_id INT NOT NULL,

    event_name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,

    start_date DATE NOT NULL,
    finish_date DATE NOT NULL,

    status ENUM('DRAFT','PUBLISHED','CANCELLED','COMPLETED'),

    is_public BOOLEAN DEFAULT TRUE,
    available_seats INT NOT NULL,

    CONSTRAINT chk_event_dates
        CHECK (finish_date > start_date),

    CONSTRAINT unique_event
        UNIQUE (event_name, start_date, site_id)
);

-- =========================
-- TABLA CATEGORY_EVENT
-- =========================
CREATE TABLE category_event (
    category_event_id INT AUTO_INCREMENT PRIMARY KEY,

    category_id INT NOT NULL,
    event_id INT NOT NULL,

    CONSTRAINT fk_category_event_category
        FOREIGN KEY (category_id)
        REFERENCES category(category_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_category_event_event
        FOREIGN KEY (event_id)
        REFERENCES event(event_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT unique_category_event
        UNIQUE (category_id, event_id)
);

-- ------------------------------------------------------------
-- Event module.sql (sha256 e468284cb0ffa29d712196a277c04c4bca421a8f99cea823306178d54fa8df6b)
-- ------------------------------------------------------------
-- =========================
-- TABLE: user
-- =========================
CREATE TABLE user (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_status_id INT,
    role_id INT,
    alias VARCHAR(50) NOT NULL UNIQUE,
    activated BOOLEAN DEFAULT FALSE
);

-- =========================
-- TABLE: site
-- =========================
CREATE TABLE site (
    site_id INT AUTO_INCREMENT PRIMARY KEY,
    city_id INT NOT NULL,
    name VARCHAR(150) NOT NULL,
    address VARCHAR(200) NOT NULL,
    capacity INT,
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6)
);

-- =========================
-- TABLE: event
-- =========================
CREATE TABLE event (
    event_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    site_id INT NOT NULL,
    event_name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    finish_date DATE NOT NULL,
    status ENUM('DRAFT','PUBLISHED','CANCELLED','COMPLETED'),
    is_public BOOLEAN DEFAULT TRUE,
    available_seats INT NOT NULL,

    CONSTRAINT fk_event_user
        FOREIGN KEY (user_id) REFERENCES user(user_id),

    CONSTRAINT fk_event_site
        FOREIGN KEY (site_id) REFERENCES site(site_id),

    CONSTRAINT chk_event_dates
        CHECK (finish_date > start_date),

    CONSTRAINT unique_event
        UNIQUE (event_name, start_date, site_id)
);

-- =========================
-- TABLE: event_layout
-- =========================
CREATE TABLE event_layout (
    event_layout_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    layout_data JSON NOT NULL,

    FOREIGN KEY (event_id)
        REFERENCES event(event_id)
);

-- =========================
-- TABLE: event_media
-- =========================
CREATE TABLE event_media (
    media_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    img_url VARCHAR(255) NOT NULL,

    FOREIGN KEY (event_id)
        REFERENCES event(event_id)
);

-- =========================
-- TABLE: event_media_log
-- =========================
CREATE TABLE event_media_log (
    media_log_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    media_id INT NOT NULL,
    img_url VARCHAR(255) NOT NULL,
    version INT NOT NULL,
    is_visible BOOLEAN DEFAULT TRUE,

    FOREIGN KEY (event_id)
        REFERENCES event(event_id),

    FOREIGN KEY (media_id)
        REFERENCES event_media(media_id)
);

-- =========================
-- TABLE: event_rating
-- =========================
CREATE TABLE event_rating (
    rating_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    user_id INT NOT NULL,
    rating INT,
    comment TEXT NOT NULL,
    is_visible BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,

    FOREIGN KEY (event_id)
        REFERENCES event(event_id),

    FOREIGN KEY (user_id)
        REFERENCES user(user_id),

    CHECK (rating BETWEEN 1 AND 5)
);

-- =========================
-- TABLE: event_comment_reply
-- =========================
CREATE TABLE event_comment_reply (
    reply_id INT AUTO_INCREMENT PRIMARY KEY,
    parent_reply_id INT,
    rating_id INT NOT NULL,
    user_id INT NOT NULL,
    reply_text TEXT NOT NULL,
    is_visible BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,

    FOREIGN KEY (parent_reply_id)
        REFERENCES event_comment_reply(reply_id),

    FOREIGN KEY (rating_id)
        REFERENCES event_rating(rating_id),

    FOREIGN KEY (user_id)
        REFERENCES user(user_id)
);

-- =========================
-- TABLE: event_status_log
-- =========================
CREATE TABLE event_status_log (
    status_log_id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    old_status ENUM('DRAFT','PUBLISHED','CANCELLED','COMPLETED'),
    new_status ENUM('DRAFT','PUBLISHED','CANCELLED','COMPLETED'),
    changed_at TIMESTAMP,
    changed_by INT,

    FOREIGN KEY (event_id)
        REFERENCES event(event_id),

    FOREIGN KEY (changed_by)
        REFERENCES user(user_id)
);

-- ------------------------------------------------------------
-- Section module.sql (sha256 f2bc9c49eff3b269047e14f98137826fdc74c69b89b7bbc11b5bc5cf668d77ad)
-- ------------------------------------------------------------
-- =====================================
-- ENUM
-- =====================================
CREATE TYPE event_status AS ENUM (
    'Draft',
    'Published',
    'Canceled',
    'Completed'
);

CREATE TABLE section_type (
    section_type_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- =====================================
-- Tabla: event
-- =====================================
CREATE TABLE event (
    event_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    user_id INT NOT NULL,
    site_id INT NOT NULL,

    event_name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,

    start_date DATE NOT NULL,
    finish_date DATE NOT NULL,

    status event_status,
    is_public BOOLEAN DEFAULT TRUE,

    available_seats INT NOT NULL,

    -- Restricciones
    CONSTRAINT chk_event_dates
        CHECK (finish_date > start_date),

    CONSTRAINT unique_event_name_date_site
        UNIQUE (event_name, start_date, site_id)
);

-- =====================================
-- Tabla: event_section
-- =====================================
CREATE TABLE event_section (
    event_section_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,

    event_id INT NOT NULL,
    section_type_id INT NOT NULL,

    capacity INT NOT NULL,
    available_seats INT NOT NULL,

    price DECIMAL(10,2) NOT NULL,

    is_active BOOLEAN DEFAULT TRUE,

    -- Foreign Keys
    CONSTRAINT fk_event_section_event
        FOREIGN KEY (event_id)
        REFERENCES event(event_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_event_section_section_type
        FOREIGN KEY (section_type_id)
        REFERENCES section_type(section_type_id),

    -- Restricciones
    CONSTRAINT chk_capacity_positive
        CHECK (capacity > 0),

    CONSTRAINT chk_available_seats
        CHECK (available_seats <= capacity),

    CONSTRAINT chk_price_positive
        CHECK (price >= 0),

    CONSTRAINT unique_event_section
        UNIQUE (event_id, section_type_id)
);

-- ------------------------------------------------------------
-- Seat module.sql (sha256 8552f2df1289f4baa9820480ee85bf5de83b7b09cb5257b68513fc3fb40ef050)
-- ------------------------------------------------------------
-----------------------
-- ENUMS
-----------------------

CREATE TYPE seat_status_enum AS ENUM (
'available',
'reserved',
'sold',
'courtesy'
);

CREATE TYPE seat_type_enum AS ENUM (
'regular',
'courtesy'
);

-----------------------
-- USER (referenciado)
-----------------------

CREATE TABLE "user" (
user_id INT PRIMARY KEY,
user_status_id INT,
role_id INT,
alias VARCHAR(50) NOT NULL UNIQUE,
activated BOOLEAN DEFAULT FALSE
);

-----------------------
-- EVENT SECTION
-----------------------

CREATE TABLE event_section (
event_section_id INT PRIMARY KEY,
event_id INT,
section_type_id INT,
capacity INT NOT NULL,
available_seats INT NOT NULL,
price DECIMAL(10,2) NOT NULL,
is_active BOOLEAN DEFAULT TRUE
);

-----------------------
-- SEAT BLOCK
-----------------------

CREATE TABLE seat_block (
seat_block_id INT PRIMARY KEY,
event_section_id INT NOT NULL,
name VARCHAR(100) NOT NULL,
capacity INT NOT NULL,

FOREIGN KEY (event_section_id)
REFERENCES event_section(event_section_id)
);

-----------------------
-- SEAT
-----------------------

CREATE TABLE seat (
seat_id INT PRIMARY KEY,
seat_block_id INT NOT NULL,
event_section_id INT NOT NULL,
code VARCHAR(20) NOT NULL,
row INT NOT NULL,
position INT NOT NULL,
type seat_type_enum,
status seat_status_enum,

FOREIGN KEY (seat_block_id)
REFERENCES seat_block(seat_block_id),

FOREIGN KEY (event_section_id)
REFERENCES event_section(event_section_id)
);

-----------------------
-- SEAT LOG
-----------------------

CREATE TABLE seat_log (
seat_log_id INT PRIMARY KEY,
seat_id INT NOT NULL,
old_status seat_status_enum,
new_status seat_status_enum,
changed_at TIMESTAMP,
changed_by INT,
reason VARCHAR(255),

FOREIGN KEY (seat_id)
REFERENCES seat(seat_id),

FOREIGN KEY (changed_by)
REFERENCES "user"(user_id)
);

-- ------------------------------------------------------------
-- Ticket module.sql (sha256 57ed68d13b257b06dc5907fca07a9a042f8e7f5f2dc29a54b828e9543e3ac897)
-- ------------------------------------------------------------
-- =========================
-- ENUMS
-- =========================

CREATE TYPE seat_type AS ENUM (
    'regular',
    'courtesy'
);

CREATE TYPE seat_status AS ENUM (
    'available',
    'reserved',
    'sold',
    'courtesy'
);

CREATE TYPE ticket_status AS ENUM (
    'draft',
    'payment_pending',
    'paid',
    'cancelled',
    'refunded',
    'used'
);

CREATE TYPE order_status AS ENUM (
    'pending',
    'processing',
    'paid',
    'failed',
    'refunded'
);

-- =========================
-- EVENT
-- =========================

CREATE TABLE event (
    event_id SERIAL PRIMARY KEY,

    user_id INT NOT NULL,
    site_id INT NOT NULL,

    event_name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,

    start_date DATE NOT NULL,
    finish_date DATE NOT NULL,

    status VARCHAR(20),

    is_public BOOLEAN DEFAULT TRUE,
    available_seats INT NOT NULL,

    CHECK (finish_date > start_date),

    UNIQUE(event_name, start_date, site_id)
);

-- =========================
-- ORDER
-- =========================

CREATE TABLE "order" (
    order_id SERIAL PRIMARY KEY,

    user_id INT NOT NULL,
    event_id INT NOT NULL,

    total_amount DECIMAL(10,2) NOT NULL,

    currency VARCHAR(3) DEFAULT 'COP',

    status order_status,

    created_at TIMESTAMP,

    FOREIGN KEY(user_id)
        REFERENCES "user"(user_id),

    FOREIGN KEY(event_id)
        REFERENCES event(event_id)
);

-- =========================
-- TICKET
-- =========================

CREATE TABLE ticket (
    ticket_id SERIAL PRIMARY KEY,

    event_id INT NOT NULL,
    user_id INT NOT NULL,
    order_id INT NOT NULL,

    code VARCHAR(20) NOT NULL,
    qr_code VARCHAR(255),

    status ticket_status,

    expiration_date DATE,

    total_price DECIMAL(10,2) NOT NULL,

    FOREIGN KEY(event_id)
        REFERENCES event(event_id),

    FOREIGN KEY(user_id)
        REFERENCES "user"(user_id),

    FOREIGN KEY(order_id)
        REFERENCES "order"(order_id)
);

-- =========================
-- SEAT
-- =========================

CREATE TABLE seat (
    seat_id SERIAL PRIMARY KEY,

    seat_block_id INT NOT NULL,
    event_section_id INT NOT NULL,

    code VARCHAR(20) NOT NULL,
    row INT NOT NULL,
    position INT NOT NULL,

    type seat_type,
    status seat_status
);

-- =========================
-- SEAT_TICKET
-- =========================

CREATE TABLE seat_ticket (
    seat_id INT NOT NULL,
    ticket_id INT NOT NULL,

    price DECIMAL(10,2) NOT NULL,

    PRIMARY KEY(seat_id, ticket_id),

    FOREIGN KEY(seat_id)
        REFERENCES seat(seat_id),

    FOREIGN KEY(ticket_id)
        REFERENCES ticket(ticket_id)
);

-- =========================
-- TICKET_CHECKIN
-- =========================

CREATE TABLE ticket_checkin (
    checkin_id SERIAL PRIMARY KEY,

    ticket_id INT NOT NULL,

    checkin_time TIMESTAMP,

    validated_by INT,

    FOREIGN KEY(ticket_id)
        REFERENCES ticket(ticket_id),

    FOREIGN KEY(validated_by)
        REFERENCES "user"(user_id)
);

-- =========================
-- TICKET_LOG
-- =========================

CREATE TABLE ticket_log (
    ticket_log_id SERIAL PRIMARY KEY,

    ticket_id INT NOT NULL,

    old_status ticket_status,
    new_status ticket_status,

    changed_at TIMESTAMP,

    changed_by INT,

    reason VARCHAR(255),

    FOREIGN KEY(ticket_id)
        REFERENCES ticket(ticket_id),

    FOREIGN KEY(changed_by)
        REFERENCES "user"(user_id)
);

-- ------------------------------------------------------------
-- Payment module.sql (sha256 8deccc15ad4919a1528c0f673826df28c044845c83394bab9bf1bbcc07e63155)
-- ------------------------------------------------------------
CREATE TYPE payment_status_enum AS ENUM (
'pending',
'processing',
'paid',
'failed',
'refunded'
);

CREATE TYPE refund_status_enum AS ENUM (
'requested',
'approved',
'processed',
'failed'
);

CREATE TYPE order_status_enum AS ENUM (
'pending',
'processing',
'paid',
'failed',
'refunded'
);

CREATE TYPE payment_method_enum AS ENUM (
'card',
'pse',
'nequi',
'cash'
);

CREATE TYPE gateway_enum AS ENUM (
'wompi'
);

CREATE TYPE event_status_enum AS ENUM (
'Draft',
'Published',
'Cancelled',
'Completed'
);


-- USER 

CREATE TABLE "user" (
user_id INT PRIMARY KEY,
user_status_id INT,
role_id INT,
alias VARCHAR(50) NOT NULL UNIQUE,
activated BOOLEAN DEFAULT FALSE
);

-- EVENT

CREATE TABLE event (
event_id INT PRIMARY KEY,
user_id INT NOT NULL,
site_id INT NOT NULL,
event_name VARCHAR(100) NOT NULL,
description VARCHAR(255) NOT NULL,
start_date DATE NOT NULL,
finish_date DATE NOT NULL,
status event_status_enum,
is_public BOOLEAN DEFAULT TRUE,
available_seats INT NOT NULL,

CONSTRAINT fk_event_user
FOREIGN KEY (user_id) REFERENCES "user"(user_id),

CONSTRAINT chk_event_dates
CHECK (finish_date > start_date)
);

-- ORDER

CREATE TABLE "order" (
order_id INT PRIMARY KEY,
user_id INT NOT NULL,
event_id INT NOT NULL,
total_amount DECIMAL(10,2) NOT NULL,
currency VARCHAR(3) DEFAULT 'COP',
status order_status_enum,
created_at TIMESTAMP,

CONSTRAINT fk_order_user
FOREIGN KEY (user_id) REFERENCES "user"(user_id),

CONSTRAINT fk_order_event
FOREIGN KEY (event_id) REFERENCES event(event_id)
);

-- PAYMENT

CREATE TABLE payment (
payment_id INT PRIMARY KEY,
order_id INT NOT NULL,
gateway gateway_enum,
gateway_transaction_id VARCHAR(100),
status payment_status_enum,
amount DECIMAL(10,2) NOT NULL,
payment_method payment_method_enum,
processed_at TIMESTAMP,

CONSTRAINT fk_payment_order
FOREIGN KEY (order_id) REFERENCES "order"(order_id)
);

-- PAYMENT LOG

CREATE TABLE payment_log (
payment_log_id INT PRIMARY KEY,
payment_id INT NOT NULL,
old_status payment_status_enum,
new_status payment_status_enum,
changed_at TIMESTAMP,
reason VARCHAR(255),

CONSTRAINT fk_paymentlog_payment
FOREIGN KEY (payment_id) REFERENCES payment(payment_id)
);

-- REFUND

CREATE TABLE refund (
refund_id INT PRIMARY KEY,
payment_id INT NOT NULL,
status refund_status_enum,
reason VARCHAR(255),
requested_at TIMESTAMP,
processed_at TIMESTAMP,

CONSTRAINT fk_refund_payment
FOREIGN KEY (payment_id) REFERENCES payment(payment_id)
);

-- TRANSACTION WEBHOOK

CREATE TABLE transaction_webhook (
webhook_id INT PRIMARY KEY,
payment_id INT NOT NULL,
payload JSONB NOT NULL,
received_at TIMESTAMP,

CONSTRAINT fk_webhook_payment
FOREIGN KEY (payment_id) REFERENCES payment(payment_id)
);

-- ------------------------------------------------------------
-- Wallet module.sql (sha256 641b0ea8571ef164d77aac1b05ab3864222523c6fd52e57a04aa36e9b7c8d771)
-- ------------------------------------------------------------
-- =========================
-- ENUMS
-- =========================

CREATE TYPE wallet_transaction_type AS ENUM (
    'refund',
    'top-up',
    'withdrawal',
    'payment'
);

CREATE TYPE wallet_transaction_status AS ENUM (
    'pending',
    'completed',
    'failed'
);

CREATE TYPE wallet_entity_type AS ENUM (
    'order',
    'payment',
    'refund'
);

-- =========================
-- WALLET
-- =========================

CREATE TABLE wallet (
    wallet_id SERIAL PRIMARY KEY,

    user_id INT NOT NULL,

    balance DECIMAL(10,2) DEFAULT 0,
    currency VARCHAR(3) DEFAULT 'COP',

    FOREIGN KEY(user_id)
        REFERENCES "user"(user_id)
);

-- =========================
-- WALLET_TRANSACTION
-- =========================

CREATE TABLE wallet_transaction (
    transaction_id SERIAL PRIMARY KEY,

    wallet_id INT NOT NULL,

    type wallet_transaction_type,

    amount DECIMAL(10,2),

    status wallet_transaction_status,

    created_at TIMESTAMP,

    FOREIGN KEY(wallet_id)
        REFERENCES wallet(wallet_id)
);

-- =========================
-- WALLET_REFERENCE
-- =========================

CREATE TABLE wallet_reference (
    wallet_reference_id SERIAL PRIMARY KEY,

    transaction_id INT NOT NULL,

    entity_type wallet_entity_type,
    entity_id INT,

    FOREIGN KEY(transaction_id)
        REFERENCES wallet_transaction(transaction_id)
);

-- ------------------------------------------------------------
-- Notification module.sql (sha256 7047637e6f0329fa645cf9c248a095a2ac614102c184a28f97e318356d669eeb)
-- ------------------------------------------------------------
-- ENUM para estado de entrega
CREATE TYPE delivered_status_enum AS ENUM (
    'Pending',
    'Sent',
    'Delivered',
    'Failed'
);

-- Tabla notification_type
CREATE TABLE notification_type (
    notification_type_id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(100) NOT NULL,
    active BOOLEAN DEFAULT TRUE
);

-- Tabla channel
CREATE TABLE channel (
    channel_id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(100) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    config JSONB
);

-- Tabla user
CREATE TABLE "user" (
    user_id INT PRIMARY KEY,
    user_status_id INT,
    role_id INT,
    alias VARCHAR(50) NOT NULL UNIQUE,
    activated BOOLEAN DEFAULT FALSE
);

-- Tabla notification
CREATE TABLE notification (
    notification_id INT PRIMARY KEY,
    channel_id INT,
    notification_type_id INT,
    event_id INT,
    subject VARCHAR(150) NOT NULL,
    body TEXT NOT NULL,
    sent_at TIMESTAMP,

    CONSTRAINT fk_notification_channel
        FOREIGN KEY (channel_id) REFERENCES channel(channel_id),

    CONSTRAINT fk_notification_type
        FOREIGN KEY (notification_type_id) REFERENCES notification_type(notification_type_id)
);

-- Tabla notification_user
CREATE TABLE notification_user (
    notification_user_id INT PRIMARY KEY,
    notification_id INT NOT NULL,
    user_id INT NOT NULL,
    email_address VARCHAR(255) NOT NULL,
    delivered_status delivered_status_enum,
    error_message TEXT,
    read_at TIMESTAMP,

    CONSTRAINT fk_notification_user_notification
        FOREIGN KEY (notification_id) REFERENCES notification(notification_id),

    CONSTRAINT fk_notification_user_user
        FOREIGN KEY (user_id) REFERENCES "user"(user_id),

    CONSTRAINT unique_notification_user
        UNIQUE (notification_id, user_id)
);

-- ------------------------------------------------------------
-- Theme module.sql (sha256 5c83900f1f92a30aa29662dc6ab97f3704db32215e9deb205f07aad97e99ada5)
-- ------------------------------------------------------------
-- =========================
-- THEME
-- =========================

CREATE TABLE theme (
    theme_id SERIAL PRIMARY KEY,

    name VARCHAR(100) NOT NULL,
    description VARCHAR(255) NOT NULL,

    default_palette JSONB
);

-- =========================
-- USER_THEME
-- =========================

CREATE TABLE user_theme (
    user_theme_id SERIAL PRIMARY KEY,

    user_id INT NOT NULL,
    theme_id INT NOT NULL,

    is_active BOOLEAN DEFAULT TRUE,

    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    FOREIGN KEY(user_id)
        REFERENCES "user"(user_id),

    FOREIGN KEY(theme_id)
        REFERENCES theme(theme_id),

    UNIQUE(user_id, theme_id)
);

-- =========================
-- THEME_CUSTOMIZATION
-- =========================

CREATE TABLE theme_customization (
    customization_id SERIAL PRIMARY KEY,

    user_theme_id INT NOT NULL,

    property VARCHAR(100) NOT NULL,
    value VARCHAR(255),

    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    FOREIGN KEY(user_theme_id)
        REFERENCES user_theme(user_theme_id)
);

-- =========================
-- THEME_LOG
-- =========================

CREATE TABLE theme_log (
    log_id SERIAL PRIMARY KEY,

    user_theme_id INT NOT NULL,

    action VARCHAR(50),
    property VARCHAR(100),

    old_value VARCHAR(255),
    new_value VARCHAR(255),

    FOREIGN KEY(user_theme_id)
        REFERENCES user_theme(user_theme_id)
);

-- ------------------------------------------------------------
-- Languaje module.sql (sha256 ef1742d573bb2e2fcebf3ffa14ea090d51b7e3c2a63905b01712262fedd93397)
-- ------------------------------------------------------------
-- -----------------
-- IDIOMAS
-- -----------------
CREATE TABLE language (
  language_id INT PRIMARY KEY,
  code VARCHAR(10) NOT NULL,
  name VARCHAR(50) NOT NULL,
  is_active BOOLEAN DEFAULT TRUE
);

-- -----------------
-- UBICACION
-- -----------------
CREATE TABLE department (
  department_id INT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  code VARCHAR(10)
);

CREATE TABLE city (
  city_id INT PRIMARY KEY,
  department_id INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  code VARCHAR(10),
  FOREIGN KEY (department_id) REFERENCES department(department_id)
);

CREATE TABLE site (
  site_id INT PRIMARY KEY,
  city_id INT NOT NULL,
  name VARCHAR(150) NOT NULL,
  address VARCHAR(200) NOT NULL,
  capacity INT,
  latitude DECIMAL(9,6),
  longitude DECIMAL(9,6),
  FOREIGN KEY (city_id) REFERENCES city(city_id)
);

-- -----------------
-- PERFIL
-- -----------------
CREATE TABLE profile (
  profile_id INT PRIMARY KEY,
  user_id INT NOT NULL,
  city_id INT NOT NULL,
  full_name VARCHAR(100) NOT NULL,
  photo_url VARCHAR(255),
  bio VARCHAR(255),
  FOREIGN KEY (city_id) REFERENCES city(city_id)
);

-- -----------------
-- EVENTOS
-- -----------------
CREATE TABLE event (
  event_id INT PRIMARY KEY,
  user_id INT NOT NULL,
  site_id INT NOT NULL,
  event_name VARCHAR(100) NOT NULL,
  description VARCHAR(255) NOT NULL,
  start_date DATE NOT NULL,
  finish_date DATE NOT NULL,
  status VARCHAR(20),
  is_public BOOLEAN DEFAULT TRUE,
  available_seats INT NOT NULL,

  FOREIGN KEY (site_id) REFERENCES site(site_id),

  CHECK (finish_date > start_date),
  UNIQUE (event_name, start_date, site_id)
);

-- -----------------
-- CATEGORIAS
-- -----------------
CREATE TABLE category (
  category_id INT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(100),
  active BOOLEAN DEFAULT TRUE,
  visible BOOLEAN DEFAULT TRUE,
  tag_id INT
);

-- -----------------
-- TEMAS
-- -----------------
CREATE TABLE theme (
  theme_id INT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(255),
  default_palette JSON
);

-- -----------------
-- SECCIONES
-- -----------------
CREATE TABLE section_type (
  section_type_id INT PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

-- -----------------
-- BLOQUES DE ASIENTO
-- -----------------
CREATE TABLE seat_block (
  seat_block_id INT PRIMARY KEY,
  event_section_id INT,
  name VARCHAR(100) NOT NULL,
  capacity INT NOT NULL
);

-- -----------------
-- CALIFICACION EVENTO
-- -----------------
CREATE TABLE event_rating (
  rating_id INT PRIMARY KEY,
  event_id INT NOT NULL,
  user_id INT NOT NULL,
  rating INT CHECK (rating BETWEEN 1 AND 5),
  comment VARCHAR(255),
  is_visible BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP,
  FOREIGN KEY (event_id) REFERENCES event(event_id)
);

-- -----------------
-- RESPUESTAS A COMENTARIOS
-- -----------------
CREATE TABLE event_comment_reply (
  reply_id INT PRIMARY KEY,
  parent_reply_id INT,
  rating_id INT NOT NULL,
  user_id INT NOT NULL,
  reply_text VARCHAR(255) NOT NULL,
  is_visible BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP,
  FOREIGN KEY (rating_id) REFERENCES event_rating(rating_id)
);

-- -----------------
-- REVIEWS
-- -----------------
CREATE TABLE review (
  review_id INT PRIMARY KEY,
  user_id INT NOT NULL,
  event_id INT NOT NULL,
  rating INT CHECK (rating BETWEEN 1 AND 5),
  comment VARCHAR(255),
  created_at TIMESTAMP,
  updated_at TIMESTAMP,
  FOREIGN KEY (event_id) REFERENCES event(event_id)
);

-- -----------------
-- TIPOS DE NOTIFICACION
-- -----------------
CREATE TABLE notification_type (
  notification_type_id INT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(100),
  active BOOLEAN DEFAULT TRUE
);

-- -----------------
-- CANALES
-- -----------------
CREATE TABLE channel (
  channel_id INT PRIMARY KEY,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(100),
  active BOOLEAN DEFAULT TRUE,
  config JSON
);

-- -----------------
-- NOTIFICACIONES
-- -----------------
CREATE TABLE notification (
  notification_id INT PRIMARY KEY,
  channel_id INT,
  notification_type_id INT,
  event_id INT,
  subject VARCHAR(150) NOT NULL,
  body TEXT,
  sent_at TIMESTAMP,

  FOREIGN KEY (channel_id) REFERENCES channel(channel_id),
  FOREIGN KEY (notification_type_id) REFERENCES notification_type(notification_type_id),
  FOREIGN KEY (event_id) REFERENCES event(event_id)
);

-- =====================================
-- TRADUCCIONES (MULTI IDIOMA)
-- =====================================

CREATE TABLE event_translation (
  translation_id INT PRIMARY KEY,
  event_id INT NOT NULL,
  language_id INT NOT NULL,
  translated_name VARCHAR(100),
  translated_description VARCHAR(255),
  status VARCHAR(20),

  FOREIGN KEY (event_id) REFERENCES event(event_id),
  FOREIGN KEY (language_id) REFERENCES language(language_id),

  UNIQUE (event_id, language_id)
);

CREATE TABLE category_translation (
  translation_id INT PRIMARY KEY,
  category_id INT NOT NULL,
  language_id INT NOT NULL,
  translated_name VARCHAR(100),
  translated_description VARCHAR(255),
  source VARCHAR(50),
  status VARCHAR(20),

  FOREIGN KEY (category_id) REFERENCES category(category_id),
  FOREIGN KEY (language_id) REFERENCES language(language_id),

  UNIQUE (category_id, language_id)
);

CREATE TABLE profile_translation (
  translation_id INT PRIMARY KEY,
  profile_id INT NOT NULL,
  language_id INT NOT NULL,
  translated_bio VARCHAR(255),
  source VARCHAR(50),
  status VARCHAR(20),

  FOREIGN KEY (profile_id) REFERENCES profile(profile_id),
  FOREIGN KEY (language_id) REFERENCES language(language_id),

  UNIQUE (profile_id, language_id)
);

CREATE TABLE theme_translation (
  translation_id INT PRIMARY KEY,
  theme_id INT NOT NULL,
  language_id INT NOT NULL,
  translated_name VARCHAR(100),
  translated_description VARCHAR(255),
  source VARCHAR(50),
  status VARCHAR(20),

  FOREIGN KEY (theme_id) REFERENCES theme(theme_id),
  FOREIGN KEY (language_id) REFERENCES language(language_id),

  UNIQUE (theme_id, language_id)
);

CREATE TABLE review_translation (
  translation_id INT PRIMARY KEY,
  review_id INT NOT NULL,
  language_id INT NOT NULL,
  translated_comment VARCHAR(255),
  source VARCHAR(50),
  status VARCHAR(20),

  FOREIGN KEY (review_id) REFERENCES review(review_id),
  FOREIGN KEY (language_id) REFERENCES language(language_id),

  UNIQUE (review_id, language_id)
);
