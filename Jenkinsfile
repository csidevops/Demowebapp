pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout([$class: 'GitSCM',
                          branches: [[name: '*/main']],
                          userRemoteConfigs: [[url: 'https://github.com/csidevops/Demowebapp.git']]])
            }
        }
        stage('Build with Maven') {
            steps {
                bat "mvn clean package"
            }
        }
        stage('SonarQube Analysis') {
            steps {
                // Run SonarQube Scanner to analyze the code and send results to SonarCloud
                bat "mvn sonar:sonar -Dsonar.projectKey=DW1 -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=4b874b261540d629213e98fb8946d784e2ddefb3"
            }
        }
        stage('Publish to Artifactory') {
            steps {
                script {
                    def server = Artifactory.server 'JFrog'         
                    def targetPath = "demorepo-generic-local/Demowebapp/${env.BUILD_NUMBER}/"
                    def localPath = "target/*.war"
                    
                    // Upload the WAR file to Artifactory
                    server.upload spec: """{
                        "files": [
                            {
                                "pattern": "${localPath}",
                                "target": "${targetPath}"
                            }
                        ]
                    }"""
                }
            }
        }
        stage('Deploy to Tomcat') {
            steps {
                deploy adapters: [
                    // Replace 'your-credentials-id' and 'your-tomcat-url' with actual values
                    tomcat7(credentialsId: 'your-credentials-id', url: 'http://your-tomcat-url:8080')
                ], contextPath: 'your-app-context-path', war: '**/*.war'
            }
        }
    }
}
