UPDATE users
SET role = 'USER'
WHERE email = 'user';

UPDATE users
SET role = 'ADMIN'
WHERE email = 'admin';
