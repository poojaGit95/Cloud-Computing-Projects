
Auto Backup Restore

Create an application which recursively traverses the files of a directory and makes a backup to 
the cloud.  The program should also be able to restore from the cloud as well.  You will use the 
cloud storage APIs to do this. 
 
Examples: 
% backup directory-name bucket-name::directory-name 
 This will make a backup to the cloud of the specified directory to the specified “bucket” 
in either Azure of AWS.  The directory structure of the files should be respected and visible in 
the cloud.  If the bucket does not exist please create it. 
 
% restore bucket-name::directory-name directory-name 
 This will restore from the specified bucket-name in the cloud to the specified directory. 
The directory structure of the files should be respected.    
          

      


