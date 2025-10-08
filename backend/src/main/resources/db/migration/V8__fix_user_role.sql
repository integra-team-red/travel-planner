UPDATE users
SET role = 'ROLE_USER'
WHERE email = 'user';

UPDATE users
SET role = 'ROLE_ADMIN'
WHERE email = 'admin';
