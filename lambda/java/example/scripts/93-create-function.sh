#!/bin/bash

##
## 関数作成
##

. $(dirname $0)/0-env.sh

#ARTIFACT_BUCKET=$(cat bucket-name.txt)
#LAYER_ARN=$(cat layer-arn.txt)

## zip を s3バケットにコピー
aws s3 cp build/distributions/example.zip s3://$ARTIFACT_BUCKET

#create_function () {
#	aws lambda create-function \
#		--function-name $1 \
#		--handler $2 \
#		--runtime java11 \
#		--role arn:aws:iam::${AWS_ACCOUNT_ID}:role/${ROLE_NAME} \
#		--timeout 15 \
#		--memory-size 256 \
#		--layers $LAYER_ARN \
#		--code S3Bucket=${ARTIFACT_BUCKET},S3Key=example.zip
#}
#
#create_function example-hello example.Hello
#create_function example-hello2 example.Hello2
#create_function example-hello3 example.Hello3::hello3
#create_function example-hello4 example.Hello4::hello4

update_functions example.zip \
	example-hello  example.Hello \
	example-hello2 example.Hello2 \
	example-hello3 example.Hello3::hello3 \
	example-hello4 example.Hello4::hello4

