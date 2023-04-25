-- liquibase formatted sql

-- changeset garry:fill_data_user
insert into users (id, first_name, last_name, username, password, phone, role, enabled, image)
values  (1, 'User1', 'User11', 'user1@gmail.com', '$2y$10$DsOdO9yAoJpgcYHWpuFqx.JKhfyt7KIhJBEWwKsAKvgtGtw/6guPC', '+71111111111', 'USER', true, '');
values  (2, 'User2', 'User22', 'user2@gmail.com', '$2y$10$50NZ9k4JrR2I6SxhXqmGm.KbwaGQejmRCA8X14aQUfRa5/Vefp11y', '+72222222222', 'USER', true, '');
values  (3, 'User3', 'User33', 'user3@gmail.com', '$2y$10$m6chA7QmRDndXAPe0W4RsebXNjAZtgbRK2Vfa0aD.tPhoBqfeTguS', '+73333333333', 'USER', true, '');
values  (4, 'User4', 'User44', 'user4@gmail.com', '$2y$10$z8/xOnhjYCAIJIpcHwXw6u7ujWm8kcbQQRuZyVS7o4Zejn.1q1G2S', '+74444444444', 'ADMIN', true, '');