### 日志文件在哪?

```shell
# 宿主机
tail -f -n 200 /data/orion-visor-space/docker-volumes/orion-visor-service/root-orion/logs/orion-visor/app.log
# 容器内
tail -f -n 200 /root/orion/logs/orion-visor/app.log
# 滚动日志 .../logs/orion-visor/rolling/*
```

### 数据误删除怎么办?

数据库的数据都采用了逻辑删除, 可以将已删除的数据中的 `deleted` 字段改为 `0`   
如果不知道数据是哪一条, 可以查询用户操作日志, 点击 `参数` 寻找操作的id

### 执行命令时为什么会找不到环境变量?

可以在执行命令的第一行设置 `source /etc/profile` 来加载环境变量

### 命令中途执行失败如何设置中断执行?

可以在执行命令的第一行设置 `set -e`  
作用是: 当执行出现意料之外的情况时, 立即退出

### 在调度任务、批量执行 命令执行成功的依据是什么?

是获取命令的 `exitcode` 判断是否为 `0` 如果非0则代表命令执行失败  
同理, 在命令的最后一行设置 `exit 1` 结果将会是失败, 可以用此来中断后续流程

### 调度任务、批量执行 的日志文件中如何只保存原始输出?

修改 `application.yaml` `app.exec-log.append-ansi` 为 `false`

### SFTP 为什么有些文件无法编辑?

只有普通文件可以在线编辑, 也就是 attr 为 `-` 开头的文件, 且文件大小不超过 `2MB` (默认)  
修改 `.env.production` `VITE_SFTP_PREVIEW_MB` 改为一个合适的大小(MB) 重新构建

### 为什么使用密钥认证还是无法连接机器?

```
# 升级 openssh
yum update openssh
sshd -v (我的版本: OpenSSH_7.4p1, OpenSSL 1.0.2k-fips  26 Jan 2017)

# 生成密钥时添加参数 -m PEM
ssh-keygen -t rsa -m PEM
chmod 700  ~/.ssh
chmod 700  ~/.ssh/authorized_keys 

# 修改 sshd 配置 /etc/ssh/sshd_config
PubkeyAuthentication yes
RSAAuthentication yes
AuthorizedKeysFile .ssh/authorized_keys

# 重启 sshd 服务
service sshd restart
```
