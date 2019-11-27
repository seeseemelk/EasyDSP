pipeline {
  agent any
  stages {
    stage('Build') {
      agent {
        docker {
          image 'openjdk:11'
        }

      }
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