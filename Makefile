.PHONY: up
.PHONY: down
.PHONY: rb
.PHONY: clean
.PHONY: test

up:
	docker-compose up -d

down:
	docker-compose down

build:
	cd src/code && sudo ./gradlew build

rb: build
	docker compose restart --no-deps web

clean:
	cd src/code && sudo ./gradlew clean

test:
	docker compose exec web sh -c "cd /app && ./gradlew test"