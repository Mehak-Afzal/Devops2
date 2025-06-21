pipeline {
    agent any

    stages {
        stage('Clone Repo') {
            steps {
                git 'https://github.com/Mehak-Afzal/Devops2.git'
            }
        }

        stage('Build and Run Containers') {
            steps {
                // Build the PHP app and DB services using docker-compose
                sh 'docker-compose -p guestbook-ci -f docker-compose.yml up -d --build'
                sh 'sleep 10' // Give containers some time to be ready
            }
        }

        stage('Run Selenium Tests (Java)') {
            steps {
                dir('selenium-tests') {
                    sh '''
                    docker build -t guestbook-selenium-tests -f Dockerfile .
                    docker run --rm --network host guestbook-selenium-tests mvn test
                    '''
                }
            }
        }
    }
}
