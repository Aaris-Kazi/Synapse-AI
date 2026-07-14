# Synapse AI


This is a build a project for a backend to handle google oauth and chat with ChatGPT

## Services Type
- ChatGPT
- Ollama
- Google Oauth

## Commands to run

### For linux
    export $(cat /home/warhero/.env | xargs) && mvn clean install

### For windows powershell command
    Get-Content .env | % { $k,$v=$_.split('='); Set-Item env:$k $v }; mvn clean install