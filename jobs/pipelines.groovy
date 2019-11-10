def gitHubCredentialsId = 'bainss-pat-github'

def multiBranchpipelines = [
        [repo: 'polarpoint-io/pol-hss-simple-springboot'],

]

def freestyleJobs = []


def pipelineJobs = []

multiBranchpipelines.each { pipeline ->
    def (githubOrg, githubRepoName) = pipeline.repo.split('/')
    def gitHubUrl = "https://github.pohzn.com/${githubOrg}/${githubRepoName}"
    def buildName = (pipeline.buildName == null) ? "${githubOrg}--${githubRepoName}" : pipeline.buildName

    multibranchPipelineJob(buildName) {
        description("on GitHub: <a href=\"${gitHubUrl}\">${gitHubUrl}</a>")

        branchSources {
            branchSource {
                source {
                    github {
                        repoOwner(githubOrg)
                        id(buildName)
                        repository(githubRepoName)
                        apiUri("https://github.pohzn.com/api/v3")
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
                        ignoredAuthors("jenkins@mycnets.com")
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
        triggers {
            periodic(720)
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
    def pipelineGitHubUrl = "https://github.pohzn.com/${pipelineGithubOrg}/${pipelineGithubRepoName}"
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
