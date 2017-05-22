pipeline {
    agent any

    stages {
        stage ('build') {
            steps {
                withMaven (mavenLocalRepo: ".repository") {
                    sh 'mvn clean install'
                }
            }
        }

        stage ('archive') {
            steps {
                archive '**/target/**/*'
                junit '*/target/surefire-reports/*.xml'
            }
        }
    }
}