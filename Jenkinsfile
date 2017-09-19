pipeline {
    agent {
        docker { image 'ubuntu' }
    }
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean install'
            }
        }
    }

    post {
      always {
        junit '**/target/surefire-reports/*.xml'
      }
   }
}