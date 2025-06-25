<div align="center"><img src="https://oss.orionsec.cn/orion-visor/logo_horizontal.png" alt="logo" width="520" /></div>
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
* **在线终端**：提供在线终端 SSH/RDP 等多种协议，支持快捷命令、自定义快捷键和主题风格。
* **文件管理**：支持远程主机 SFTP 大文件的批量上传、下载和在线编辑等操作。
* **批量操作**：支持批量执行主机命令、多主机文件分发等功能。
* **计划任务**：支持配置 cron 表达式，定时执行主机命令。
* **安全可靠**：动态配置权限，记录用户操作日志，提供简单的审计功能。

## 演示环境

* 🔗 演示地址: [https://dv.orionsec.cn/](https://dv.orionsec.cn/)
* 🔏 演示账号: admin/admin
* ⭐ 体验后可以点一下 `star`
  这对我很重要! [github](https://github.com/dromara/orion-visor) [gitee](https://gitee.com/dromara/orion-visor) [gitcode](https://gitcode.com/dromara/orion-visor)
* 🌈 如果本项目对你有帮助请帮忙推广一下 让更多的人知道此项目!
* 🎭 演示环境部分功能不可用, 完整功能请本地部署!
* 📛 演示环境请不要随便删除数据!
* 📧 如果演示环境不可用请联系我!
* 📨 **作者已被毕(cai)业(yuan) 寻java高级内推 望京/5号/10号线 有坑位的联系我哦(随缘)** 微信: `ljh1553488`

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

* [文档地址](https://visor.dromara.org/)
* [安装文档](https://visor.dromara.org/quickstart/docker.html)
* [更新日志](https://visor.dromara.org/update/change-log.html)
* [操作手册](https://visor.dromara.org/operator/asset.html)
* [常见问题](https://visor.dromara.org/support/faq.html)

## 技术栈

* SpringBoot 2.7.17
* Mysql 8.0+
* Redis 6.0+
* Vue3 3.5+
* Arco Design 2.56+

## 主要功能预览

#### 工作台

![工作台](https://oss.orionsec.cn/orion-visor/screenshot/workplace.png "工作台")

#### 主机终端

![新建连接](https://oss.orionsec.cn/orion-visor/screenshot/terminal-hosts.png "新建连接")
![ssh](https://oss.orionsec.cn/orion-visor/screenshot/terminal-ssh.png "ssh")
![sftp](https://oss.orionsec.cn/orion-visor/screenshot/terminal-sftp.png "sftp")
![rdp](https://oss.orionsec.cn/orion-visor/screenshot/terminal-rdp.png "rdp")
![主题设置](https://oss.orionsec.cn/orion-visor/screenshot/terminal-theme.png "主题设置")

#### 主机列表

![主机列表](https://oss.orionsec.cn/orion-visor/screenshot/host-list.png "主机列表")

#### 批量执行

![批量执行](https://oss.orionsec.cn/orion-visor/screenshot/exec-command.png "批量执行")
![执行日志](https://oss.orionsec.cn/orion-visor/screenshot/exec-log.png "执行日志")

#### 批量上传

![批量上传任务](https://oss.orionsec.cn/orion-visor/screenshot/batch-upload.png "批量上传任务")

#### 计划任务

![计划任务详情](https://oss.orionsec.cn/orion-visor/screenshot/exec-job.png "计划任务详情")

## Star History

[![Star History Chart](https://api.star-history.com/svg?repos=dromara/orion-visor&type=Date)](https://star-history.com/#dromara/orion-visor&Date)

## 关于我

本人专注于使用 Java 和 Vue 进行全栈开发, 并在系统自动化运维方面拥有丰富开发的经验。如果您在这些领域有需求或遇到痛点, 请随时联系我, 并备注“合作”。

## 联系我

<img src="https://oss.orionsec.cn/orion-visor/vx.jpg" alt="vx" width="628px"/>  

微信: ljh1553488  
QQ群: 755242157

📧 问题/加群微信备注: ops  
📧 合作/功能定制备注: 合作

## 支持一下

<img src="https://oss.orionsec.cn/orion-visor/pay.jpg" alt="收款码" width="540px"/>  

🎁 为了项目能健康持续的发展, 我期望获得相应的资金支持, 你们的支持是我不断更新前进的动力!

## 免责声明

在使用本项目之前, 请确保您已经了解并同意相关的使用协议和隐私政策。[免责声明](https://github.com/dromara/orion-visor/blob/main/DISCLAIMER.md)

## License

本项目遵循 [Apache-2.0](https://github.com/dromara/orion-visor/blob/main/LICENSE) 开源许可证。

## Gitee 最有价值的开源项目 GVP

![GVP](https://oss.orionsec.cn/orion-visor/gvp.jpg "GVP")

## GitCode 最有影响力的开源项目 G-Star

![GSTAR](https://oss.orionsec.cn/orion-visor/gstar.jpg "GSTAR")
