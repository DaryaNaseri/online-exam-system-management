CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    user_role  VARCHAR(50)  NOT NULL,
    first_name VARCHAR(255) CHECK (first_name ~ '^[A-Z][a-z]+$'),
    last_name  VARCHAR(255) CHECK (last_name ~ '^[A-Z][a-z]+$'),
    email      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL UNIQUE,
    role       VARCHAR(50)  NOT NULL,
    status     VARCHAR(50)  NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE courses
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    course_code VARCHAR(255) NOT NULL,
    start_date  DATE         NOT NULL,
    end_date    DATE         NOT NULL,
    teacher_id  BIGINT REFERENCES users (id),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP
);

CREATE TABLE exams
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    duration    INT          NOT NULL,
    teacher_id  BIGINT REFERENCES users (id),
    course_id   BIGINT REFERENCES courses (id),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP
);

CREATE TABLE questions
(
    id         BIGSERIAL PRIMARY KEY,
    qtype      VARCHAR(50) NOT NULL,
    title      VARCHAR(50) NOT NULL,
    content    TEXT        NOT NULL,
    teacher_id BIGINT REFERENCES users (id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE options
(
    id          BIGSERIAL PRIMARY KEY,
    text        TEXT    NOT NULL,
    is_correct  BOOLEAN NOT NULL,
    question_id BIGINT REFERENCES questions (id),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP
);

CREATE TABLE question_options
(
    question_id BIGINT REFERENCES questions (id),
    option_id   BIGINT REFERENCES options (id),
    PRIMARY KEY (question_id, option_id)
);

CREATE TABLE exam_questions
(
    id          BIGSERIAL PRIMARY KEY,
    exam_id     BIGINT REFERENCES exams (id)     NOT NULL,
    question_id BIGINT REFERENCES questions (id) NOT NULL,
    score       DOUBLE PRECISION                 NOT NULL DEFAULT 1.0
);

CREATE TABLE exam_answers
(
    id          BIGSERIAL PRIMARY KEY,
    answer_type VARCHAR(50)                      NOT NULL,
    exam_id     BIGINT REFERENCES exams (id)     NOT NULL,
    question_id BIGINT REFERENCES questions (id) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP
);

CREATE TABLE roles
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE authorities
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE roles_authorities
(
    role_id      BIGINT REFERENCES roles (id),
    authority_id BIGINT REFERENCES authorities (id),
    PRIMARY KEY (role_id, authority_id)
);

CREATE TABLE users_roles
(
    user_id BIGINT REFERENCES users (id),
    role_id BIGINT REFERENCES roles (id),
    PRIMARY KEY (user_id, role_id)
);

CREATE TABLE j_course_students
(
    course_id  BIGINT REFERENCES courses (id),
    student_id BIGINT REFERENCES users (id),
    PRIMARY KEY (course_id, student_id)
);

--
-- -- Insert default roles
-- INSERT INTO roles (name)
-- VALUES ('ROLE_ADMIN'),
--        ('ROLE_TEACHER'),
--        ('ROLE_STUDENT');
--
-- -- Insert default authorities
-- INSERT INTO authorities (name)
-- VALUES ('VIEW_USER_LIST'),
--        ('APPROVE_USER'),
--        ('MANAGE_USER_ROLES'),
--        ('FILTER_AND_SEARCH_USERS'),
--        ('VIEW_COURSE_LIST'),
--        ('CREATE_COURSE'),
--        ('ASSIGN_TEACHER_TO_COURSE'),
--        ('ADD_STUDENT_TO_COURSE'),
--        ('EDIT_STUDENT_LIST'),
--        ('DELETE_COURSE'),
--        ('EDIT_COURSE'),
--        ('VIEW_TEACHER_OF_COURSE'),
--        ('VIEW_STUDENTS_OF_COURSE'),
--        ('ALL_EXAMS_OF_COURSE'),
--        ('CREATE_EXAM'),
--        ('EDIT_EXAM'),
--        ('DELETE_EXAM'),
--        ('VIEW_TEACHER_COURSE');
--
-- -- Assign authorities to roles
-- INSERT INTO roles_authorities (role_id, authority_id)
-- VALUES ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM authorities WHERE name = 'APPROVE_USER')),
--        ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'), (SELECT id FROM authorities WHERE name = 'VIEW_USER_LIST')),
--        ((SELECT id FROM roles WHERE name = 'ROLE_ADMIN'),
--         (SELECT id FROM authorities WHERE name = 'MANAGE_USER_ROLES')),
--        ((SELECT id FROM roles WHERE name = 'ROLE_TEACHER'),
--         (SELECT id FROM authorities WHERE name = 'VIEW_TEACHER_COURSE')),
--        ((SELECT id FROM roles WHERE name = 'ROLE_TEACHER'),
--         (SELECT id FROM authorities WHERE name = 'ALL_EXAMS_OF_COURSE')),
--        ((SELECT id FROM roles WHERE name = 'ROLE_TEACHER'), (SELECT id FROM authorities WHERE name = 'CREATE_EXAM'));
--
-- -- Insert default admin user
-- INSERT INTO users (user_role, first_name, last_name, email, password, username, role, status, created_at)
-- VALUES ('ADMIN', 'Admin1', 'Admin', 'admin@gmail.com', 'adminadmin', 'adminadmin', 'ROLE_ADMIN', 'ACTIVE', NOW());
--
-- -- Insert default teacher user
-- INSERT INTO users (user_role, first_name, last_name, email, password, username, role, status, created_at)
-- VALUES ('TEACHER', 'Teacher1', 'Teacher', 'teacher@gmail.com', 'Teacher', 'Teacher1', 'ROLE_TEACHER', 'APPROVED',
--         NOW());
--
-- -- Assign roles to users
-- INSERT INTO users_roles (user_id, role_id)
-- VALUES ((SELECT id FROM users WHERE email = 'admin@gmail.com'), (SELECT id FROM roles WHERE name = 'ROLE_ADMIN')),
--        ((SELECT id FROM users WHERE email = 'teacher@gmail.com'), (SELECT id FROM roles WHERE name = 'ROLE_TEACHER'));
