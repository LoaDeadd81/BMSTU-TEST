#!/bin/bash

# shellcheck disable=SC2046
export  $(grep -v '^#' .env | xargs -0)
cd src/kapp && ./gradlew cleanTest test
./gradlew allureAggregateReport --clean