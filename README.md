# Bonfire Xaero's Map Bridge (Fabric)

Bonfire 专用 Fabric 附属模组。

这个模组的目标很单纯：

- 强制 `Xaero's Minimap` 不再按服务器域名/IP 拆分根地图目录
- 强制 `Xaero's World Map` 不再按服务器域名/IP 拆分根地图目录
- 把多人地图统一固定到 `Multiplayer_Any Address`
- 首次启动时尝试把旧的 `Multiplayer_*` 数据复制迁移到统一目录

这样同一台服务器即使换了多个入口域名、分流域名、备用域名或直连 IP，客户端也会继续使用同一套 Xaero 地图数据。

## 目标环境

- Minecraft Java `1.21.8`
- Fabric Loader `0.18.3`
- Xaero's Minimap `25.3.5`
- Xaero's World Map `1.40.6`
- Java `21`

## 当前实现

- 运行时通过 Mixin 拦截 Xaero 的“按服务器地址区分地图根目录”判断，强制关闭
- 启动时把下列配置改为 `false`
  - `config/xaero/minimap/client.cfg`
  - `config/xaero/world-map/client.cfg`
  - 若存在，也兼容修正旧版平面配置文件
- 启动时扫描 `xaero/minimap/Multiplayer_*` 与 `xaero/world-map/Multiplayer_*`
- 将旧目录内容复制合并到 `Multiplayer_Any Address`

## 构建

Windows:

```bat
gradlew.bat build
```

产物位置：

`build/libs/bonfire-xaeros-map-bridge-fabric-0.1.0+1.21.8.jar`

## 部署

将构建出的 jar 放进对应客户端版本的 `mods` 文件夹，并同时安装：

- `Xaero's Minimap`
- `Xaero's World Map`

## 说明

这是 Bonfire 特供固定版，当前行为是硬编码的：

- 多人地图固定根目录：`Multiplayer_Any Address`
- 不提供客户端内切换开关

如果后续你要，我可以继续把它扩展成：

- 可配置固定目录名
- 首次迁移报告文件
- 更激进的目录冲突合并策略
