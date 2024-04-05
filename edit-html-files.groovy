pipeline {
	agent any
	parameters {
  	base64File description: 'Please add the index.html', name: 'website'
	}
    
	environment {
    	def numberOfPage = "value"
	}
	stages {
 	stage('Stage1') {
    	steps {
        	script {
            	def asd = ""
            	userInput = input(
               		id: 'userInput',
               		message: 'Introduce el contenido de la pagina',
               		parameters: [
               			[$class: 'TextParameterDefinition', defaultValue: '', description: '-', name: '-'],
                   		[$class: 'TextParameterDefinition', defaultValue: '', description: 'Insert el numero de la pagina', name: 'number']
               		]
       		)
       		numberOfPage = userInput['number']
            	echo "El numero de la pagina es:  '${numberOfPage}'"
        	}
        	withFileParameter('website') {
              	sh 'cd /var/www/html/josue-site' + numberOfPage + ' && cat $website > josue.html '
       		  echo 'Archivo creado exitosamente'
            	}
    	}
 	}   
	}
}
 
