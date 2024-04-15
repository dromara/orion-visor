FROM mysql:8.0.28
COPY sql/init-1-schema-databases.sql /tmp
COPY sql/init-2-schema-tables.sql /tmp
COPY sql/init-3-data.sql /tmp
COPY docker/mysql/my.cnf /etc/mysql/conf.d/my.cnf
RUN cat /tmp/init-1-schema-databases.sql >> /tmp/init.sql && \
    cat /tmp/init-2-schema-tables.sql >> /tmp/init.sql && \
    cat /tmp/init-3-schema-quartz.sql >> /tmp/init.sql && \
    cat /tmp/init-4-data.sql >> /tmp/init.sql && \
    cp /tmp/init.sql /docker-entrypoint-initdb.d
