Lambda Nodejs メモ
===

## Handler

### 引数
```
exports.handler = function(event) { }
exports.handler = function(event, context) { }
exports.handler = function(event, context, callback) { }

 event: event オブジェクト
 context: コンテキストオブジェクト
 callback: コールバック関数
```

### 返値

- 同期ハンドラー
    - callback で通知する（？）
- 非同期ハンドラー  
    以下のいずれか
    - レスポンス
    - エラー
    - Promise
