pipeline {
    agent any
    stages {
        stage('Ejecutar comando') {
            steps {
                script {
                    sh 'wget -qO- http://www.dev-huesos.com/josue-site > /dev/null'
    
                    def webpageContent = sh(script: 'wget -qO- http://www.dev-huesos.com/josue-site', returnStdout: true).trim()

                    webpageContent = webpageContent.replaceAll('<title>Mostrar Usuario</title>', '<title>Nuevo Título</title>')

                    sh "echo '${webpageContent}' > /var/www/josue-site/josue.html"
                }
            }
        }
    }
}
