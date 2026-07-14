pipeline {
    agent any

    environment {
        IMAGE_NAME = "aariskazi/synapse-ai"
        CONTAINER_NAME = "synapse-ai"
        PORT = "8080"
        IMAGE_TAG = "v${BUILD_NUMBER}"
        MACHINE_IP="192.168.0.114"
    }

    stages {

        stage('Clean Workspace') {
            steps {
                deleteDir()
            }
        }

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Aaris-Kazi/Synapse-AI.git'
            }
        }

        stage('Debug') {
            steps {
                sh 'ls -la'
                sh 'find . -name pom.xml'
            }
        }

        stage('Build JAR') {
            steps {
                sh '''
                set -a
                . /home/warhero/.env
                set +a
                mvn clean package
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest
                '''
            }
        }

        stage('Stop Old Container') {
            steps {
                sh 'docker stop $CONTAINER_NAME || true'
                sh 'docker rm $CONTAINER_NAME || true'
            }
        }

        stage('Run Container') {
            steps {
                sh '''
                docker run -d \
                --env-file /home/warhero/.env \
                -p 8088:$PORT \
                --name $CONTAINER_NAME \
                $IMAGE_NAME:$IMAGE_TAG
                '''
            }
        }
    }
}