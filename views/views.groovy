// use the organisation and repo name as the view name

listView('HIH') {
    description('Pipelines and Jobs')
    filterBuildQueue()
    jobs {
        regex(/hih-[a-z|-]+$/)
    }

    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}

listView('BH') {
    description('Pipelines and Jobs')
    filterBuildQueue()
    jobs {
        regex(/bh-[a-z|-]+$/)
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}

listView('DevOps') {
    description('Pipelines and Jobs')
    filterBuildQueue()

    filterBuildQueue()
    jobs {
        regex(/dev-ops-common-[a-z|-]+$/)
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}

buildMonitorView('HIH Builds') {
    description('HIH Builds')
    jobs {
        regex(/hih-[a-z|-]+.*(development)/)
        recurse(shouldRecurse = true)
        statusFilter(StatusFilter.ENABLED)
    }
}

buildMonitorView('BH Builds') {
    description('BH Builds')
    jobs {
        regex(/bh-[a-z|-]+.*(development)/)
        recurse(shouldRecurse = true)
        statusFilter(StatusFilter.ENABLED)
    }
}

buildMonitorView('DevOps Builds') {
    description('DevOps Builds')
    jobs {
        regex(/dev-ops-common-[a-z|-]+.*(development)/)
        recurse(shouldRecurse = true)
        statusFilter(StatusFilter.ENABLED)
    }
}



