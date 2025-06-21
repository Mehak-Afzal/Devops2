pipeline {
    agent any

    stages {
        stage('Clean Old Containers') {
            steps {
                sh '''
                docker rm -f lamp-db || true
                docker rm -f lamp-web || true
                docker rm -f selenium-tests || true
                docker-compose down || true
                '''
            }
        }

        stage('Clone Repo') {
            steps {
                git 'https://github.com/Mehak-Afzal/Devops2.git'
            }
        }

        stage('Build and Run Containers') {
            steps {
                sh 'docker-compose -p guestbook-ci -f docker-compose.yml up -d --build'
                sh 'sleep 10' // Let containers get ready
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

    post {
        always {
            echo 'Cleaning up containers after pipeline...'
            sh '''
            docker-compose -p guestbook-ci -f docker-compose.yml down || true
            docker rm -f lamp-db lamp-web selenium-tests || true
            '''
        }
    }
}
