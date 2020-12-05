INSERT INTO auth_roles (id, created_at, name)
SELECT
    'eaf4bc88-86ef-11ea-bc55-0242ac130003', now(), 'ROLE_USER'
WHERE NOT EXISTS (
    SELECT * FROM auth_roles WHERE id = 'eaf4bc88-86ef-11ea-bc55-0242ac130003'
);

INSERT INTO auth_roles (id, created_at, name)
SELECT
    'eaf4beea-86ef-11ea-bc55-0242ac130003', now(), 'ROLE_MANAGER'
WHERE NOT EXISTS (
    SELECT * FROM auth_roles WHERE id = 'eaf4beea-86ef-11ea-bc55-0242ac130003'
);


INSERT INTO auth_roles (id, created_at, name)
SELECT
    'eaf4c200-86ef-11ea-bc55-0242ac130003', now(), 'ROLE_ADMIN'
WHERE NOT EXISTS (
    SELECT * FROM auth_roles WHERE id = 'eaf4c200-86ef-11ea-bc55-0242ac130003'
);

INSERT INTO auth_users (id,created_at, first_name, last_name, username, email, password, account_non_expired, account_non_locked, credentials_non_expired, enabled)
SELECT
    'eaf4c2f0-86ef-11ea-bc55-0242ac130003', now(), '', 'Administrateur', 'admin', 'bmbiandji@gmail.com', '{bcrypt}$2y$12$5ZjWscGfL8mERqkKa/fQaOO5WGUoGTyCie4O408LRvy6aEx.6Kwm.', '1', '1', '1', '1'
WHERE NOT EXISTS (
    SELECT * FROM auth_users WHERE id = 'eaf4c2f0-86ef-11ea-bc55-0242ac130003'
);

INSERT INTO auth_users_roles (user_id, role_id)
SELECT
    'eaf4c2f0-86ef-11ea-bc55-0242ac130003','eaf4bc88-86ef-11ea-bc55-0242ac130003'
WHERE NOT EXISTS (
    SELECT * FROM auth_users_roles WHERE  role_id = 'eaf4bc88-86ef-11ea-bc55-0242ac130003'
);

INSERT INTO auth_users_roles (user_id, role_id)
SELECT
    'eaf4c2f0-86ef-11ea-bc55-0242ac130003','eaf4beea-86ef-11ea-bc55-0242ac130003'
WHERE NOT EXISTS (
    SELECT * FROM auth_users_roles WHERE  role_id = 'eaf4beea-86ef-11ea-bc55-0242ac130003'
);

INSERT INTO auth_users_roles (user_id, role_id)
SELECT
    'eaf4c2f0-86ef-11ea-bc55-0242ac130003','eaf4c200-86ef-11ea-bc55-0242ac130003'
WHERE NOT EXISTS (
    SELECT * FROM auth_users_roles WHERE  role_id = 'eaf4c200-86ef-11ea-bc55-0242ac130003'
);

UPDATE auth_users SET company_id = '49467a99-801e-459d-b82a-5315e874cb29' WHERE id = 'eaf4c2f0-86ef-11ea-bc55-0242ac130003';
