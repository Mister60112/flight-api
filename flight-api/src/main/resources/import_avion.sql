-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database

INSERT INTO planes(id, operator, model, registration, capacity) VALUES(NEXTVAL('planes_sequence'), 'AirbusIndustrie', 'AIRBUS A380', 'F-ABCD', 10);
INSERT INTO planes(id, operator, model, registration, capacity) VALUES(NEXTVAL('planes_sequence'), 'Boeing', 'BOEING CV2', 'F-AZER', 15);
-- Inserting a plane from Embraer
INSERT INTO planes(id, operator, model, registration, capacity) VALUES(NEXTVAL('planes_sequence'), 'Embraer', 'EMBRAER E190', 'F-EFGH', 12);

-- Inserting a plane from Bombardier
INSERT INTO planes(id, operator, model, registration, capacity) VALUES(NEXTVAL('planes_sequence'), 'Bombardier', 'BOMBARDIER CRJ900', 'F-IJKL', 18);

-- Inserting a plane from Airbus
INSERT INTO planes(id, operator, model, registration, capacity) VALUES(NEXTVAL('planes_sequence'), 'AirbusIndustrie', 'AIRBUS A320', 'F-MNOP', 20);

-- Inserting a plane from Boeing
INSERT INTO planes(id, operator, model, registration, capacity) VALUES(NEXTVAL('planes_sequence'), 'Boeing', 'BOEING 737', 'F-QRST', 22);
