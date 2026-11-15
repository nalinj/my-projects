This is a test banking Spring Boot Application.

Assumptions.
1. Account number is 16 digits long.
2. The uniqueness of a customer is determined by the name and the nickname pair.


TODO
1. Uniqueness of account numbers needs to be ensured.
2. Caching is handled by ECache but Redis like production grading caching needs to be used on production.


Database scripts.
Schema Name: westpac

CREATE TABLE westpac.account (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	number varchar NULL,
	name varchar NULL,
	nick_name varchar NULL
);

CREATE TABLE westpac.nick_name (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);
