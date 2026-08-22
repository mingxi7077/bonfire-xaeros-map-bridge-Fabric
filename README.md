# Bonfire Xaero's Map Bridge (Fabric)

[English](#english) | [简体中文](#简体中文)

Bonfire Xaero's Map Bridge is a Fabric client companion for unified Xaero multiplayer storage.

Bonfire Xaero's Map Bridge 是一个用于统一 Xaero 多人地图存档的 Fabric 客户端附属模组。

---

## English

Bonfire Xaero's Map Bridge is a Fabric client companion that unifies Xaero multiplayer map storage and migrates older `Multiplayer_*` data into one stable Bonfire directory.

### What It Changes

- Stops Xaero's Minimap from splitting multiplayer data by server address.
- Stops Xaero's World Map from splitting multiplayer data by server address.
- Migrates old multiplayer map folders into one shared Bonfire target directory.

### Compatibility

- Minecraft Java `1.21.8`
- Fabric Loader `0.18.3+`
- Xaero's Minimap `25.3.5`
- Xaero's World Map `1.40.6`
- Java `21`

### Repository Layout

- `src/`: mod source code
- `build.gradle`: Gradle build definition
- `build/`: generated output, excluded from release tracking

### Build

```powershell
.\gradlew.bat build
```

### License

This repository is released under the [MIT License](LICENSE).

---

## 简体中文

Bonfire Xaero's Map Bridge 是一个 Fabric 客户端附属模组，用来统一 Xaero 多人地图数据的目录结构，并把旧的 `Multiplayer_*` 存档迁移到稳定的 Bonfire 目录下。

### 它修改了什么

- 阻止 Xaero's Minimap 按服务器地址拆分多人地图数据。
- 阻止 Xaero's World Map 按服务器地址拆分多人地图数据。
- 将旧的多人地图目录迁移到统一的 Bonfire 目标路径。

### 兼容性

- Minecraft Java `1.21.8`
- Fabric Loader `0.18.3+`
- Xaero's Minimap `25.3.5`
- Xaero's World Map `1.40.6`
- Java `21`

### 仓库结构

- `src/`：模组源码
- `build.gradle`：Gradle 构建定义
- `build/`：生成输出，不纳入发布源码

### 构建方式

```powershell
.\gradlew.bat build
```

### 授权

本仓库采用 [MIT License](LICENSE) 开源。

---

## 联系方式 / Contact

项目问题或合作沟通，请发送邮件至 [mingxi7707@qq.com](mailto:mingxi7707@qq.com)。
For project questions or collaboration, email [mingxi7707@qq.com](mailto:mingxi7707@qq.com).
