pipeline {
    agent any
    //{ docker { image 'maven:3.3.3' } }
    stages {
        stage('Build') {
            steps {
                echo "Building app..."
//                 sh './gradlew clean build'
            }
        }
        stage('Starting') {
            steps {
                sh '''
                echo "Starting app..."
//                 java -jar app/build/libs/app.jar
                '''
            }
        }
    }
    post {
             always {
                 echo 'This will always run'
             }
             success {
                 echo 'This will run only if successful'
             }
             failure {
                 echo 'This will run only if failed'
             }
             unstable {
                 echo 'This will run only if the run was marked as unstable'
             }
             changed {
                 echo 'This will run only if the state of the Pipeline has changed'
                 echo 'For example, if the Pipeline was previously failing but is now successful'
             }
         }
}