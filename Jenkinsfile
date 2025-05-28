pipeline {
    agent any

    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M3"
    }

    stages{
        stage('Checkout') {
            steps {
                // Get source from a GitHub repository
                git branch: 'main', url: 'https://github.com/harryitpro/springbootapp-demos.git'
            }
        }
        stage('Clean') {
            steps {
                sh 'mvn clean'
            }
        }
        stage('Compile') {
            steps {
                sh 'mvn compile'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Package') {
            steps {
                // Run Maven on a Unix agent.
                withMaven(maven: 'M3') {
                    sh "mvn package"
                }
            }
            post {
                always {
                    // Publish JUnit test results
                    junit '**/target/surefire-reports/*.xml'
                    // Publish JaCoCo coverage report
                    recordCoverage tools: [[parser: 'JACOCO', pattern: '**/target/site/jacoco/jacoco.xml']],
                                   sourceCodeRetention: 'EVERY_BUILD',
                                   qualityGates: [[metric: 'LINE', threshold: 80.0, type: 'PROJECT', unstable: true]]
                }
            }
        }
    }
}
