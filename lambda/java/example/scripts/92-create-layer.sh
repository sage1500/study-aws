#!/bin/bash

##
## レイヤー作成
##

. $(dirname $0)/0-env.sh

#ARTIFACT_BUCKET=$(cat bucket-name.txt)

## zip を s3バケットにコピー
aws s3 cp build/distributions/example-libs.zip s3://$ARTIFACT_BUCKET

## レイヤー作成
create_layer example-libs example-libs.zip
#aws lambda publish-layer-version \
#	--layer-name example-libs \
#	--content S3Bucket=${ARTIFACT_BUCKET},S3Key=example-libs.zip \
#	--compatible-runtimes java11

## レイヤーのバージョンARN取得
#LAYER_VERSION_ARN=$(aws lambda list-layers --query "Layers[?LayerArn=='arn:aws:lambda:ap-northeast-1:${AWS_ACCOUNT_ID}:layer:example-libs'].LatestMatchingVersion.LayerVersionArn|[0]" --output text)
LAYER_VERSION_ARN=$(get_layer_version_arn example-libs)
echo $LAYER_VERSION_ARN > layer-version-arn.txt
