/*
    def PACKAGENAME = '''

       version=$(git rev-parse --short HEAD)
       PNAME=$(echo $JOB_NAME | tr / . | tr "[:upper:]" "[:lower:]")
       PACKAGENAME=${PNAME%.*}
       echo $PACKAGENAME
                   '''

       def proc = ['bash', '-c', PACKAGENAME].execute()
       proc.waitFor()
    println proc.text
       
     
def Version = sh ' git rev-parse --short HEAD)'
Version()
   */


def call(Map config) {
 
 node ('master'){
  try{
         
 stage('Checkout'){
 checkout scm  
      }

stage('Build'){

    echo 'building'
    def z = version.vshort()
    z.executeCommand(this)
    
// executeShellCommand(command)
   
    sh 'npm install'
// def artifactname = libraryResource 'dockerImageName.sh'
 //sh artifactname

 
}
stage('Test'){ 
    echo 'version' 
 }
 
stage('Publish') { 
def request = libraryResource 'dockerPush.sh'
 sh request
 }
stage('PostAction') {
   echo "Cleaning WorkSpace"
   deleteDir()
    echo "Sending Email"

    
  }
   echo "Success"
   return true
   
  
  }
  catch (err){
      echo "Failed"
   throw err
   }

  
  
}
 }


