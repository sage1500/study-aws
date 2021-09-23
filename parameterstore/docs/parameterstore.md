ParameterStore
===

## IAMポシリー

https://docs.aws.amazon.com/ja_jp/systems-manager/latest/userguide/sysman-paramstore-access.html

## パラメータ名の制約

https://docs.aws.amazon.com/ja_jp/systems-manager/latest/userguide/sysman-paramstore-su-create.html


## パラメータ設計

パラメータ名は、以下を考慮して設計すべき。

- 上記「パラメータ名の制約」
- 下記「spring-cloud-aws-parameter-store-config」の規則
- spring 以外からの利用の考慮



## Spring統合

- Spring Cloud の Spring Cloud AWS (旧)
  - https://github.com/spring-cloud/spring-cloud-aws
  - groupId:artifactId: org.springframework.cloud:spring-cloud-aws-parameter-store-config
  - groupId:artifactId: org.springframework.cloud:spring-cloud-starter-aws-parameter-store-config
- AWSpring Cloud の Spring Cloud AWS (新)
  - https://github.com/awspring/spring-cloud-aws
  - groupId:artifactId: io.awspring.cloud:spring-cloud-aws-parameter-store-config
  - groupId:artifactId: io.awspring.cloud:spring-cloud-starter-aws-parameter-store-config


### spring-cloud-aws-parameter-store-config

#### 依存関係

- io.awspring.cloud:spring-cloud-starter-aws-parameter-store-config
- org.springframework.cloud:spring-cloud-starter-bootstrap ※これがないと自動構成が動かない

dependencyManagement:

```xml
<dependencyManagement>
	<dependencies>
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-dependencies</artifactId>
			<version>${spring-cloud.version}</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
		<dependency>
			<groupId>io.awspring.cloud</groupId>
			<artifactId>spring-cloud-aws-dependencies</artifactId>
			<version>${spring-cloud-aws.version}</version>
			<type>pom</type>
			<scope>import</scope>
		</dependency>
	</dependencies>
</dependencyManagement>
```


#### プロパティ:

spring-cloud-aws-parameter-store の設定をプロパティに設定する。
ただし、`application.yml` ではなく、`bootstrap.yml` に定義する必要があることに注意。
そのため、`spring.application.name` も bootstrap.yml に定義する必要がある。  
※なお、`application-${spring.profiles.active}.yml` 同様に、 `bootstrap-${spring.profiles.active}.yml` も利用可。

設定可能なパラメータは以下のとおり。（設定値はデフォルト値）
```yml
aws.paramstore:
  prefix: /config
  defaultContext: application
  profileSeparator: _
  region:  (なし)
  endpoint: (なし) ## エンドポイントをAWSから LocalStack 等に変えたいときに設定する。
  failFast: true
  name: (なし) ## 未設定の場合は、AutoConfigure で ${spring.application.name} が設定される。
  enabled: true
```

以下を `application.yml` または、`bootstrap.yml` に定義して、ログを有効にするとそのパラメータを対象にしたのかログに出力されるので、デバッグに便利。

```yml
logging.level:
  io.awspring.cloud.paramstore.AwsParamStorePropertySource: debug
```


#### 取得対象のParameterStoreのパラメータと Spring のプロパティとの関係

ParameterStore からパラメータ名の接頭辞が以下のものを取得し、
パラメータ名から接頭辞を取り除き、"/" を "." に変換したものがプロパティ名として採用される。

- `${aws.paramstore.prefix}/${aws.paramstore.name}${aws.paramstore.profileSeparator}${プロファイル}/` ※アクティブプロファイル毎
- `${aws.paramstore.prefix}/${aws.paramstore.name}/`
- `${aws.paramstore.prefix}/${aws.paramstore.defaultContext}${aws.paramstore.profileSeparator}${プロファイル}/` ※アクティブプロファイル毎
- `${aws.paramstore.prefix}/${aws.paramstore.defaultContext}/`

上記は、それぞれ PropertySource として作成され、同一プロパティ名がある場合の優先順は、上記の記載順の通り。


#### application.yml との関係

application.yml の中で `${xxx}` を使用して、ParameterStore から読み取ったパラメータの参照も可能。


## CLI

### 前準備：

EC2上で操作する場合は、環境変数 AWS_DEFAULT_REGION を事前に設定しておくとよい。※ローカルPC上で操作する場合は、`aws configure` しておけばよい。

```sh
$ ZONE=$(ec2-metadata -z | cut -d ' ' -f 2)
$ export AWS_DEFAULT_REGION=${ZONE::-1}
```

### パラメータ設定

```
$ aws ssm put-parameter --name "/config/environment_dev/MY_FOO_BAR" --value "value123" --type String
$ aws ssm put-parameter --name "/config/environment_dev/MY_FOO_BAR2" --value "value456" --type String
```

### パラメータ取得

```
$ aws ssm get-parameters-by-path --path "/config/environment_dev" --recursive --with-decryption
```


### 環境変数化

参考: https://qiita.com/th_/items/8ffb28dd6d27779a6c9d

```sh
INSTANCE_ID=$(ec2-metadata -i | cut -d ' ' -f 2)
ZONE=$(ec2-metadata -z | cut -d ' ' -f 2)
REGION=${ZONE::-1}
APP_ENV=$(aws --region ${REGION} ec2 describe-instances --instance-ids ${INSTANCE_ID} --query "Reservations[0].Instances[0].Tags[?Key=='Env'].Value" --output text)

if [ -z $APP_ENV ]; then
  echo "No Env tag: instance_id=$INSTANCE_ID"
  exit 1
fi

CONTEXT=/config/environment_$APP_ENV
KEYVALUES=($(aws --region ${REGION} ssm get-parameters-by-path --path $CONTEXT --recursive --with-decryption --query "Parameters[*].[Name, Value]" --output text))

for (( i = 0; i < ${#KEYVALUES[*]}; i += 2 )); do
  KEY=$(echo ${KEYVALUES[$i]#$CONTEXT/} | sed 's/\(\/\|\.\)/_/g')
  VALUE=${KEYVALUES[$((i + 1))]}
  #echo debug:"export $KEY=$VALUE"
  export $KEY=$VALUE
done
```

ポイント：
- 上記スクリプトをファイルに保存し、AP起動用のスクリプトから sourceコマンドで取り込んで使用する想定。
- EC2の タグ Env に環境名が設定されていることを前提としている。
- パラメータ名 "/config/environment_環境名/" から始まるパラメータを環境変数化する仕様としている。
- パラメータ名から環境変数名変換時のルール
  - パラメータ名の先頭2階層分を削る。※spring-cloud-aws-parameter-store-config の仕様を考慮。
  - "." を "_" に変換する。※Springを使用した場合、環境変数名の "_"はプロパティ名に変換時に "."に戻される。
  - "/" は "_" に変換する。※spring-cloud-aws-parameter-store-config の仕様を考慮。
