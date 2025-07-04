FROM --platform=$BUILDPLATFORM mysql:8.0.28
# 系统时区
ARG TZ=Asia/Shanghai
# 设置时区
RUN ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime && \
  echo '${TZ}' > /etc/timezone
# 复制配置
COPY ./docker/mysql/my.cnf /etc/mysql/conf.d/my.cnf
# 复制初始化脚本
COPY ./sql /tmp
# 设置初始化脚本
RUN cat /tmp/init-1-schema-databases.sql >> /tmp/init.sql && \
    cat /tmp/init-2-schema-tables.sql >> /tmp/init.sql && \
    cat /tmp/init-3-schema-quartz.sql >> /tmp/init.sql && \
    cat /tmp/init-4-data.sql >> /tmp/init.sql && \
    cp /tmp/init.sql /docker-entrypoint-initdb.d
