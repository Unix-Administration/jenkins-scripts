pipeline {
    agent any
    
    parameters {
        string(defaultValue: 'user1', description: 'User directory name', name: 'USERNAME')
    }
    
    stages {
        stage('Edit HTML') {
            steps {
                script {
                    // User's directory
                    def userDir = "/var/www/${params.USERNAME}"
                    // HTML file path
                    def htmlFile = "${userDir}/index.html"
                    
                    // Check if the directory exists
                    if (!fileExists(userDir)) {
                        error "User directory does not exist: ${userDir}"
                    }
                    
                    // Edit HTML file
                    sh "nano ${htmlFile}"
                }
            }
        }
        
        stage('Save Changes') {
            steps {
                script {
                    // Restart Apache if HTML file was modified
                    if (env.CHANGES_EXIST == 'true') {
                        sh "systemctl restart apache2"
                    } else {
                        echo "No changes were made to the HTML file."
                    }
                }
            }
        }
    }
    
    post {
        always {
            // Cleanup
            cleanWs()
        }
    }
}

// Function to check if a file exists
def fileExists(filePath) {
    return file(filePath).exists()
}
