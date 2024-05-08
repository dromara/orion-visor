### 命令执行

批量执行 ssh 主机 shell 脚本。

* 重置: 重置全部参数
* 执行: 执行所输入的命令
* 返回: 返回到执行命令页面
* 从模板中选择: 从模板中选择需要执行的命令
* 执行历史: 点击历史命令可以快速填入

### 批量执行日志

查看批量执行任务日志。

* 执行命令: 跳转到批量执行页面
* 清空: 清空执行日志
* 删除: 删除执行日志
* 重新执行: 重新执行此命令
* 命令: 查看执行时的命令
* 参数: 查看执行时的参数
* 中断: 中断命令执行
* 日志: 查看执行日志, ctrl + 左键点击会用新页面打开
* 下载: 下载执行日志

### 执行模板

用来维护批量执行的命令模板, 支持动态参数, 使用 `@{{ xxx }}` 来替换命令参数。

* 新增: 新增执行模板
* 执行: 打开命令执行框并且带入模板参数
* 修改: 修改执行模板
* 删除: 删除执行模板

### 日志面板快捷键

* 回车: `Enter`
* 向上滚动一行: `↑`
* 向上滚动一页: `Home`
* 向下滚动一行: `↓`
* 向下滚动一页: `End`
* 全选: `ctrl` `A`
* 复制: `ctrl` `C`
* 搜索: `ctrl` `F`
* 清空: `ctrl` `L`

### 命令内置参数

⚡ 使用 `@{{ xxx }}` 来替换命令参数

| 参数              | 描述                        |
|:----------------|:--------------------------|
| source          | 执行来源 (BATCH/JOB)          |
| sourceId        | 执行来源id (JOB特有)            |
| seq             | 执行序列 (JOB特有)              |
| userId          | 执行用户id                    |
| username        | 执行用户名                     |
| execId          | 执行记录id                    |
| hostId          | 执行主机id                    | 
| hostName        | 执行主机名称                    |
| hostCode        | 执行主机编码                    |
| hostAddress     | 执行主机地址                    |
| hostUsername    | 执行主机用户名                   |
| osType          | 执行主机系统版本                  |
| port            | SSH 端口                    |
| charset         | SSH 编码集                   |
| scriptExec      | 是否使用脚本执行                  |
| scriptPath      | 脚本文件路径                    |
| uuid            | 生成任务维度 uuid               |
| uuidShort       | 生成任务维度 uuid 无 '-'         |
| hostUuid        | 生成机器维度 uuid               |
| hostUuidShort   | 生成机器维度 uuid 无 '-'         |
| timestampMillis | 时间戳毫秒                     |
| timestamp       | 时间戳                       |
| date            | 执行时间 yyyy-MM-dd           |
| datetime        | 执行时间 yyyy-MM-dd HH:mm :ss |
