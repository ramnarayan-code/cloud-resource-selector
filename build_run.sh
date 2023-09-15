#!/usr/bin/env bash

docker build -t ip-range-selector .

docker run -d -p 8080:8080 ip-range-selector