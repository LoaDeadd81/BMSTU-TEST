.PHONY: up
.PHONY: up-test
.PHONY: down
.PHONY: build
.PHONY: clean

up:
	docker-compose up -d

up-test:
	cd test && docker-compose up -d db

down:
	docker-compose down --volumes

build:
	cd src/code && sudo ./gradlew build

clean:
	cd src/code && sudo ./gradlew clean
