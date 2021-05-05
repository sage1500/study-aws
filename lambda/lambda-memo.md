

## ロール作成とポリシーのアタッチ
```
aws iam create-role --role-name lambda-ex --assume-role-policy-document file://trust-policy.json
aws iam attach-role-policy --role-name lambda-ex --policy-arn arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole
```

1コマンド目の出力結果
```json
{
    "Role": {
        "Path": "/",
        "RoleName": "lambda-ex",
        "RoleId": "AROASXP36GEJEVIRMBUBF",
        "Arn": "arn:aws:iam::187896312082:role/lambda-ex",
        "CreateDate": "2021-05-03T09:00:30+00:00",
        "AssumeRolePolicyDocument": {
            "Version": "2012-10-17",
            "Statement": [
                {
                    "Effect": "Allow",
                    "Principal": {
                        "Service": "lambda.amazonaws.com"
                    },
                    "Action": "sts:AssumeRole"
                }
            ]
        }
    }
}
```

## zipにする
```
7z a function.zip index.js
```

## 関数登録

```
aws lambda create-function --function-name my-function --zip-file fileb://function.zip --handler index.handler --runtime nodejs12.x --role arn:aws:iam::187896312082:role/lambda-ex
```

```
aws lambda create-function --function-name my-function2 --zip-file fileb://build/distributions/01.zip --handler example.Handler --runtime java11 --role arn:aws:iam::187896312082:role/lambda-ex
```

### S3バケット経由
```
aws s3 cp my-function.zip s3://my-bucket
aws lambda update-function-code --function-name my-function --s3-bucket my-bucket --s3-key my-function.zip
```

出力結果
```json
{
    "FunctionName": "my-function",
    "FunctionArn": "arn:aws:lambda:ap-northeast-1:187896312082:function:my-function",
    "Runtime": "nodejs12.x",
    "Role": "arn:aws:iam::187896312082:role/lambda-ex",
    "Handler": "index.handler",
    "CodeSize": 310,
    "Description": "",
    "Timeout": 3,
    "MemorySize": 128,
    "LastModified": "2021-05-03T09:11:49.914+0000",
    "CodeSha256": "1dvIdmka/Klr6BThdycSLH6gOABBXmYfqnhJC5HpP5s=",
    "Version": "$LATEST",
    "TracingConfig": {
        "Mode": "PassThrough"
    },
    "RevisionId": "fb354e7e-18d5-41e7-90e9-75a3023cc4c4",
    "State": "Active",
    "LastUpdateStatus": "Successful",
    "PackageType": "Zip"
}
```

## (AWS上の)Lambda実行
```
aws lambda invoke --function-name my-function output.json
aws lambda invoke --function-name my-function output.json --log-type Tail
aws lambda invoke --function-name my-function output.json --log-type Tail --query 'LogResult' --output text |  base64 -d
```
※output.json: 関数の結果を書き込むファイル名
※イベントに載せるパラメータの設定: `--payload '{"key1": "value1", "key2": "value2", "key3": "value3"}'` 
※イベントに載せるパラメータの設定: `--payload file://xxxx.json` 



## Lambda関数一覧取得
aws lambda list-functions --max-items 10

## Lambda 関数を取得する
aws lambda get-function --function-name my-function

## Lambda 関数を更新する
7z u function.zip index.js
aws lambda update-function-code --function-name my-function --zip-file fileb://function.zip


## ロググループ削除
aws logs delete-log-group --log-group-name /aws/lambda/my-function

※★TODO★ Lamda の関数名はグローバルな命名則を考えた方がよさげ

## レイヤー
aws lambda publish-layer-version \
    --layer-name <value> \ 
    --content S3Bucket=<value>,S3Key=<value> \
    --compatible-runtimes <value>
aws lambda list-layers [--compatible-runtime <value>]
aws lambda list-layer-versions --layer-name <value>
aws lambda delete-layer-version --layer-name <value> --version-number <value>
aws lambda get-layer-version --layer-name <value> --version-number <value>

create-function
update-function-configuration
update-function-code
    --function-name <value>
    [--s3-bucket <value>]
    [--s3-key <value>]
list-functions
get-function --function-name <value>
delete-function
invoke

