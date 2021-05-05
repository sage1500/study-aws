Lambda Java メモ
===

## Handler

### インタフェース

以下のいずれかを利用する。

- `com.amazonaws.services.lambda.runtime.RequestHandler`
- `com.amazonaws.services.lambda.runtime.RequestStreamHandler`

### 入力パラメータ例
- `Map<String,String>`
- `List<Integer>`
- `Integer`
- `String`
- 任意のPOJO

### 出力パラメータ例
- `String` ※通常の文字列
- `String` ※JSON
- `Integer`
- 任意のPOJO

## その他入出力パラメータ例
- SQSEvent / String
- S3Event / String
- DynamodbEvent / String
- KinesisEvent / String
- APIGatewayV2ProxyRequestEvent / APIGatewayV2ProxyResponseEvent 
- APIGatewayProxyRequestEvent / APIGatewayProxyResponseEvent
- ScheduledEvent / String ※CloudWatch Event
- CloudWatchLogsEvent / String
- CloudFrontEvent / String
- CodeCommitEvent / String
- CognitoEvent / String
- ConfigEvent / String
- KinesisFirehoseEvent / String
- LexEvent / String
- SNSEvent / String

## Lambda レイヤー

https://docs.aws.amazon.com/ja_jp/lambda/latest/dg/configuration-layers.html

- 関数では一度に 5 つのレイヤー 使用できます。
- 関数とすべてのレイヤーの解凍後の合計サイズが、解凍後のデプロイパッケージのサイズ制限 250 MB を超えることはできません。
- 独自のレイヤーを作成することもできまる。
- AWS または AWS のお客様が公開したレイヤーを使用することもできます。
- レイヤーは、特定の AWS アカウント (AWS Organizations)、またはすべてのアカウントに対して、
    レイヤーの使用に関するアクセス権限を付与するためのリソースベースのポリシーをサポートしています。
- AWS サーバーレスアプリケーションモデル (AWS SAM) を使用して、レイヤー、および関数のレイヤー設定を管理することもできます。  
    ※手順については、AWS サーバーレスアプリケーションモデル 開発者ガイド の「レイヤーの使用」をご参照ください。  
        https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/serverless-sam-cli-layers.html

### レイヤー作成
マネージメントコンソールの場合、関数の他にレイヤーというメニューがあるので、そちらから  
https://ap-northeast-1.console.aws.amazon.com/lambda/home?region=ap-northeast-1#/layers

```
aws lambda publish-layer-version --layer-name my-layer --description "My layer"  \ 
    --license-info "MIT" --content S3Bucket=lambda-layers-us-east-2-123456789012,S3Key=layer.zip \
    --compatible-runtimes python3.6 python3.7 python3.8
```

### その他レイヤー操作
```
aws lambda list-layers --compatible-runtime python3.8
aws lambda get-layer-version --layer-name my-layer --version-number 2
aws lambda delete-layer-version --layer-name my-layer --version-number 1
```

### Zipの構成

- ルートディレクトリ: `java/lib`
- Zip化する Gradle タスクの記述例:  
    ```groovy
    task packageLibs(type: Zip) {
        into('java/lib') {
            from configurations.runtimeClasspath
        }
    }
    ```
