pipeline {
  agent {
    docker {
      image 'gradle:jdk16-hotspot'
    }
  }
  stages {
    stage('Build') {
      steps {
        sh 'gradle clean build'
      }

  }
  post {
    always {
      archiveArtifacts(artifacts: 'build/libs/*.jar', fingerprint: true)
    }

  }
}