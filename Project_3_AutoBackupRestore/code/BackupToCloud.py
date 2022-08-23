import sys
import boto3
from datetime import datetime
import os

class BackupToCloudOpertion():

    def __init__(self):
        self.session = boto3.Session()
        self.s3 = self.session.resource('s3')
        self.awsRegion = self.session.region_name


    def createBucketInCloudS3(self, cloudBucketDirectoryPath):
        allBucketsList = []
        bucketName = cloudBucketDirectoryPath.split('/')[0]
        for bucket in self.s3.buckets.all():
            allBucketsList.append(bucket.name)
        if bucketName not in allBucketsList:
            print("creating bucket: ", bucketName)
            try:
                self.s3.create_bucket(Bucket=bucketName, CreateBucketConfiguration={"LocationConstraint": self.awsRegion})
            except:
                print('Error occurred')
                print(sys.exc_info()[0])
                sys.exit(0)

    def getLocalFilesAndPathMap(self, localDirectoryPath):
        localDirectoryName = localDirectoryPath.split('/')[-1]
        fileNamePathMap = {}
        for root, dirs, files in os.walk(localDirectoryPath):
            for file in files:
                fullPath = os.path.join(root, file)
                idx = fullPath.index(localDirectoryName)
                subPath = fullPath[idx:]
                fileNamePathMap[file] = [fullPath, subPath]
        return fileNamePathMap


    def getCurBucketFilesAndTimeMap(self, cloudBucketDirectoryPath):
        bucketFileTimeMap = {}
        bucketName = cloudBucketDirectoryPath.split('/')[0]
        curBucket = self.s3.Bucket(bucketName)
        idx = cloudBucketDirectoryPath.index('/')
        prefix = cloudBucketDirectoryPath[idx+1:]
        for curObject in curBucket.objects.filter(Prefix=prefix):
            fileUploadUTCTime = curObject.last_modified.strftime("%Y-%m-%d %H:%M:%S")
            curFile = curObject.key.split('/')[-1]
            if curFile == '':
                curFile = 'Directory'
            bucketFileTimeMap[curFile] = fileUploadUTCTime
        return bucketFileTimeMap


    def getCurDirectoryFilesAndTimeMap(self, localDirectoryPath):
        localDirectoryName = localDirectoryPath.split('/')[-1]
        localFilesTimeMap = {}
        for root, dirs, files in os.walk(localDirectoryPath):
            for file in files:
                fullPath = os.path.join(root, file)
                fileModifiedUnixTime = os.path.getmtime(fullPath)
                fileModifiedUTCTime = datetime.utcfromtimestamp(fileModifiedUnixTime).strftime("%Y-%m-%d %H:%M:%S")
                localFilesTimeMap[file] = fileModifiedUTCTime
        return localFilesTimeMap

    def getLocalModifiedFilesList(self, localDirectoryPath, cloudBucketDirectoryPath):
        modifiedLocalFiles = []
        localFilesTimeMap = self.getCurDirectoryFilesAndTimeMap(localDirectoryPath)
        bucketFileTimeMap = self.getCurBucketFilesAndTimeMap(cloudBucketDirectoryPath)
        for file in localFilesTimeMap.keys():
            if file in bucketFileTimeMap.keys():
                if localFilesTimeMap[file] > bucketFileTimeMap[file]:
                    modifiedLocalFiles.append(file)
            else:
                modifiedLocalFiles.append(file)
        return modifiedLocalFiles

    def backUpFilesFromLocalToCloudS3(self, cloudBucketDirectoryPath, localDirectoryPath):
        self.createBucketInCloudS3(cloudBucketDirectoryPath)
        modifiedFileList = self.getLocalModifiedFilesList(localDirectoryPath, cloudBucketDirectoryPath)
        localFilesPathMap = self.getLocalFilesAndPathMap(localDirectoryPath)
        bucketName = cloudBucketDirectoryPath.split('/')[0]
        idx = cloudBucketDirectoryPath.index('/')
        prefix = cloudBucketDirectoryPath[idx+1:]
        filesAddedToCloud = []
        for file in modifiedFileList:
            self.s3.Object(bucketName, prefix+localFilesPathMap[file][1]).put(
                Body=open(localFilesPathMap[file][0], "rb"))
            filesAddedToCloud.append(file)
        return filesAddedToCloud
