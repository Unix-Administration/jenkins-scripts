pipeline {
    agent any

    stages {
        stage('Delete Unnecessary Files') {
            steps {
                script {
                    // Especifica la ruta del directorio que deseas limpiar
                    def directoryPath = '/ruta/al/directorio'

                    // Lógica para eliminar archivos innecesarios utilizando auto-remove
                    sh "find ${directoryPath} -name '*.tmp' -auto-remove"
                    
                    echo "Archivos innecesarios eliminados exitosamente en ${directoryPath}."
                }
            }
        }
    }
}
