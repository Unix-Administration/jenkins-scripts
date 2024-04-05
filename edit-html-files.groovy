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
                    def userDir = "/var/www/html/${params.USERNAME}"
                    // HTML file path
                    def htmlFile = "${userDir}/josue.html"
                    
                    // Check if the directory exists
                    if (!fileExists(userDir)) {
                        error "User directory does not exist: ${userDir}"
                    }
                    
                    // Use "Text File Operations" plugin to edit HTML file
                    // This requires the "Text File Operations" Jenkins plugin
                    textFile = readFile file: htmlFile
                    editedContent = textFile.replace('original_content', 'new_content')
                    writeFile file: htmlFile, text: editedContent
                    
                    // Mark that changes were made for the next stage
                    env.CHANGES_EXIST = 'true'
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
