# `bashbot`
#### _by Patrick Altaie_

Looks bash.org quotes by ID up on bash.org and posts to the Slack channel where the slash command was sent

## Running Locally
Run `mvn clean install spring-boot:run` which will run the slash command listener at
[http://localhost:8080/slash-command](http://localhost:8080/slash-command)

If you'd like to have Slack point to your local environment, click
[here](https://api.slack.com/apps/A68BZBXEC/slash-commands)
and edit the `bash` Request URL to point to your local environment (use e.g. [ngrok](https://ngrok.com/) to expose your
environment to the internet)

## Pushing your solution to IBM Bluemix (Cloud Foundry)
Simply run `cf push` from the project's root!