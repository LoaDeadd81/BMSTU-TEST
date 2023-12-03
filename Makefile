.PHONY: up
.PHONY: down
.PHONY: rb
.PHONY: clean
.PHONY: test

up:
	docker-compose up -d

up-db:
	docker-compose up -d db

down:
	docker-compose down --volumes

build:
	cd src/code && sudo ./gradlew build

rb: build
	docker compose restart --no-deps web

clean:
	cd src/code && sudo ./gradlew clean

test:
	sudo rm -rf allure-re*
	sudo rm -rf src/main/allure-re*
	sudo rm -rf src/main/BL/build/allure-re*
	sudo rm -rf src/main/BL/build/test-results
	sudo rm -rf src/main/DA/build/allure-re*
	sudo rm -rf src/main/DA/build/test-results
	sudo rm -rf src/main/RestAPI/build/allure-re*
	sudo rm -rf src/main/RestAPI/build/test-results
	sudo rm -rf src/main/build/allure-re*
	sudo rm -rf src/main/build/test-results
	sudo ./test.sh
