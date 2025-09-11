FROM --platform=$TARGETPLATFORM influxdb:2

# 系统时区
ARG TZ=Asia/Shanghai

# 设置时区
RUN ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime && \
    echo "${TZ}" > /etc/timezone
