INSERT INTO drone(id, serial_number, model, weight_limit, battery, state)
VALUES
(10, 'serial-number-10', 'LIGHT_WEIGHT', 200, 10.1, 'IDLE'),
(11, 'serial-number-11', 'CRUISER_WEIGHT', 400, 100.0, 'LOADING'),
(12, 'serial-number-12', 'HEAVY_WEIGHT', 500, 5.8, 'DELIVERING'),
(20, 'serial-number-20', 'MIDDLE_WEIGHT', 300, 100.0, 'IDLE'),
(21, 'serial-number-21', 'MIDDLE_WEIGHT', 300, 25.0, 'IDLE'),
(22, 'serial-number-22', 'HEAVY_WEIGHT', 500, 50.0, 'LOADED');

INSERT INTO medicine(id, name, weight, code, image)
VALUES
(33, 'A33', 50, 'A_33', RAWTOHEX('A33_BLOB')),
(34, 'B34', 80, 'B_34', RAWTOHEX('B34_BLOB'));

INSERT INTO cargo(drone_id, medicine_id)
VALUES
(12, 33),
(12, 34);