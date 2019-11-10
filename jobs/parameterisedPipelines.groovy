def gitHubCredentialsId = 'bainss-pat-github'


def polarpointAutomationJobs = [
        [repo: 'polarpoint-io/groovy-jenkins-java-integration-tests']
]


// freestyle pipelines
polarpointAutomationJobs.each { job ->
    def (pipelineGithubOrg, pipelineGithubRepoName) = job.repo.split('/')
    def pipelineGitHubUrl = "https://github.com/${pipelineGithubOrg}/${pipelineGithubRepoName}.git"
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



