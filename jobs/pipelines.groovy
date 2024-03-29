def gitHubCredentialsId = 'github-pat-credentials'

def multiBranchpipelines = [
        [repo: 'polarpoint-io/spring-boot-service'],

]

def freestyleJobs = []


def pipelineJobs = []

multiBranchpipelines.each { pipeline ->
    def (githubOrg, githubRepoName) = pipeline.repo.split('/')
    def gitHubUrl = "https://github.com/${githubOrg}/${githubRepoName}"
    def buildName = (pipeline.buildName == null) ? "${githubOrg}--${githubRepoName}" : pipeline.buildName

    multibranchPipelineJob(buildName) {
        description("on GitHub: <a href=\"${gitHubUrl}\">${gitHubUrl}</a>")

        branchSources {
            branchSource {
                source {
                    github {
                        repositoryUrl(gitHubUrl)
                        configuredByUrl(false)
                        repoOwner(githubOrg)
                        id(buildName)
                        repository(githubRepoName)
                        apiUri("https://api.github.com")
                        credentialsId(gitHubCredentialsId)
                        traits {
                            headWildcardFilter {
                                includes('release master development hotfix bugfix feature *BH-* *PC-* *HD-* *SLR-*')
                                excludes('PR')
                            }
                        }
                    }
                }

                buildStrategies {
                    ignoreCommitterStrategy {
                        ignoredAuthors("jenkins@local")
                        allowBuildIfNotExcludedAuthor(true)
                    }
                        //skip-initial-build(true)
                    
                }

                strategy {
                    namedExceptionsBranchPropertyStrategy {
                        defaultProperties {
                            buildRetentionBranchProperty {
                                buildDiscarder {
                                    logRotator {
                                        daysToKeepStr("180")
                                        numToKeepStr('30')
                                        artifactDaysToKeepStr("1")
                                        artifactNumToKeepStr("1")
                                    }
                                }
                            }
                        }

                        namedExceptions {
                            named {
                                name("master production")
                                props {
                                    buildRetentionBranchProperty {
                                        buildDiscarder {
                                            logRotator {
                                                daysToKeepStr("180")
                                                numToKeepStr('30')
                                                artifactDaysToKeepStr("180")
                                                artifactNumToKeepStr("5")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        orphanedItemStrategy {
            discardOldItems {
                daysToKeep(1)  // remove merged pipelines every day
            }
        }
/*
        properties {
            noTriggerOrganizationFolderProperty {
                branches('*')
            }
        }
*/
        configure {
            def traits = it / sources / data / 'jenkins.branch.BranchSource' / source / traits
            traits << 'org.jenkinsci.plugins.github__branch__source.BranchDiscoveryTrait' {
                strategyId(3)
            }
        }
    }
}


pipelineJobs.each { job ->
    def (pipelineGithubOrg, pipelineGithubRepoName) = job.repo.split('/')
    def pipelineGitHubUrl = "https://github.com/${pipelineGithubOrg}/${pipelineGithubRepoName}"
    pipelineJob(pipelineGithubRepoName) {
        description("on GitHub: <a href=\"${pipelineGitHubUrl}\">${pipelineGitHubUrl}</a>")
        definition {
            cpsScm {
                scm {
                    git {
                        remote {
                            url(pipelineGitHubUrl)
                            credentials(gitHubCredentialsId)
                        }
                    }
                }
                scriptPath("Jenkinsfile")
            }
            triggers {
                scm('@midnight')
            }
        }
    }
}
