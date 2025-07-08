#!/bin/bash
git clean -df
git reset --hard HEAD
git pull
git log -n 1
