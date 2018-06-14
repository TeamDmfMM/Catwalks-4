pipeline {
  agent any
  stages {
    stage('Setup CI Environment') {
      steps {
        sh './gradlew setupCIWorkspace'
      }
    }
    stage('Build Project') {
      steps {
        sh './gradlew.bat build'
      }
    }
  }
}