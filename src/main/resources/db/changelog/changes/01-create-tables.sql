--liquibase formatted sql
--changeset roman:1

-- 1. Таблица пользователей
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       role VARCHAR(50) NOT NULL
);

-- 2. Таблица авторов
CREATE TABLE authors (
                         id BIGSERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         biography TEXT
);

-- 3. Таблица издательств
CREATE TABLE publishers (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL UNIQUE,
                            address VARCHAR(255)
);

-- 4. Таблица жанров
CREATE TABLE genres (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL UNIQUE
);

-- 5. Таблица книг (содержит внешние ключи на Жанр и Издательство)
CREATE TABLE books (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       isbn VARCHAR(50) UNIQUE,
                       genre_id BIGINT,
                       publisher_id BIGINT,
                       CONSTRAINT fk_book_genre FOREIGN KEY (genre_id) REFERENCES genres(id) ON DELETE SET NULL,
                       CONSTRAINT fk_book_publisher FOREIGN KEY (publisher_id) REFERENCES publishers(id) ON DELETE SET NULL
);

-- 6. Промежуточная таблица для связи Многие-ко-Многим (Книги <-> Авторы)
CREATE TABLE book_authors (
                              book_id BIGINT NOT NULL,
                              author_id BIGINT NOT NULL,
                              PRIMARY KEY (book_id, author_id),
                              CONSTRAINT fk_ba_book FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
                              CONSTRAINT fk_ba_author FOREIGN KEY (author_id) REFERENCES authors(id) ON DELETE CASCADE
);

-- 7. Таблица отзывов (связывает Книгу и Пользователя)
CREATE TABLE reviews (
                         id BIGSERIAL PRIMARY KEY,
                         text TEXT NOT NULL,
                         rating INT CHECK (rating >= 1 AND rating <= 5),
                         book_id BIGINT NOT NULL,
                         user_id BIGINT NOT NULL,
                         CONSTRAINT fk_review_book FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
                         CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- 8. Таблица закладок/полок (связывает Книгу и Пользователя)
CREATE TABLE bookmarks (
                           id BIGSERIAL PRIMARY KEY,
                           status VARCHAR(50) NOT NULL,
                           user_id BIGINT NOT NULL,
                           book_id BIGINT NOT NULL,
                           CONSTRAINT fk_bookmark_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                           CONSTRAINT fk_bookmark_book FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);