def userExists(username) {
    def existingUser = jenkins.model.Jenkins.instance.getUser(username)
    return existingUser != null
}

node {
    stage('Create Jenkins Users') {
        def createNewUser = true

        while (createNewUser) {
            def username = ''
            def password = ''

            while (username.isEmpty() || password.isEmpty() || userExists(username)) {
                stage('User Input') {
                    def userInput = input(
                        id: 'userInput',
                        message: 'Introduce a username and a password',
                        parameters: [
                            [$class: 'TextParameterDefinition', defaultValue: '', description: 'Insert a username', name: 'username'],
                            [$class: 'TextParameterDefinition', defaultValue: '', description: 'Insert a password', name: 'password']
                        ])

                    username = userInput['username']
                    password = userInput['password']

                    if (username.isEmpty() || password.isEmpty()) {
                        echo "Username and password are required fields and cannot be empty."
                    } else if (userExists(username)) {
                        echo "User '${username}' already exists. Please choose a different username."
                    }
                }
            }

            def instance = jenkins.model.Jenkins.instance
            def hudsonRealm = instance.getSecurityRealm()

            def newUser = hudsonRealm.createAccount(username, password)
            newUser.save()

            echo "Jenkins user '${username}' created successfully."

            stage('More Users') {
                def moreUsersInput = input(
                    id: 'moreUsersInput',
                    message: 'Do you want to add more users?',
                    parameters: [
                        [$class: 'BooleanParameterDefinition', defaultValue: true, description: 'Add more users?', name: 'moreUsers']
                    ])

                createNewUser = moreUsersInput['moreUsers']
            }
        }
    }
}

