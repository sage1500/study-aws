#!/bin/bash

##
## レイヤー作成
##

. $(dirname $0)/0-env.sh
. $(dirname $0)/define.sh

## ビルド
gradle -q build

## zip を s3バケットにコピー
aws s3 cp build/distributions/example-libs.zip s3://$ARTIFACT_BUCKET

## レイヤー作成
create_layer $LAYER_NAME $LAYER_ARTIFACT
