pipeline {
  agent any
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