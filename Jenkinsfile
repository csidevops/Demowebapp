pipeline {
    agent any

    environment {
        TOMCAT_USERNAME = 'csidevops'
        TOMCAT_PASSWORD = 'Eadevops#1234'
        TOMCAT_MANAGER_URL = "http://localhost:8081/manager/text"        
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
                script {
                    // Find the generated war file
                    def warFile = findFiles(glob: '**/target/*.war').first()
                    if (!warFile) {
                        error "No war file found in the target directory."
                    }

                    // Base64 encode the username and password for basic authentication
                    def credentials = "${env.TOMCAT_USERNAME}:${env.TOMCAT_PASSWORD}".bytes.encodeBase64().toString()

                    // Build the URL for the deployment
                    def deployUrl = "${env.TOMCAT_MANAGER_URL}/deploy?path=/myapp&update=true"

                    // Execute the PowerShell script to deploy the war file
                    def deployCommand = """
                        powershell -Command "Invoke-WebRequest -Uri '${deployUrl}' -Method PUT -InFile '${warFile}' -Headers @{Authorization='Basic ${credentials}'}"
                    """
                    def deployProcess = deployCommand.execute()
                    deployProcess.waitFor()

                    // Check the exit code of the process to see if the deployment was successful
                    if (deployProcess.exitValue() == 0) {
                        println "Deployment successful!"
                    } else {
                        println "Deployment failed!"
                        println "Error message: ${deployProcess.error.text}"
                        error "Deployment failed!"
                    }
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
