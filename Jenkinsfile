pipeline {
    agent any

    stages {
        stage ('build') {
            steps {
                withMaven (maven: 'Maven 3.3.9', jdk: 'SunJDK8', mavenLocalRepo: ".repository") {
                    sh 'mvn clean install'
                }
            }
        }

        stage ('archive') {
            steps {
                archive '**/target/**/*.jar'
                junit '*/target/surefire-reports/*.xml'
            }
        }

        stage ('sonar analysis') {
            steps {
                withSonarQubeEnv('Random Words') {
                    withMaven(maven: 'Maven 3.3.9', jdk: 'SunJDK8', mavenLocalRepo: ".repository"){
                        sh 'mvn sonar:sonar'
                    }
                }
            }
        }
    }
}