pipeline {

    agent {
        docker {
            image 'maven:3.6.3'
            args '-v /root/.m2:/root/.m2'
        }
    }

    stages {

        stage('Maven Test') {
            steps {
                echo 'Testing maven project'
                sh 'mvn -B clean test -Dspring.profiles.active=test'
                echo 'Testing finished'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Maven Build') {
            steps {
                sh 'mvn -B -DskipTests install'
                sh 'ls -al .'
            }
            post {
                success {
                    archiveArtifacts 'target/*.jar'
                }
            }
        }

        stage('Docker Build') {
            when { 
                branch pattern: '(master)|((task|feature)/\\w+)', comparator: 'REGEXP' 
            }
            steps {
                echo('Building Docker Image')
                sh 'ls -al . && cat ./Dockerfile'
                sh 'docker build .'
            }
        }

        stage('Docker Deploy') {
            when { 
                branch pattern: 'master', comparator: 'EQUALS' 
            }
            steps {
                echo('Build Docker Image')
            }
        }

        stage('Deploy to Production') {
            when { 
                branch pattern: 'master', comparator: 'EQUALS' 
            }
            steps {
                echo('Deploying the production ready application to production')
            }
        }

        stage('Deploy to Development') {
            when { 
                branch pattern: '(task|feature)/\\w+', comparator: 'REGEXP' 
            }
            steps {
                echo('Deploying the application to development environment')
            }
        }
    }
}