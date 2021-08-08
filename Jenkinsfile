pipeline {
    agent any
    //{ docker { image 'maven:3.3.3' } }
    stages {
        stage('Build') {
            steps {
                sh 'echo "Hello from Jenkins"'
                sh '''
                echo "Multiline shell steps work too"
                ls -lah
                '''
            }
        }
    }
}