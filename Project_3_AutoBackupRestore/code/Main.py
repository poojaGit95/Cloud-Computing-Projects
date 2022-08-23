import sys
import RestoreToLocal
import BackupToCloud

class Main():

    def __init__(self):
        self.userInput = sys.argv

    def userInputForOperationSelected(self):
        if self.userInput[1]=='backup' or self.userInput[1]=='restore':
            return self.userInput[1]
        else:
            print("Enter valid option - Backup or Restore")
            sys.exit(0)

    def userInputForCloudBucketName(self):
        cloudDirecPath = self.userInput[2]
        if cloudDirecPath[-1]!='/':
            cloudDirecPath = cloudDirecPath+'/'
        return cloudDirecPath

    def userInputForLocalDirectoryPath(self):
        localDirecPath = self.userInput[3]
        if localDirecPath[-1]=='/':
            localDirecPath = localDirecPath[:-1]
        return localDirecPath

execute = Main()
operationSelected = execute.userInputForOperationSelected()
cloudBucketDirectoryPath = execute.userInputForCloudBucketName()
localDirectoryPath = execute.userInputForLocalDirectoryPath()

if operationSelected=='backup':
    backupToCloud = BackupToCloud.BackupToCloudOpertion()
    filesAddedToCloud = backupToCloud.backUpFilesFromLocalToCloudS3(cloudBucketDirectoryPath, localDirectoryPath)
    if len(filesAddedToCloud)==0:
        print('No backup done, all files up to date')
    else:
        print('Files backed up to Cloud: ', filesAddedToCloud)
    sys.exit(0)

elif operationSelected=='restore':
    restoreToLocal = RestoreToLocal.RestoreToLocalOperation()
    filesRestoredToLocal = restoreToLocal.restoreFilesFromCloudS3ToLocal(cloudBucketDirectoryPath, localDirectoryPath)
    print('Files Restored to Local: ', filesRestoredToLocal)
    sys.exit(0)

else:
    print("Enter valid option - Backup or Restore")

