# learnqa-docker-selenium-baseline

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8b0d1d5da11442b78d7157a059b1cdce)](https://app.codacy.com/app/chit786/learnqa-docker-selenium-baseline?utm_source=github.com&utm_medium=referral&utm_content=chit786/learnqa-docker-selenium-baseline&utm_campaign=Badge_Grade_Dashboard)
[![Build Status](https://travis-ci.org/chit786/learnqa-docker-selenium-baseline.svg?branch=master)](https://travis-ci.org/chit786/learnqa-docker-selenium-baseline)

## Why another test automation setup

Docker is a consistent way of reproducible steps and to minimize the efforts one has to put on setting up selenium-grid infrastructure , in this repo you will find all the setup with startup and teardown scripts to setup selenium grid using 2 proven flavors :
- [zalenium](https://github.com/zalando/zalenium)
- [selenoid](https://github.com/aerokube/selenoid)

## default test

./gradlew clean test

## using zalenium with gradle 

./gradlew clean test -PdockerSetup=zalenium -DREMOTE_DRIVER=true

## using selenoid with gradle 
./gradlew clean test -PdockerSetup=selenoid -DREMOTE_DRIVER=true

