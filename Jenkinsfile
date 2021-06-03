pipeline {
    agent {
        docker {
            image 'gradle:jdk16-hotspot'
            args '-v /root/.m2:/root/.m2'
        }
    }
    stages {
        stage('Build') {
            steps {
                    sh 'gradle clean build'
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'build/libs/*.jar', fingerprint: true
        }
    }
}