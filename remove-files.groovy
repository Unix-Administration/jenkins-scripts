pipeline {
    agent any

    stages {
        stage('Delete All Files in Directory') {
            steps {
                script {
                    def userInput = input(
                        id: 'directoryInput',
                        message: 'Enter the directory path to clean:',
                        parameters: [
                            [$class: 'TextParameterDefinition', defaultValue: '', description: 'Directory Path', name: 'directoryPath']
                        ])

                    def directoryPath = userInput['directoryPath']

                    if (directoryPath.isEmpty()) {
                        error("Directory path cannot be empty.")
                    }

                    sh "rm -rf ${directoryPath}/*"

                    echo "All files inside ${directoryPath} have been deleted successfully."
                }
            }
        }
    }
}
