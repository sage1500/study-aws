# cognito

- ポイント
  - Cognito 上の設定
    - Amazon Cognito ドメイン  
      なんでもよいので設定する。
  - Spring Security 上の設定
    - client_id: <アプリクライアントID>  
      KeyCloakと違い、払いだされたIDを設定する。
    - issuer-uri: https://cognito-idp.ap-northeast-1.amazonaws.com/ + <プールID>
