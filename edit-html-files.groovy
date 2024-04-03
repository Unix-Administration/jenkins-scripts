pipeline {
    agent any

    stages {
        stage('Edit HTML') {
            steps {
                script {
                    // Prompt user for HTML content
                    def userInput = input(
                        message: 'Enter HTML content:',
                        parameters: [string(defaultValue: '', description: 'HTML content', name: 'htmlContent')]
                    )
                    
                    // Write HTML content to file
                    writeFile file: '/var/www/user1/index.html', text: userInput

                    // Restart Apache
                    sh 'sudo systemctl restart apache2'
                }
            }
        }
    }
}
