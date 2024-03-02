> ##### 1. 数据误删除怎么办?

数据库的数据都采用了逻辑删除, 可以将已删除的数据中的 `deleted` 字段改为 `0`   
如果不知道数据是哪一条, 可以查询用户操作日志, 点击 `参数` 寻找操作的id       

> ##### 2. 是否支持维护 Windows 主机?

支持, 但是 Windows 的 ssh 命令兼容性不好, 一切需要执行ssh命令的地方都不友好  
如: 批量执行, 调度任务兼容性非常不友好  

> ##### 3. 执行命令时为什么会找不到环境变量?

可以在执行命令的第一行设置 `source /etc/profile` 来加载环境变量  

> ##### 4. 命令中途执行失败如何设置中断执行?

可以在执行命令的第一行设置 `set -e`  
作用是: 当执行出现意料之外的情况时, 立即退出   

> ##### 5. 在调度任务、批量执行 命令执行成功的依据是什么?

是获取命令的 `exitcode` 判断是否为 `0` 如果非0则代表命令执行失败  
同理, 在命令的最后一行设置 `exit 1` 结果将会是失败, 可以用此来中断后续流程  

> ##### 6. 为什么使用秘钥认证还是无法连接机器?

```
# 升级 openssh
yum update openssh
sshd -v (我的版本: OpenSSH_7.4p1, OpenSSL 1.0.2k-fips  26 Jan 2017)

# 生成秘钥时添加参数 -m PEM
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

<br/>

⚡ 详细使用请参考操作手册~

<br/>
