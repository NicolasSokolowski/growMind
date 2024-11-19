BEGIN;

DROP TABLE IF EXISTS "card", "deck", "_user", "role";

CREATE TABLE "role" (
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "name" VARCHAR(10) NOT NULL UNIQUE,
    "created_at" TIMESTAMP NOT NULL DEFAULT NOW(),
    "updated_at" TIMESTAMP
);

CREATE TABLE "_user" (
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "first_name" VARCHAR(50) NOT NULL,
    "last_name" VARCHAR(50) NOT NULL,
    "email" VARCHAR(100) NOT NULL UNIQUE,
    "password" VARCHAR(256) NOT NULL,
    "role_id" INT NOT NULL REFERENCES "role"("id"),
    "created_at" TIMESTAMP NOT NULL DEFAULT NOW(),
    "updated_at" TIMESTAMP
);

CREATE TABLE "deck" (
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "name" VARCHAR(50) NOT NULL,
    "user_id" INT NOT NULL REFERENCES "_user"("id"),
    "created_at" TIMESTAMP NOT NULL DEFAULT NOW(),
    "updated_at" TIMESTAMP
);

CREATE TABLE "card" (
    "id" INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    "front_side" VARCHAR(50) NOT NULL,
    "back_side" VARCHAR(50) NOT NULL,
    "difficulty" INT DEFAULT 0,
    "deck_id" INT NOT NULL REFERENCES "deck"("id"),
    "created_at" TIMESTAMP NOT NULL DEFAULT NOW(),
    "updated_at" TIMESTAMP
);

COMMIT;