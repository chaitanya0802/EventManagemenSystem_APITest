pipeline {
    agent any
 
    tools {
        maven 'maven 3.9.11'  
        jdk 'jdk-21'   
    }
 
    environment { 
        GIT_REPO = 'https://github.com/chaitanya0802/EventManagemenSystem_APITest.git'
        BRANCH = 'main'
        REPORT_PATH = 'target/surefire-reports'
    }
 
    //stags
    stages {
        stage('Checkout Code') {
            steps {
                git branch: "${BRANCH}", url: "${GIT_REPO}"
            }
        }
 
        stage('Build & Run Tests') {
            steps {
               bat 'mvn test -DsuiteXmlFile=testng.xml'
            }
        }
 
        stage('Publish Reports') {
            steps {
		//report
                publishHTML(target: [
                    reportName: 'Extent Report',
                    reportDir: 'target/reports', 
                    reportFiles: 'ExtentReport.html',     
                    keepAll: true,
                    alwaysLinkToLastBuild: true,
                    allowMissing: false
                ])

            }
            
        }
    }
 
    post {
        success {
            echo 'Build completed successfully!'
        }
        failure {
            echo 'Build failed. Please check logs.'
        }
    }
}
