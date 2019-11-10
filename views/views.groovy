// use the organisation and repo name as the view name


listView('DevOps') {
    description('Pipelines and Jobs')
    filterBuildQueue()

    filterBuildQueue()
    jobs {
        regex(/dev-ops--[a-z|-]+$/)
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



buildMonitorView('DevOps Builds') {
    description('DevOps Builds')
    jobs {
        regex(/dev-ops--[a-z|-]+.*(development)/)
        recurse(shouldRecurse = true)
        statusFilter(StatusFilter.ENABLED)
    }
}



