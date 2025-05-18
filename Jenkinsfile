pipeline {
    agent any

    stages {
        stage('Clone Repo') {
            steps {
                git 'https://github.com/Mehak-Afzal/Devops.git'
            }
        }

        stage('Build and Run with Docker') {
            steps {
                script {
                    sh 'docker-compose -p guestbook-ci -f docker-compose.yml up -d --build'
                }
            }
        }
    }
}
