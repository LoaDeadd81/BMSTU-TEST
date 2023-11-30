#!/bin/bash

# shellcheck disable=SC2046
export  $(grep -v '^#' .env | xargs -0)
cd src/main && ./gradlew cleanTest test
allure generate allure-results/* --clean