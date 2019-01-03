def dockerImageName() {
  def imageName = sh(returnStdout: true, script: """#!/bin/bash -el
      version=\$(git rev-parse --short HEAD)
      PName=\$(echo $JOB_NAME | tr / . | tr "[:upper:]" "[:lower:]")
      PackageName=\${PName%.*}
      echo "$PackageName.$version:$BRANCH_NAME"
   """).trim()
}

def dockerBuild(imageName) {
  def build = sh(returnStdout: true, script: """#!/bin/bash -el
    echo "Building image..."
    docker build -t adilforms/${imageName} .
   """).trim()
}

def dockerPush(imageName) {
  def push = sh(returnStdout: true, script: """#!/bin/bash -el
    echo "Publishing..."
    docker push adilforms/${imageName}
   """).trim()
}
def dockerfile = readfile libraryResource 'DOCKERFILE'       //Loading Dockerfile into variable from resource



def call(Map config) {
  def imageName = ""
 
 node ('master'){              //Running Jenkins on Master Node
  deleteDir()                  //Clearing Workspace before Checking out new code
  //try{                         //Building Pipeline in try and Catch Block to catch errors
  
stage('Checkout'){
 checkout scm                 // In this Step Jenkins will get the Git Url and Branch name from the job.
 sh ' echo $dockerfile >> Dockerfile'       //Creating Dockerfile from the variable.
 
}
stage('Build'){
 
    echo 'building'
    sh 'npm install'                    
       
  imageName = dockerImageName()
  
        dockerBuild(imageName)
 
}
stage('Test'){ 
    echo 'version' //Executing Test 
 }
 
stage('Publish') { 
//def request = libraryResource 'dockerPush.sh' // In this step we are loading docker push function,
 //sh request //which will publish Docker Image to DockerHub
       def request = dockerPush(imageName)
        sh request
 }
stage('PostAction') {
   echo "Email" //Sending Email after Build
   
  }
   echo "Success"
   //return true
   
  
  //}
  //catch (err){
    //  echo "Failed"
   //throw err //this will throw error when something Breaks
   //echo "Sending Email"
  // }

  
  
}
 }

