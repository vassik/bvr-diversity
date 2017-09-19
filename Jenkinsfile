pipeline {
    agent {
        docker { image 'maven' }
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