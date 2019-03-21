pipeline {
    agent {
        node { label 'linux&&docker' }
    }

    stages {
        stage ('Build with JDK11') {
            steps {
                //DXA has to be able to be built on JDK11:
                withDockerContainer("maven:3.6-jdk-11-slim") { 
                    //DXA has to be able to be build without SDL proprietary dependencies:
                    sh "dependency:purge-local-repository"

                    sh "mvn clean verify"
                }
            }
        }
        
        stage ('Build a branch') {
            when { not { branch 'develop' } }
            // Not on the develop branch, so build it, but do not install it.
            steps {
                //Build on JDK8:
                withDockerContainer("maven:3.6-jdk-8-alpine") { sh "mvn clean verify"}
            }
        }

        stage ('Build and deploy from develop') {
            when { branch 'develop' }
            steps {
                //Build on JDK8 and deploy it to local repository:
                withCredentials([file(credentialsId: 'dxa-maven-settings', variable: 'MAVEN_SETTINGS_PATH')]) {
                    withDockerContainer('maven:3.6-jdk-8-alpine') {
                        sh "mvn -s $MAVEN_SETTINGS_PATH -Plocal-repository clean deploy"
                    }
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
    }
}
