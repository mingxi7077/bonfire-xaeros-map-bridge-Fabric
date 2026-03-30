# Bonfire Xaero's Map Bridge (Fabric)

![License](https://img.shields.io/badge/license-BNSL--1.0-red)
![Commercial Use](https://img.shields.io/badge/commercial-use%20by%20written%20permission%20only-critical)
![Platform](https://img.shields.io/badge/platform-Fabric%201.21.8-brightgreen)
![Side](https://img.shields.io/badge/side-client-blueviolet)
![Java](https://img.shields.io/badge/java-21-orange)

Bonfire Xaero's Map Bridge is a Fabric client companion that unifies Xaero multiplayer map storage and migrates old `Multiplayer_*` data into one stable Bonfire directory.

> Non-commercial source-available. Commercial use requires prior written permission via `mingxi7707@qq.com`.

## What It Changes

- Stops Xaero's Minimap from splitting multiplayer data by server address.
- Stops Xaero's World Map from splitting multiplayer data by server address.
- Migrates older multiplayer map folders into a single shared target directory.

## Compatibility

- Minecraft Java `1.21.8`
- Fabric Loader `0.18.3+`
- Xaero's Minimap `25.3.5`
- Xaero's World Map `1.40.6`
- Java `21`

## Build

```powershell
.\gradlew.bat build
```

## Repository Scope

- Source only.
- Generated jars, remapped outputs, and local run data are excluded from Git.

## License

Bonfire Non-Commercial Source License 1.0

Commercial use is prohibited unless you first obtain written permission from `mingxi7707@qq.com`.
