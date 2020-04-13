def pullAndPush() {
    // Stash all local changes not in the commit, because merging fails with local uncommitted changes.
    sh 'git stash'

    lock("GitRepository-infrastructure") {
        def statusCode = -1
        def maxRetries = 3
        for (def counter = 0; statusCode != 0 && counter < maxRetries; counter++) {
            statusCode = sh(script:"git pull --rebase origin master && git push origin HEAD:master", returnStatus:true)
        }
        if (statusCode != 0) {
            error "Wasn't able to push changes to git in ${maxRetries} tries ..."
        }
    }
}

return this