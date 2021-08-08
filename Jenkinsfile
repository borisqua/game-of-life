pipeline {
    agent any
    //{ docker { image 'maven:3.3.3' } }
    stages {
        stage('Build') {
            steps {
                echo "Building app..."
                sh './gradlew clean build'
                sh '''
                echo "Starting app..."
                java -jar app/build/libs/app.jar
                '''
            }
        }
    }
}