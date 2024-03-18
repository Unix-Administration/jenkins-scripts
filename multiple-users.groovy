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

if (userExists(username)) {
    echo "User '${username}' already exists."
} else {
    def instance = jenkins.model.Jenkins.instance
    def hudsonRealm = instance.getSecurityRealm()

    def newUser = hudsonRealm.createAccount(username, password)
    newUser.save()

    echo "Jenkins user '${username}' created successfully."
}
