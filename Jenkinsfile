pipeline {
  agent {
    docker {
      image 'openjdk:11'
    }

  }
  stages {
    stage('Build') {
      steps {
        sh './gradlew shadowJar'
      }
    }
    stage('Test') {
      steps {
        sh './gradlew check'
      }
    }
    stage('Publish') {
      steps {
        archiveArtifacts 'build/libs/*.jar'
      }
    }
  }
}