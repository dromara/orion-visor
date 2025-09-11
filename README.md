<div align="center"><img src="docs/assets/logo_horizontal.png?time=20250627" alt="logo" width="520" /></div>
<p style="margin-top: 12px" align="center"><b>【Dromara】 一款高颜值、现代化的自动化运维&轻量堡垒机平台。</b></p>
<p align="center">
    <a target="_blank"
       style="text-decoration: none !important;"
       href="https://app.codacy.com/gh/lijiahangmax/orion-visor/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade">
      <img src="https://app.codacy.com/project/badge/Grade/49eaab3a9a474af3b87e1d21ffec71c4" alt="quality" />
    </a>
    <a target="_blank"
       style="text-decoration: none !important;"
       href="https://www.apache.org/licenses/LICENSE-2.0">
      <img src="https://img.shields.io/github/license/dromara/orion-visor" alt="License" />
    </a>
    <a target="_blank"
       style="text-decoration: none !important;"
       href="https://github.com/dromara/orion-visor/releases">
      <img src="https://img.shields.io/github/v/release/dromara/orion-visor" alt="release" />
    </a>
    <a target="_blank"
       style="text-decoration: none !important;"
       href="https://gitee.com/dromara/orion-visor/stargazers">
      <img src="https://gitee.com/dromara/orion-visor/badge/star.svg?theme=gvp" alt="star" />
    </a>
    <a target="_blank"
       style="text-decoration: none !important;"
       href="https://gitee.com/dromara/orion-visor/members">
      <img src="https://gitee.com/dromara/orion-visor/badge/fork.svg?theme=gvp" alt="fork" />
    </a>
    <a target="_blank"
       style="text-decoration: none !important;"
       href="https://github.com/dromara/orion-visor">
      <img src="https://img.shields.io/github/stars/dromara/orion-visor" alt="star" />
    </a>
    <a target="_blank"
       style="text-decoration: none !important;"
       href="https://github.com/dromara/orion-visor">
      <img src="https://img.shields.io/github/forks/dromara/orion-visor" alt="star" />
    </a>
    <a target="_blank"
       style="text-decoration: none !important;"
       href="https://gitcode.com/dromara/orion-visor">
      <img src="https://gitcode.com/dromara/orion-visor/star/badge.svg" alt="star"/>
    </a>
</p>

------------------------------

**`orion-visor`** 提供一站式自动化运维解决方案。

* **资产管理**：支持对资产进行分组，实现对主机、密钥和身份的统一管理和授权。
* **在线终端**：提供在线终端 SSH/RDP/VNC 等多种协议，支持快捷命令、自定义快捷键和主题风格。
* **文件管理**：支持远程主机 SFTP 大文件的批量上传、下载和在线编辑等操作。
* **批量操作**：支持批量执行主机命令、多主机文件分发等功能。
* **计划任务**：支持配置 cron 表达式，定时执行主机命令。
* **系统监控**：支持对主机 CPU、内存、磁盘、网络等系统指标的监控和告警。
* **安全可靠**：动态配置权限，记录用户操作日志，提供简单的审计功能。

## 演示环境

* 🔗 演示地址: [https://dv.orionsec.cn/](https://dv.orionsec.cn/)
* 🔏 演示账号: admin/admin
* ⭐ 体验后可以点一下 `star`
  这对我很重要! [github](https://github.com/dromara/orion-visor) [gitee](https://gitee.com/dromara/orion-visor) [gitcode](https://gitcode.com/dromara/orion-visor)
* 🌈 如果本项目对你有帮助请帮忙推广一下 让更多的人知道此项目!
* 🎭 演示环境部分功能不可用, 完整功能请本地部署!

## 快速开始

```bash
# clone
git clone --depth=1 https://github.com/dromara/orion-visor
cd orion-visor
# 启动
docker compose up -d
# 等待后端服务启动后 (2min±) 访问 http://localhost:1081/
```

## 项目文档

* [文档地址](https://visor.orionsec.cn/)
* [安装文档](https://visor.orionsec.cn/quickstart/docker.html)
* [更新日志](https://visor.orionsec.cn/update/change-log.html)
* [操作手册](https://visor.orionsec.cn/operator/asset.html)
* [常见问题](https://visor.orionsec.cn/support/faq.html)

## 技术栈

* SpringBoot 2.7+
* Mysql 8.0+
* Redis 6.0+
* InfluxDB 2.7+
* Vue 3.5+
* Arco Design 2.56+

## 主要功能预览

#### 工作台

![工作台](docs/assets/screenshot/workplace.png?time=20250627 "工作台")

#### 主机终端

![新建连接](docs/assets/screenshot/terminal-hosts.png?time=20250627 "新建连接")
![ssh](docs/assets/screenshot/terminal-ssh.png?time=20250627 "ssh")
![sftp](docs/assets/screenshot/terminal-sftp.png?time=20250627 "sftp")
![rdp](docs/assets/screenshot/terminal-rdp.png?time=20250627 "rdp")
![主题设置](docs/assets/screenshot/terminal-theme.png?time=20250627 "主题设置")

#### 主机列表

![主机列表](docs/assets/screenshot/host-list.png?time=20250627 "主机列表")

#### 主机监控

![主机监控](docs/assets/screenshot/monitor-list.png?time=20250627 "主机监控")
![监控详情](docs/assets/screenshot/monitor-detail.png?time=20250627 "监控详情")

#### 批量执行

![批量执行](docs/assets/screenshot/exec-command.png?time=20250627 "批量执行")
![执行日志](docs/assets/screenshot/exec-log.png?time=20250627 "执行日志")

#### 批量上传

![批量上传任务](docs/assets/screenshot/batch-upload.png?time=20250627 "批量上传任务")

#### 计划任务

![计划任务详情](docs/assets/screenshot/exec-job.png?time=20250627 "计划任务详情")

## Star History

[![Star History Chart](https://api.star-history.com/svg?repos=dromara/orion-visor&type=Date)](https://star-history.com/#dromara/orion-visor&Date)

## 关于我

本人专注于使用 Java 和 Vue 进行全栈开发, 并在系统自动化运维方面拥有丰富开发的经验, 并提供企业级的解决方案。如果您在这些领域有需求或遇到痛点, 请随时联系我,
并备注“合作”。

## 联系我

<img src="docs/assets/vx.jpg?time=20250627" alt="vx" width="628px"/>  

微信: ljh1553488  
QQ群: 755242157

📧 问题/加群微信备注: ops  
📧 合作/功能定制备注: 合作

## 支持一下

<img src="docs/assets/pay.jpg?time=20250627" alt="收款码" width="540px"/>  

🎁 为了项目能健康持续的发展, 我期望获得相应的资金支持, 你们的支持是我不断更新前进的动力!

## 免责声明

在使用本项目之前, 请确保您已经了解并同意相关的使用协议和隐私政策。[免责声明](https://github.com/dromara/orion-visor/blob/main/DISCLAIMER.md)

## License

本项目遵循 [Apache-2.0](https://github.com/dromara/orion-visor/blob/main/LICENSE) 开源许可证。

## 贡献者

[![Contributors](https://contri.buzz/api/wall?repo=dromara/orion-visor)](https://github.com/dromara/orion-visor, "Contributors")

## Gitee 最有价值的开源项目 GVP

![GVP](docs/assets/gvp.jpg?time=20250627 "GVP")

## GitCode 最有影响力的开源项目 G-Star

![GSTAR](docs/assets/gstar.jpg?time=20250627 "GSTAR")
