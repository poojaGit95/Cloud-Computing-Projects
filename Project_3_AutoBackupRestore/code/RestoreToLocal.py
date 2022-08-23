import os
import errno
import sys
import boto3


class RestoreToLocalOperation():

    def __init__(self):
        self.session = boto3.Session()
        self.s3 = self.session.resource('s3')

    def restoreFilesFromCloudS3ToLocal(self, cloudBucketDirectoryPath, localDirectoryPath):
        bucketName = cloudBucketDirectoryPath.split('/')[0]
        idx = cloudBucketDirectoryPath.index('/')
        prefix = cloudBucketDirectoryPath[idx + 1:]
        if localDirectoryPath[-1] != '/':
            localDirectoryPath = localDirectoryPath + '/'
        filesRestoredToLocal = []

        try:
            curBucket = self.s3.Bucket(bucketName)
            for curObject in curBucket.objects.filter(Prefix=prefix):
                path, filename = os.path.split(curObject.key)
                try:
                    if not os.path.exists(localDirectoryPath+path):
                        os.makedirs(localDirectoryPath+path)
                        if not os.path.isdir(localDirectoryPath+curObject.key):
                            curBucket.download_file(curObject.key, localDirectoryPath+curObject.key)
                            filesRestoredToLocal.append(curObject.key)
                    else:
                        if not os.path.isdir(localDirectoryPath + curObject.key):
                            curBucket.download_file(curObject.key, localDirectoryPath + curObject.key)
                            filesRestoredToLocal.append(curObject.key)
                except OSError as exc:
                    if exc.errno != errno.EEXIST:
                        raise
        except:
            print('Error occurred')
            print(sys.exc_info()[0])
            sys.exit(0)

        return filesRestoredToLocal

