ROLE_NAME=lambda-ex

AWS_ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
ARTIFACT_BUCKET=$([ -f bucket-name.txt ] && cat bucket-name.txt)
LAYER_VERSION_ARN=$([ -f layer-version-arn.txt ] && cat layer-version-arn.txt)

## レイヤー作成
create_layer () {
	aws lambda publish-layer-version \
		--layer-name $1 \
		--content S3Bucket=${ARTIFACT_BUCKET},S3Key=$2 \
		--compatible-runtimes java11
}

## レイヤーバージョンARN取得
get_layer_version_arn () {
	aws lambda list-layers \
		--query "Layers[?LayerArn=='arn:aws:lambda:ap-northeast-1:${AWS_ACCOUNT_ID}:layer:$1'].LatestMatchingVersion.LayerVersionArn|[0]" \
		--output text
}

## 関数作成
create_function () {
	aws lambda create-function \
		--function-name $1 \
		--handler $2 \
		--runtime java11 \
		--role arn:aws:iam::${AWS_ACCOUNT_ID}:role/${ROLE_NAME} \
		--timeout 15 \
		--memory-size 256 \
		--layers $LAYER_VERSION_ARN \
		--code S3Bucket=${ARTIFACT_BUCKET},S3Key=$3
}

## 関数更新
update_function () {
	aws lambda update-function-code \
		--function-name $1 \
		--s3-bucket ${ARTIFACT_BUCKET} --s3-key $2
}

exists_function () {
	aws lambda get-function --function-name $1 >& /dev/null
}


## 関数一括更新
update_functions () {
	local s3key=$1
	shift
	
	while (($# > 1)); do
		if exists_function $1; then
			update_function $1 $s3key
		else
			create_function $1 $2 $s3key
		fi
		shift 2
	done
}

