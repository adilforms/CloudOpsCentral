
import groovy.json.*
 
def dockerfile = readfile libraryResource 'DOCKERFILE'
def version = sh( script: '''git rev-parse --short HEAD
                 PNAME=$(echo $JOB_NAME | tr / . | tr "[:upper:]" "[:lower:]")
                 PACKAGENAME=${PNAME%.*}''', returnStdout: true).toString().trim()
                 
                 echo 'PACKAGENAME'.'version'
                 return 'PACKAGENAME'.'version'



def call(Map config) {

 node ('master'){
  //deleteDir()
  try{
  
stage('Checkout'){
 checkout scm  // In this Step Jenkins will get the Git Url and Branch name from the job.
  //def dockerfile = libraryResource 'dockerfilepull.sh' // Reading Docker function to Copy Docker file.
 //sh  dockerfile  
 sh ' echo $dockerfile >> Dockerfile'
 
}
stage('Build'){
 
    echo 'building'

 
    sh 'npm install'
//def builddocker = libraryResource 'dockerBuild.sh'
 //sh builddocker
 
 
}
stage('Test'){ 

     
def version = sh( script: '''git rev-parse --short HEAD
                 PNAME=$(echo $JOB_NAME | tr / . | tr "[:upper:]" "[:lower:]")
                 PACKAGENAME=${PNAME%.*}''', returnStdout: true).toString().trim()
                 
                 echo version
                 return version
 }
 
stage('Publish') { 
 echo '----'
def request = libraryResource 'dockerPush.sh'
 sh request
 }
stage('PostAction') {
 echo "Email"
   //echo "Cleaning WorkSpace"
   deleteDir()  
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

