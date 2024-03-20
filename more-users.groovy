def userExists(username, email) {
    def existingUser = jenkins.model.Jenkins.instance.getUser(username)
    def existingEmailUser = jenkins.model.Jenkins.instance.getUserByFullName(email)
    return existingUser != null || existingEmailUser != null
}

node {
    stage('Create Jenkins Users') {
        def createNewUser = true

        while (createNewUser) {
            def username = ''
            def password = ''
            def confirmPassword = ''
            def fullName = ''
            def email = ''

            while (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty() || email.isEmpty() || userExists(username, email)) {
                stage('User Input') {
                    def userInput = input(
                        id: 'userInput',
                        message: 'Introduce the following details to create a user',
                        parameters: [
                            [$class: 'TextParameterDefinition', defaultValue: '', description: 'Insert a username', name: 'username'],
                            [$class: 'PasswordParameterDefinition', defaultValue: '', description: 'Insert a password', name: 'password'],
                            [$class: 'PasswordParameterDefinition', defaultValue: '', description: 'Confirm password', name: 'confirmPassword'],
                            [$class: 'TextParameterDefinition', defaultValue: '', description: 'Insert a full name', name: 'fullName'],
                            [$class: 'TextParameterDefinition', defaultValue: '', description: 'Insert an email', name: 'email']
                        ])

                    username = userInput['username']
                    password = userInput['password'].toString() // Retrieve password as string
                    confirmPassword = userInput['confirmPassword'].toString() // Retrieve confirmPassword as string
                    fullName = userInput['fullName']
                    email = userInput['email']

                    if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
                        echo "All fields are required and cannot be empty."
                    } else if (password != confirmPassword) {
                        echo "Passwords do not match. Please try again."
                    } else if (userExists(username, email)) {
                        echo "User with the same username or email already exists. Please choose a different username and email."
                    }
                }
            }

            def instance = jenkins.model.Jenkins.instance
            def hudsonRealm = instance.getSecurityRealm()

            def newUser = new hudson.model.User(username, password, fullName, email)
            hudsonRealm.createAccount(newUser)

            echo "Jenkins user '${username}' with full name '${fullName}' and email '${email}' created successfully."
            echo "Press 'q' to finish the process."
        }
    }
}
