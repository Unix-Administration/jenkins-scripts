def userExists(username, fullName) {
    def userWithSameName = jenkins.model.Jenkins.instance.getUser(fullName)
    def existingUser = jenkins.model.Jenkins.instance.getUser(username)
    return userWithSameName != null || existingUser != null
}

node {
    stage('Create Jenkins Users') {
        def createNewUser = true

        while (createNewUser) {
            def username = ''
            def password = ''
            def fullName = ''

            while (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || userExists(username, fullName)) {
                stage('User Input') {
                    def userInput = input(
                        id: 'userInput',
                        message: 'Introduce a username, a password, and a full name, or press "q" to finish:',
                        parameters: [
                            [$class: 'TextParameterDefinition', defaultValue: '', description: 'Insert a username', name: 'username'],
                            [$class: 'TextParameterDefinition', defaultValue: '', description: 'Insert a password', name: 'password'],
                            [$class: 'TextParameterDefinition', defaultValue: '', description: 'Insert a full name', name: 'fullName']
                        ])

                    username = userInput['username']
                    password = userInput['password']
                    fullName = userInput['fullName']

                    if (username.isEmpty() || password.isEmpty() || fullName.isEmpty()) {
                        echo "Username, password, and full name are required fields and cannot be empty."
                    } else if (userExists(username, fullName)) {
                        echo "User with the same username or full name already exists. Please choose a different username and full name."
                    }
                }
            }

            def instance = jenkins.model.Jenkins.instance
            def hudsonRealm = instance.getSecurityRealm()

            def newUser = hudsonRealm.createAccount(username, password, fullName)
            newUser.save()

            echo "Jenkins user '${username}' with full name '${fullName}' created successfully."
            echo "Press 'q' to finish the process."
        }
    }
}

