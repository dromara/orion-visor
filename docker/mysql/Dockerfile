FROM --platform=$BUILDPLATFORM mysql:8.0.28

# 系统时区
ARG TZ=Asia/Shanghai

# 设置时区
RUN ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime && \
    echo "${TZ}" > /etc/timezone

# 复制配置文件
COPY ./mysql/my.cnf /etc/mysql/conf.d/my.cnf

# 复制初始化脚本
COPY ./mysql/sql/init-*.sql /docker-entrypoint-initdb.d
