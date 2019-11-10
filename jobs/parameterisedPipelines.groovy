def gitHubCredentialsId = '43cee12e-c6be-41e5-b5c3-79e3c47c1292'

def hihAutomationJobs = [
        [repo: 'hih/groovy-jenkins-java-api-integration-test']
]

def bhAutomationJobs = [
        [repo: 'bh/groovy-jenkins-java-automation-tests']
]

// freestyle pipelines
hihAutomationJobs.each { job ->
    def (pipelineGithubOrg, pipelineGithubRepoName) = job.repo.split('/')
    def pipelineGitHubUrl = "https://github.pohzn.com/${pipelineGithubOrg}/${pipelineGithubRepoName}.git"
    pipelineJob(pipelineGithubRepoName) {
        description("on GitHub: <a href=\"${pipelineGitHubUrl}\">${pipelineGitHubUrl}</a>")
        definition {
            cpsScm {
                scm {
                    git {
                        branch("development")
                        remote {
                            url(pipelineGitHubUrl)
                            credentials(gitHubCredentialsId)
                        }
                    }
                }
                scriptPath("Jenkinsfile")
            }

        }

        triggers {
            scm('@midnight')
        }

        parameters {
            stringParam {
                name('HNGT_ENVIRONMENT')
                defaultValue('tst')
                description('Hngt environment')
                trim(true)
            }
        }

        parameters {
            stringParam {
                name('CERTIFICATE')
                defaultValue('')
                description('certificate')
                trim(true)
            }
        }

        parameters {
            stringParam {
                name('TESTSUITE')
                defaultValue('')
                description('Test Suite')
                trim(true)
            }
        }

        parameters {
            stringParam {
                name('GIT_BRANCH')
                defaultValue('development')
                description('Git Branch')
                trim(true)
            }
        }

    }
}


// freestyle pipelines
bhAutomationJobs.each { job ->
    def (pipelineGithubOrg, pipelineGithubRepoName) = job.repo.split('/')
    def pipelineGitHubUrl = "https://github.pohzn.com/${pipelineGithubOrg}/${pipelineGithubRepoName}.git"
    pipelineJob(pipelineGithubRepoName) {
        description("on GitHub: <a href=\"${pipelineGitHubUrl}\">${pipelineGitHubUrl}</a>")
        definition {
            cpsScm {
                scm {
                    git {
                        branch("development")
                        remote {
                            url(pipelineGitHubUrl)
                            credentials(gitHubCredentialsId)
                        }
                    }
                }
                scriptPath("Jenkinsfile")
            }

        }

        triggers {
            scm('@midnight')
        }

        parameters {
            stringParam {
                name('ENV_NAME')
                defaultValue('tst')
                description('Environment')
                trim(true)
            }
        }

        parameters {
            stringParam {
                name('TAG_NAME')
                defaultValue('@regression')
                description('tag name')
                trim(true)
            }
        }

        parameters {
            stringParam {
                name('BROWSER')
                defaultValue('chrome')
                description('Browser')
                trim(true)
            }
        }

        parameters {
            stringParam {
                name('DRIVER_LOCATION')
                defaultValue('/usr/bin/')
                description('driver location')
                trim(true)
            }
        }

    }
}



