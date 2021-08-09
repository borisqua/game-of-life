/** Jenkins declarative pipeline */
pipeline {
//     agent any
    agent {
//         docker { image 'maven:3.3.3' }
        label '!windows'
    }

    environment {
        DISABLE_AUTH='true'
        DB_ENGINE='sqlite'
    }

    stages {
        stage('Build') {
            steps {
                sh '''
                echo "Building app..."
                echo "Database engine is ${DB_ENGINE}"
                '''

//                 sh './gradlew clean build'
            }
        }
        stage('Starting') {
            steps {
                sh '''
                echo "Starting app..."
                echo "DISABLE_AUTH = ${DISABLE_AUTH}"
                '''
                sh 'echo env'
//                 java -jar app/build/libs/app.jar
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
/** Jenkinsfile (Scripted Pipeline) advanced*/
/*
node {
    try {
        stage('Test') {
            sh 'echo "Fail!"; exit 1'
        }
        echo 'This will run only if successful'
    } catch (e) {
        echo 'This will run only if failed'

        // Since we're catching the exception in order to report on it,
        // we need to re-throw it, to ensure that the build is marked as failed
        throw e
    } finally {
        def currentResult = currentBuild.result ?: 'SUCCESS'
        if (currentResult == 'UNSTABLE') {
            echo 'This will run only if the run was marked as unstable'
        }

        def previousResult = currentBuild.previousBuild?.result
        if (previousResult != null && previousResult != currentResult) {
            echo 'This will run only if the state of the Pipeline has changed'
            echo 'For example, if the Pipeline was previously failing but is now successful'
        }

        echo 'This will always run'
    }
} */
