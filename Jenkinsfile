pipeline {
    agent { label 'docker' }

    options {
        timestamps()
        skipDefaultCheckout(true)
        disableConcurrentBuilds()
    }

    triggers {
        pollSCM('H/5 * * * *')
    }

    environment {
        IMAGE_NAME = 'mambetali-aruzhan-student-api'
        IMAGE_TAG = 'v1.0-mambetali-aruzhan'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo 'Student: Mambetali Aruzhan Imanaliyevna'
                sh 'git log -1 --oneline'
            }
        }
        stage('Build') {
            steps {
                sh 'chmod +x ./gradlew && ./gradlew --no-daemon clean classes'
            }
        }
        stage('Test') {
            steps {
                sh './gradlew --no-daemon test'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'build/test-results/test/*.xml'
                }
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build -t "$IMAGE_NAME:$IMAGE_TAG" .'
            }
        }
        stage('Verify') {
            steps {
                sh 'docker image inspect "$IMAGE_NAME:$IMAGE_TAG" --format "Image ID: {{.Id}} | Size: {{.Size}} bytes"'
                echo 'Image verification completed for Mambetali Aruzhan'
            }
        }
    }

    post {
        success {
            echo 'SUCCESS: Mambetali Aruzhan Student API was built and verified.'
        }
        failure {
            echo 'FAILED: Check the failing stage and test results in Jenkins Console Output.'
        }
    }
}
