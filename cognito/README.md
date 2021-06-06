# cognito

- ポイント
  - Cognito 上の設定
    - Amazon Cognito ドメイン  
      なんでもよいので設定する。
  - Spring Security 上の設定
    - client_id: <アプリクライアントID>  
      KeyCloakと違い、払いだされたIDを設定する。
    - issuer-uri: https://cognito-idp.ap-northeast-1.amazonaws.com/ + <プールID>

## サンプルプログラム実行について

サンプルプログラムを実行するには以下の設定が必要。

- 環境変数
  - COGNITO_USER_POOL_ID : ユーザプールのID
  - COGNITO_DOMAIN_PREFIX : ユーザプールのドメインのプレフィックス
  - COGNITO_CLIENT_ID : ユーザプールのクライアントのID
  - COGNITO_CLIENT_SECRET : ユーザプールのクライアントのシークレット
- その他、AWS CLI が動作するのと同じ環境設定  
  ※デフォルトのプロファイルで接続できない状態の場合は、AWS_PROFILE の設定など。
