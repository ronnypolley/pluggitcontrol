node {
    stage ('Build') {
        withMaven(jdk: "SunJDK8", maven: "Maven 3.3.9", mavenLocalRepo: "$WORKSPACE/.repository"){
            sh 'mvn clean install'
        }
    }
}