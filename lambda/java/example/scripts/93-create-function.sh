#!/bin/bash

. $(dirname $0)/0-env.sh
. $(dirname $0)/define.sh

## レイヤーのバージョンARN取得
LAYER_VERSION_ARN=$(get_layer_version_arn $LAYER_NAME)
echo "Layer version arn: $LAYER_VERSION_ARN"

## ビルド
gradle -q build

## zip を s3バケットにコピー
aws s3 cp build/distributions/example.zip s3://$ARTIFACT_BUCKET

## 関数一括更新
update_functions $FUNCTIONS_ARTIFACT ${FUNCTIONS[@]}
