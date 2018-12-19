
import groovy.json.*
 
def dockerfile = readfile libraryResource 'DOCKERFILE'
def version = string "git rev-parse --short HEAD"

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
executeShellCommand(version)
    sh 'npm install'
//def builddocker = libraryResource 'dockerBuild.sh'
 //sh builddocker
 
 
}
stage('Test'){ 
    echo '$version' 
 }
 
stage('Publish') { 
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

