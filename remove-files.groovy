pipeline {
    agent any
    
    stages {
        stage('Autoremove') {
            steps {
                script{
                    def password = 'Admin123'
                    def comand = "echo '${password}'| sudo -S apt-get autoremove -y"
                    sh comand
                }
            }
        }
    }
}
