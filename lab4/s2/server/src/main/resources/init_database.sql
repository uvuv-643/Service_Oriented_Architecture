CREATE TABLE team_heroes (
                             team_id INT,
                             hero_id INT,
                             PRIMARY KEY (team_id, hero_id)
);


-- Создаем перечислимые типы данных для WeaponType и Mood
CREATE TYPE weapon_type AS ENUM ('HAMMER', 'AXE', 'PISTOL', 'KNIFE', 'BAT');
CREATE TYPE mood AS ENUM ('SORROW', 'APATHY', 'CALM', 'RAGE', 'FRENZY');

-- Создаем таблицу Car
CREATE TABLE Car (
                     id SERIAL PRIMARY KEY,
                     cool BOOLEAN
);

-- Создаем таблицу Coordinates
CREATE TABLE Coordinates (
                             id SERIAL PRIMARY KEY,
                             x INTEGER NOT NULL CHECK (x <= 9),
                             y BIGINT NOT NULL
);

-- Создаем таблицу HumanBeing
CREATE TABLE HumanBeing (
                            id BIGSERIAL PRIMARY KEY CHECK (id > 0),
                            name VARCHAR NOT NULL CHECK (name <> ''),
                            coordinates_id INTEGER NOT NULL,
                            creationDate DATE NOT NULL DEFAULT CURRENT_DATE,
                            realHero BOOLEAN NOT NULL,
                            hasToothpick BOOLEAN NOT NULL,
                            impactSpeed DOUBLE PRECISION CHECK (impactSpeed <= 676),
                            minutesOfWaiting DOUBLE PRECISION,
                            weaponType weapon_type,
                            mood mood NOT NULL,
                            car_id INTEGER NOT NULL,
    -- Установка внешних ключей с обеспечением ссылочной целостности
                            FOREIGN KEY (coordinates_id) REFERENCES Coordinates (id) ON DELETE RESTRICT ON UPDATE CASCADE,
                            FOREIGN KEY (car_id) REFERENCES Car (id) ON DELETE RESTRICT ON UPDATE CASCADE
);
