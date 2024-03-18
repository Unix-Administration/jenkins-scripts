def userExists(username) {
    def existingUser = jenkins.model.Jenkins.instance.getUser(username)
    return existingUser != null
}

def userInput = input(
    id: 'userInput',
    message: 'Introduce a username and a password',
    parameters: [
        [$class: 'TextParameterDefinition', defaultValue: '', description: 'Insert a username', name: 'username'],
        [$class: 'TextParameterDefinition', defaultValue: '', description: 'Insert a password', name: 'password']
    ])

def username = userInput['username']
def password = userInput['password']

if (username.isEmpty() || password.isEmpty()) {
    error("Username and password are required fields.")
}

def instance = jenkins.model.Jenkins.instance
def hudsonRealm = instance.getSecurityRealm()

if (userExists(username)) {
    echo "User '${username}' already exists."
} else {
    def newUser = hudsonRealm.createAccount(username, password)
    newUser.save()

    echo "Jenkins user '${username}' created successfully."
}
