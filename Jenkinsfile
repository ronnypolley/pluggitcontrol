pipeline {
    agent any

    tools {
        maven 'Maven 3.3.9'
        jdk 'SunJDK8'
    }

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
                archive '**/target/**/*'
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