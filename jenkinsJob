pipeline {
    tools {
        maven "M3"
    }
    agent any
    stages {
        stage("Preparation") {
            steps {
                sh "cp -r /home/valen/Universidad/3curso/2cuatri/AIS/Practicas/AIS-Testing/src/ ./"
                sh "cp /home/valen/Universidad/3curso/2cuatri/AIS/Practicas/AIS-Testing/pom.xml ./"
            }
        }
        stage("Test") {
            steps {
                script {
                    sh "mvn test"
                }
            }
        }
    }
    post {
        always {
            junit "**/**/target/surefire-reports/TEST-*.xml"
        }
    }
}
