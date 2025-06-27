FROM guacamole/guacd:1.5.5
USER root
# 系统时区
ARG TZ=Asia/Shanghai
# 设置时区
RUN ln -sf /usr/share/zoneinfo/${TZ} /etc/localtime && \
  echo '${TZ}' > /etc/timezone

# 创建所需目录
RUN mkdir -p /home/guacd/drive /usr/share/guacd/drive
