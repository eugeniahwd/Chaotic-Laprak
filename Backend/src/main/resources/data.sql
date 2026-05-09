INSERT INTO targets (name, role, sprite_key)
SELECT 'Mr. Astha', 'dosen', 'astha'
    WHERE NOT EXISTS (SELECT 1 FROM targets WHERE name = 'Mr. Astha');

INSERT INTO targets (name, role, sprite_key)
SELECT 'Mrs. Riri', 'dosen', 'riri'
    WHERE NOT EXISTS (SELECT 1 FROM targets WHERE name = 'Mrs. Riri');

INSERT INTO targets (name, role, sprite_key)
SELECT 'BN', 'aslab', 'bn'
    WHERE NOT EXISTS (SELECT 1 FROM targets WHERE name = 'BN');

INSERT INTO targets (name, role, sprite_key)
SELECT 'NL', 'aslab', 'nl'
    WHERE NOT EXISTS (SELECT 1 FROM targets WHERE name = 'NL');

INSERT INTO targets (name, role, sprite_key)
SELECT 'AF', 'aslab', 'af'
    WHERE NOT EXISTS (SELECT 1 FROM targets WHERE name = 'AF');