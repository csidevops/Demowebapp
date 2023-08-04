pipeline {
    agent any

    environment {
        SONAR_ORGANIZATION = 'csidevops'
        TOMCAT_USER = 'manager'
        TOMCAT_PASS = 'manager'
        TOMCAT_URL = "http://localhost:8090/manager/text"        
    }
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
                bat "mvn sonar:sonar -Dsonar.projectKey=DW1 -Dsonar.organization=${env.SONAR_ORGANIZATION} -Dsonar.host.url=https://sonarcloud.io -Dsonar.login=4b874b261540d629213e98fb8946d784e2ddefb3"
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
        stage('Deploy') {
            steps {
                bat '''
                    set WAR_FILE=\target\Demowebapp.war                    
                    curl -v -T %WAR_FILE% %TOMCAT_URL%/deploy?path=/Demowebapp -u %TOMCAT_USER%:%TOMCAT_PASS%
                '''
            }
        }
        }
    }

    post {
        always {
            // Clean up the workspace after the build
            cleanWs()
        }
    }
}
