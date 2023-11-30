#!/bin/bash

cd src/main && ./gradlew cleanTest test
allure generate allure-results/* --clean