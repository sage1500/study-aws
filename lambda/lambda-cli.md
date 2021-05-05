
## ロール作成とポリシーのアタッチ
```sh
$ aws iam create-role --role-name ${ROLE_NAME} --assume-role-policy-document file://trust-policy.json
$ aws iam attach-role-policy --role-name ${ROLE_NAME} --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
```

## zipにする
```
$ 7z a function.zip index.js
```

## 関数登録
```sh
# nodejs の例
$ aws lambda create-function \
    --function-name my-function \
    --zip-file fileb://function.zip \
    --handler index.handler \
    --runtime nodejs12.x \
    --role arn:aws:iam::${AWS_ACCOUNT_ID}:role/${ROLE_NAME}
```
```sh
# java の例
$ aws lambda create-function \
    --function-name my-function2 \
    --zip-file fileb://build/distributions/01.zip \
    --handler example.Handler \
    --runtime java11 \
    --role arn:aws:iam::${AWS_ACCOUNT_ID}:role/${ROLE_NAME}
```

## (AWS上の)Lambda実行
```sh
$ aws lambda invoke --function-name my-function output.json
$ aws lambda invoke --function-name my-function output.json --log-type Tail
$ aws lambda invoke --function-name my-function output.json --log-type Tail --query 'LogResult' --output text | base64 -d
```
※output.json: 関数の結果を書き込むファイル名  
※イベントに載せるパラメータの設定: `--payload '{"key1": "value1", "key2": "value2", "key3": "value3"}'`  ※base64化が必要っぽい  
※イベントに載せるパラメータの設定: `--payload file://xxxx.json`   



## 関数を更新する
```sh
# ローカルファイルから直接
$ 7z u function.zip index.js
$ aws lambda update-function-code \
    --function-name my-function \
    --zip-file fileb://function.zip
```

```sh
# S3経由
$ aws s3 cp function.zip s3://my-bucket
$ aws lambda update-function-code \
    --function-name my-function \
    --s3-bucket my-bucket \
    --s3-key function.zip
```

## 関数を削除する
```
$ aws lambda delete-function --function-name <value>
```


## ロググループ削除
```
$ aws logs delete-log-group --log-group-name <value>
```

※★TODO★ Lamda の関数名はグローバルな命名則を考えた方がよさげ

## 一覧

|カテゴリ|コマンド|説明|
|-|-|-|
|関数|aws lambda create-function|関数作成|
||aws lambda update-function-code|関数コード更新|
||aws lambda update-function-configuration|関数設定更新|
||aws lambda delete-function|関数削除|
||aws lambda list-functions|関数一覧取得|
||aws lambda get-function|関数取得|
||aws lambda invoke|関数呼び出し|
|レイヤー|aws lambda publish-layer-version|レイヤー作成・更新|
||aws lambda list-layers|レイヤー一覧取得|
||aws lambda list-layer-versions|レイヤーバージョン一覧取得|
||aws lambda delete-layer-version|レイヤーバージョン削除|
||aws lambda get-layer-version|レイヤーバージョン取得|


### コマンドパラメータ（代表的なもののみ）

```
$ aws lambda update-function-code
    --function-name <value>
    [--s3-bucket <value>]
    [--s3-key <value>]

$ aws lambda list-functions --max-items <value>

$ aws lambda get-function \
    --function-name <value>

$ aws lambda publish-layer-version \
    --layer-name <value> \ 
    --content S3Bucket=<value>,S3Key=<value> \
    --compatible-runtimes <value>

$ aws lambda list-layers \
    [--compatible-runtime <value>]

$ aws lambda list-layer-versions \
    --layer-name <value>

$ aws lambda delete-layer-version \
    --layer-name <value> \
    --version-number <value>

$ aws lambda get-layer-version \
    --layer-name <value> \
    --version-number <value>
```
