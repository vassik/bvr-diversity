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
}