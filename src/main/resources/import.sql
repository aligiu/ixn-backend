-- Clear existing data from the content table and pk sequences

--TRUNCATE TABLE content RESTART IDENTITY CASCADE;


-- Insert new data

INSERT INTO content (id, title, description, content, next_id, prev_id, timestamp, secret) VALUES
(1, 'Ashford and St Peter''s', 'A comprehensive overview of services and specialties offered.', '<h1>Gibberish</h1><h3>Point 1</h3><p>Lorem ipsum dolor <u>sit amet, </u><strong><u>consectetur</u></strong><u> adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqu</u>a. </p><h3>Point 2</h3><p>Ut enim ad minim veniam, quis </p><p>nostrud exercitation ullamco</p><ul><li><p>laboris <s>nisi</s> ut aliquip ex </p></li><li><p>ea commodo consequat. </p></li><li><p>Duis aute irure dolor in reprehenderit in</p></li></ul><p>voluptate velit </p><ol><li><p>esse cillum dolore </p></li><li><p>eu fugiat nulla pariatur.</p></li></ol><h3>Point 3 </h3><ul data-type="taskList"><li data-checked="false" data-type="taskItem"><label><input type="checkbox"><span></span></label><div><p>Excepteur sint occaecat <strong>cupidatat</strong> non proident, sunt in culpa qui officia</p></div></li><li data-checked="true" data-type="taskItem"><label><input type="checkbox" checked="checked"><span></span></label><div><p>Deserunt mollit anim id est laborum</p></div></li></ul>', 2, NULL, '2024-07-10 02:37:04', '<p>This secret is visible to users and admins, but not guests</p><p>Email: abc@email.com</p><p>Password: 123</p>');

INSERT INTO content (id, title, description, content, next_id, prev_id, timestamp, secret) VALUES
(2, 'East Surrey', 'Information on facilities and patient care at East Surrey.', '<p>Some important content</p>', 3, 1, '2024-07-10 02:37:04', '<p>Here''s another secret...</p>');

INSERT INTO content (id, title, description, content, next_id, prev_id, timestamp) VALUES
(3, 'Frimley', 'Details about the Frimley Health NHS Foundation Trust.', '', 4, 2, '2024-07-10 02:37:04');

INSERT INTO content (id, title, description, content, next_id, prev_id, timestamp) VALUES
(4, 'Royal Surrey', 'An in-depth look at Royal Surrey''s medical services.', '', 5, 3, '2024-07-10 02:37:04');

INSERT INTO content (id, title, description, content, next_id, prev_id, timestamp) VALUES
(5, 'Wexham', 'Insights into the Wexham Park Hospital operations.', '', 6, 4, '2024-07-10 02:37:04');

INSERT INTO content (id, title, description, content, next_id, prev_id, timestamp) VALUES
(6, 'Academy', 'Educational programs and training at the Academy.', '', 7, 5, '2024-07-10 02:37:04');

INSERT INTO content (id, title, description, content, next_id, prev_id, timestamp) VALUES
(7, 'ARCP', 'Annual Review of Competence Progression guidelines.', '', 8, 6, '2024-07-10 02:37:04');

INSERT INTO content (id, title, description, content, next_id, prev_id, timestamp) VALUES
(8, 'Conferences', 'Upcoming conferences and events for medical professionals.', '', 9, 7, '2024-07-10 02:37:04');

INSERT INTO content (id, title, description, content, next_id, prev_id, timestamp) VALUES
(9, 'Educational Resources', 'Learning materials and resources for medical education.', '', 10, 8, '2024-07-10 02:37:04');

INSERT INTO content (id, title, description, content, next_id, prev_id, timestamp) VALUES
(10, 'Exams', 'Examination schedules and preparation tips.', '', NULL, 9, '2024-07-10 02:37:04');
