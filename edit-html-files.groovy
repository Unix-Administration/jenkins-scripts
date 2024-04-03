pipeline {
    agent any
    
    parameters {
        string(name: 'USERNAME', defaultValue: '', description: 'Enter your username')
    }

    stages {
        stage('Edit HTML File') {
            steps {
                script {
                    // Define paths
                    def originalFilePath = "/var/www/${params.USERNAME}/index.html"
                    def tempFilePath = "/tmp/index.html"
                    
                    // Copy HTML file to temporary location
                    sh "cp ${originalFilePath} ${tempFilePath}"
                    
                    // Open HTML file for editing (you may need to replace 'nano' with your preferred text editor)
                    sh "nano ${tempFilePath}"
                    
                    // Copy edited HTML file back to original location
                    sh "cp ${tempFilePath} ${originalFilePath}"
                }
            }
        }
        stage('Restart Apache') {
            steps {
                script {
                    // Restart Apache
                    sh "sudo systemctl restart apache2"
                }
            }
        }
    }
}

