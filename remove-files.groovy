pipeline {
    agent any

    stages {
        stage('Delete Unnecessary Files') {
            steps {
                script {
                    // Prompt user for directory path
                    def userInput = input(
                        id: 'directoryInput',
                        message: 'Enter the directory path to clean:',
                        parameters: [
                            [$class: 'TextParameterDefinition', defaultValue: '', description: 'Directory Path', name: 'directoryPath']
                        ])

                    // Get directory path from user input
                    def directoryPath = userInput['directoryPath']

                    // Check if directory path is empty
                    if (directoryPath.isEmpty()) {
                        error("Directory path cannot be empty.")
                    }

                    // Logic to delete unnecessary files using auto-remove
                    sh "find ${directoryPath} -name '*.tmp' -auto-remove"
                    
                    echo "Unnecessary files deleted successfully in ${directoryPath}."
                }
            }
        }
    }
}
