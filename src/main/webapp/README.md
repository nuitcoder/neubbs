## Neubbs 前端

需要安装 [node.js](https://nodejs.org/zh-cn/)，使用 npm 进行包管理

## 切换 npm 国内源（仅需要操作一次）

- 使用 npm 淘宝源安装 nrm
```
$ npm install nrm -g --registry=https://registry.npm.taobao.org
```

- 查看当前使用的 npm 源
```
$ nrm ls

# 输出
# * npm ---- https://registry.npmjs.org/
#   cnpm --- http://r.cnpmjs.org/
#   taobao - https://registry.npm.taobao.org/
#   nj ----- https://registry.nodejitsu.com/
#   rednpm - http://registry.mirror.cqupt.edu.cn/
#   npmMirror  https://skimdb.npmjs.com/registry/
#   edunpm - http://registry.enpmjs.org/
```

- 使用 npm 淘宝源
```
$ nrm use taobao
```

## 安装依赖
```
$ npm install
```

## 编译
```
$ npm run watch # 实时编译

# 或者

$ npm run build # 单次编译
```
