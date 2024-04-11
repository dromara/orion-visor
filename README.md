<h1 style="display: flex; align-items: center;">
 <img style="margin-right: 8px;" src="https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/2/27/8c687ef1-5711-4a93-9db0-79c010af7902.png" width="32px" height="32px"/> orion-ops-pro 是什么
</h1>

`orion-ops-pro`
是一款现代化、高颜值的一站式智能运维管理平台，集资产管理、资产授权、批量执行、Web终端、WebSftp、角色管理、系统管理等功能于一体，致力于简化运维团队的治理工作。它是基于 `orion-ops`
的产品思路进行重构，技术架构升级，并优化了交互逻辑，让操作更快捷更友好。

<p style="text-align: left">
    <a target="_blank" style="text-decoration: none" href="https://app.codacy.com/gh/lijiahangmax/orion-ops-pro/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade">
        <img src="https://app.codacy.com/project/badge/Grade/49eaab3a9a474af3b87e1d21ffec71c4" alt="quality"/>
    </a>
	<a target="_blank" style="text-decoration: none" href="https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html">
		<img src="https://img.shields.io/badge/JDK-8+-green.svg" alt="jdk8"/>
	</a>
	<a target="_blank" style="text-decoration: none" href="https://www.apache.org/licenses/LICENSE-2.0">
		<img src="https://img.shields.io/github/license/lijiahangmax/orion-ops-pro" alt="License"/>
	</a>
	<a target="_blank" style="text-decoration: none" href="https://gitee.com/lijiahangmax/orion-ops-pro/stargazers">
		<img src="https://gitee.com/lijiahangmax/orion-ops-pro/badge/star.svg?theme=dark" alt="star"/>
	</a>
	<a target="_blank" style="text-decoration: none" href="https://gitee.com/lijiahangmax/orion-ops-pro/members">
		<img src="https://gitee.com/lijiahangmax/orion-ops-pro/badge/fork.svg?theme=dark" alt="fork"/>
	</a>		
	<!-- <a target="_blank" style="text-decoration: none" href="https://github.com/lijiahangmax/orion-ops-pro">
		<img src="https://img.shields.io/github/stars/lijiahangmax/orion-ops-pro.svg?style=social" alt="star"/>
	</a> -->	
</p>

<br/>  

当前版本: **1.0.3**  
github: https://github.com/lijiahangmax/orion-ops-pro  
gitee: https://gitee.com/lijiahangmax/orion-ops-pro  
文档: https://lijiahangmax.gitee.io/orion-ops-pro/#/    
demo: http://101.43.254.243:1081/

演示账号: `admin`    
演示密码: `admin`  
⭐ 体验后可以点一下 `star` 这对我很重要  
📞 合作/功能定制请联系底部 备注: '定制'

## 特性

* 易用便捷: 极简配置, 开箱即用, 支持 Docker 部署方式。
* 资产管理: 支持灵活配置主机分组, 实现对主机、秘钥和身份的统一管理。
* 资产授权: 可将资产数据授权给指定角色和用户, 确保数据安全性。
* 权限控制: 全面管理用户角色, 支持动态菜单配置和强制下线等功能。
* 在线终端: 提供便捷的在线 Web 终端服务, 支持快捷命令、自定义快捷键和主题风格。
* 文件管理: 实现远程主机大文件的批量上传、下载和在线编辑等操作。
* 批量操作: 支持远程主机批量执行 shell 命令。
* 计划任务: 支持配置 cron 表达式, 定时执行主机 shell 命令。
* 操作日志: 记录用户操作日志，确保操作可追溯, 提高系统安全性。
* 可扩展性: 前后端代码规范统一、代码质量高、健壮且易于阅读和扩展。

## 快速开始

docker安装: https://lijiahangmax.gitee.io/orion-ops-pro/#/quickstart/docker-install   
安装文档: https://lijiahangmax.gitee.io/orion-ops-pro/#/quickstart/install   
开发文档: https://lijiahangmax.gitee.io/orion-ops-pro/#/advance/dev   
操作手册: https://lijiahangmax.gitee.io/orion-ops-pro/#/operator/asset  
常见问题: https://lijiahangmax.gitee.io/orion-ops-pro/#/quickstart/faq  
roadmap: https://lijiahangmax.gitee.io/orion-ops-pro/#/about/roadmap

## 技术栈

* Docker
* SpringBoot
* Mysql
* Redis
* Vue3
* Arco Design

## 功能预览

> 工作台

![工作台](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/13d79a89-aadf-4100-8bb3-afb03758001f.png "工作台")

> 资产管理

![主机列表](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/d9954335-9afa-4579-b040-a1c3006cb1f0.png "主机列表")
![资产授权](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/ffbdd0e2-4811-4776-a96c-7b5d9b4f3e89.png "资产授权")

> 主机终端

![主机终端](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/de6ae2bb-3d9a-44d6-b530-664febee7dbc.png "主机终端")
![命令片段](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/0a2a8077-fb47-4c87-8327-9d6b93ecc552.png "命令片段")
![主题设置](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/20741d51-af62-40f0-bd6f-6e954d9b0398.png "主题设置")
![终端设置](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/a3bf32bc-26b5-4ec7-b429-54c17ccd136b.png "终端设置")
![sftp](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/0ae07072-1740-4f84-aaf7-c18a8074ce61.png "sftp")
![传输列表](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/ccf880a4-c393-4a35-9f35-fe7572256edd.png "传输列表")

> 批量执行

![批量执行](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/0a222b64-d2c1-481c-99b8-c3a0616d2fab.png "批量执行")
![执行日志](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/06d02d38-70ef-43c2-950c-9f8c73a105ba.png "执行日志")
![执行记录](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/0e474cc2-f7cf-49bc-be3c-f6445783ad7c.png "执行记录")

> 用户管理

![用户列表](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/0d5f26e0-de4e-4342-800c-30a0d5d3078e.png "用户列表")  
![个人中心](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/ed1e5e02-f854-44ee-bb37-ea6e45526457.png "个人中心")
![操作日志](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/ba6f1526-da00-4a3d-a550-470a6b3d2803.png "操作日志")

> 系统管理

![系统菜单](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/5087cd35-6a65-4338-bc87-c81969cdb947.png "系统菜单")  
![分配菜单](https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/3/22/5a7804ed-179c-4d25-820f-af2af1aabbba.png "分配菜单")

## 联系我

<div style="display: flex;">
  <img src="https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/2/27/d452cd30-cecd-4236-86f5-5ecbf3eac091.jpg" alt="qq" width="268px" height="398px"/>  
  <img src="https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/2/27/4f1c4e77-8e36-45a3-8be6-9da5387bb96e.jpg" alt="wx" width="298px" height="398px"/>  
</div>

📧 咨询问题微信备注: ops  
📧 合作/功能定制备注: 合作

## 支持一下

<img src="https://bjuimg.obs.cn-north-4.myhuaweicloud.com/images/2024/2/27/8063cf87-9de9-4df2-8009-d5dea6d69861.jpg" alt="收款码" width="540px"/>  

🎁 为了项目能健康持续的发展, 我期望获得相应的资金支持, 你们的支持是我不断更新前进的动力!

## 免责声明

在使用本项目之前, 请确保您已经了解并同意相关的使用协议和隐私政策。[免责声明](https://github.com/lijiahangmax/orion-ops-pro/blob/main/DISCLAIMER.md)

## License

本项目遵循 [Apache-2.0](https://github.com/lijiahangmax/orion-ops-pro/blob/main/LICENSE) 开源许可证。  
