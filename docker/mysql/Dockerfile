FROM --platform=$TARGETPLATFORM mysql:8.0.39

# 系统时区
ARG TZ=Asia/Shanghai

# 设置时区
RUN ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime && \
    echo "${TZ}" > /etc/timezone

# 复制配置文件
COPY ./mysql/my.cnf /etc/mysql/conf.d/my.cnf

# 复制初始化脚本
COPY ./mysql/sql/init-*.sql /docker-entrypoint-initdb.d/

# 心跳检测
HEALTHCHECK --interval=10s --timeout=3s --start-period=3s --retries=3 \
  CMD mysqladmin ping -h localhost -u root --password=${MYSQL_ROOT_PASSWORD} || exit 1
