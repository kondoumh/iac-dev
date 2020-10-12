echo $AWS_DEFAULT_REGION
echo $AWS_BUCKET_NAME
echo $AWS_BUCKET_BACKUP_PATH

DUMP_FILE_NAME="backupOn`date +%Y-%m-%d-%H-%M`.txt"
echo hoge > /tmp/$DUMP_FILE_NAME

aws s3 cp /tmp/$DUMP_FILE_NAME s3://$AWS_BUCKET_NAME/$AWS_BUCKET_BACKUP_PATH/$DUMP_FILE_NAME

if [ $? -ne 0 ]; then
  rm $DUMP_FILE_NAME
  echo "failed to upload to S3"
  exit 1
fi

echo 'Successfully upload to S3'

exit 0
