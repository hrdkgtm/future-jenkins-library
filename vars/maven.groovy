#!/usr/bin/env groovy

def call(Map param){
    pipeline {
        agent any
        stages {
            stage('Build') {
                steps {
                    sh 'mvn -B -DskipTests clean package'
                }
            }
            stage('Test') {
                steps {
                    sh 'mvn test'
                }
                post {
                    always {
                        junit 'target/surefire-reports/*.xml'
                    }
                }
            }
            stage('Deliver') {
                steps {
                    // get the deliver script from jenkins-library
                    getScript()
                    sh 'bash ./deliver.sh'
                }
            }
        }
    }
}

def getScript(){
    // Load script from library with package path
    def my_script = libraryResource 'scripts/deliver.sh'

    // create a file with script_bash content
    writeFile file: './deliver.sh', text: my_script
}