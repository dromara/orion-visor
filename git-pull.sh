#!/bin/bash
# 清空
git clean -df
# 切换到 HEAD
git reset --hard HEAD
# 拉取远程
git pull
# 查看日志
git log -n 1
